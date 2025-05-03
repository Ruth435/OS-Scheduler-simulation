//M. M. Kuttel 2025 mkuttel@gmail.com
package barScheduling;

import java.util.Random;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/*
 Barman Thread class.
 */

public class Barman extends Thread {
	

	private CountDownLatch startSignal;
	private BlockingQueue<DrinkOrder> orderQueue;
	private BlockingQueue<Long> waitTimes;
	int schedAlg =0;
	int q=10000; //really big if not set, so FCFS
	private int switchTime;
	long busyTime = 0;
	long idleTime = 0;
	long temp = 0; //to hold a time momentarily
	int patronNum = 400;
	private long[] patronWaits = new long[500]; //assuming the bar capacity is no more than this
	
	public long[] getWaitTimes() {
    return patronWaits;
	}

	public double getCPUUtilization() { //returns CPU utilization as a percentage
		double temp = (double)(idleTime + busyTime);
		temp = busyTime/temp;
		temp *= 100;
		return temp;
	}


	Barman(  CountDownLatch startSignal,int sAlg) {
		//which scheduling algorithm to use
		this.schedAlg=sAlg;
		if (schedAlg==1) 
		{
			this.orderQueue = new PriorityBlockingQueue<>(5000, Comparator.comparingInt(DrinkOrder::getExecutionTime)); //SJF
			this.waitTimes = new PriorityBlockingQueue<>();
		}
		else 
		{
			this.orderQueue = new LinkedBlockingQueue<>(); //FCFS & RR
			this.waitTimes = new LinkedBlockingQueue<>();
		}
	    this.startSignal=startSignal;
	}
	
	Barman(  CountDownLatch startSignal,int sAlg,int quantum, int sTime) { //overloading constructor for RR which needs q
		this(startSignal, sAlg);
		q=quantum;
		switchTime=sTime;
	}

	public void placeDrinkOrder(DrinkOrder order) throws InterruptedException {
        orderQueue.put(order);
		waitTimes.put(System.currentTimeMillis());
    }
	
	public void run() {
		int interrupts=0;
		long totalWait;
		try {
			DrinkOrder currentOrder;
			
			startSignal.countDown(); //barman ready
			startSignal.await(); //check latch - don't start until told to do so
			temp = System.currentTimeMillis();
			if ((schedAlg==0)||(schedAlg==1)) { //FCFS and non-preemptive SJF
				while(true) {
					currentOrder=orderQueue.take();
					totalWait = System.currentTimeMillis() - waitTimes.take();
					idleTime += (System.currentTimeMillis()- temp);
					patronNum = Integer.parseInt(currentOrder.toString().substring(0, currentOrder.toString().indexOf(':')));
					patronWaits[patronNum] += totalWait;
					System.out.println("---Barman preparing drink for patron "+ currentOrder.toString());
					temp = System.currentTimeMillis();
					sleep(currentOrder.getExecutionTime()); //processing order (="CPU burst")
					busyTime += (System.currentTimeMillis() - temp);
					System.out.println("---Barman has made drink for patron "+ currentOrder.toString());
					currentOrder.orderDone();
					temp = System.currentTimeMillis();
					sleep(switchTime);//cost for switching orders
					idleTime += (System.currentTimeMillis() - temp);
					temp = System.currentTimeMillis();
				}
			}
			else { // RR 
				int burst=0;
				int timeLeft=0;
				System.out.println("---Barman started with q= "+q);

				while(true) {
					System.out.println("---Barman waiting for next order ");
					currentOrder=orderQueue.take();
					totalWait = System.currentTimeMillis() - waitTimes.take();
					idleTime += (System.currentTimeMillis()- temp);
					patronNum = Integer.parseInt(currentOrder.toString().substring(0, currentOrder.toString().indexOf(':')));
					patronWaits[patronNum] += totalWait;
					System.out.println("---Barman preparing drink for patron "+ currentOrder.toString() );
					burst=currentOrder.getExecutionTime();
					if(burst<=q) { //within the quantum
						temp = System.currentTimeMillis();
						sleep(burst); //processing complete order ="CPU burst"
						busyTime += (System.currentTimeMillis() - temp);
						System.out.println("---Barman has made drink for patron "+ currentOrder.toString());
						currentOrder.orderDone();
					}
					else {
						temp = System.currentTimeMillis();
						sleep(q);
						busyTime += (System.currentTimeMillis() - temp);
						timeLeft=burst-q;
						System.out.println("--INTERRUPT---preparation of drink for patron "+ currentOrder.toString()+ " time left=" + timeLeft);
						interrupts++;
						currentOrder.setRemainingPreparationTime(timeLeft);
						orderQueue.put(currentOrder); //put back on queue at end
						waitTimes.put(System.currentTimeMillis());
					}
					temp = System.currentTimeMillis();
					sleep(switchTime);//cost for switching orders
					idleTime += (System.currentTimeMillis() - temp);
					temp = System.currentTimeMillis();
				}
			}
				
		} catch (InterruptedException e1) {
			temp = System.currentTimeMillis();
			System.out.println("---Barman is packing up ");
			System.out.println("---number interrupts="+interrupts);
			idleTime += (System.currentTimeMillis()- temp);
			System.out.println("Idle time: " + idleTime + "ms \tBusy time: " + busyTime + "ms");
			long check = 8;
			patronNum = 0;
			while (check != 0)
			{
				check = patronWaits[patronNum];
				System.out.println("Patron " + patronNum + " Waited " + check + "ms" );
				patronNum++;
				check = patronWaits[patronNum];
			}
		}
	}
}


