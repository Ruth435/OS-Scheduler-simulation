//M. M. Kuttel 2025 mkuttel@gmail.com
package barScheduling;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import barScheduling.SchedulingSimulation;

/*class for the patrons at the bar*/

public class Patron extends Thread {
	
	private Random random;// for variation in Patron behaviour
	private CountDownLatch startSignal; //all start at once, actually shared
	private Barman theBarman; //the Barman is actually shared though

	private int ID; //thread ID 
	private int numberOfDrinks;
	private long turnaroundTime;
	private long responseTime;
	private boolean firstDrink = true;
	private long endTime;

	private DrinkOrder [] drinksOrder;

	Patron( int ID,  CountDownLatch startSignal, Barman aBarman, long seed) {
		this.ID=ID;
		this.startSignal=startSignal;
		this.theBarman=aBarman;
		this.numberOfDrinks=5; // number of drinks is fixed
		drinksOrder=new DrinkOrder[numberOfDrinks];
		if (seed>0) random = new Random(seed);// for consistent Patron behaviour
		else random = new Random();
	}

	public long getTurnaroundTime() {
		return turnaroundTime;
	}
	
	public long getResponseTime() {
		return responseTime;
	}
	
	
//this is what the threads do	
	public void run() {
		try {
			
			//Do NOT change the block of code below - this is the arrival times
			startSignal.countDown(); //this patron is ready
			startSignal.await(); //wait till everyone is ready
			int arrivalTime = ID*50; //fixed arrival for testing
	        sleep(arrivalTime);// Patrons arrive at staggered  times depending on ID 
			System.out.println("+new thirsty Patron "+ this.ID +" arrived"); //Patron has actually arrived
			//End do not change
			long startTime = System.nanoTime();
	        for(int i=0;i<numberOfDrinks;i++) {
	        	drinksOrder[i]=new DrinkOrder(this.ID); //order a drink (=CPU burst)	        
	        	//drinksOrder[i]=new DrinkOrder(this.ID,i); //fixed drink order (=CPU burst), useful for testing
				System.out.println("Order placed by " + drinksOrder[i].toString()); //output in standard format  - do not change this
				long startResponseTime = System.nanoTime();
				theBarman.placeDrinkOrder(drinksOrder[i]);
				drinksOrder[i].waitForOrder();
				if(firstDrink)
				{
					responseTime = (System.nanoTime() - startResponseTime)/1000000 ;
					firstDrink = false;
				}
				System.out.println("Drinking patron " + drinksOrder[i].toString());
				sleep(drinksOrder[i].getImbibingTime()); //drinking drink = "IO"
			}
			endTime = System.nanoTime();
			System.out.println("Patron "+ this.ID + " completed ");
			turnaroundTime = (endTime - startTime)/1000000;
			SchedulingSimulation.logToFile("patron_stats.txt","Patron " + this.ID + " Turnaround Time: " + turnaroundTime + "ms Response Time: " + responseTime + "ms");
			System.out.println("Patron " + this.ID + " Turnaround Time: " + turnaroundTime + "ms Response Time: " + responseTime + "ms");
			SchedulingSimulation.finishTimes.put(endTime);
		} catch (InterruptedException e1) {  //do nothing
		}
}
}
	

