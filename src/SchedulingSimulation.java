//M. M. Kuttel 2025 mkuttel@gmail.com

package barScheduling;
// the main class, starts all threads and the simulation


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;


public class SchedulingSimulation {
	static int noPatrons=10; //number of customers - default value if not provided on command line
	static int sched=0; //default scheduling algorithm, 0= FCFS, 1=SJF, 2=RR
	static int q=10000, s=0;
	static long seed=0;
	static CountDownLatch startSignal;	
	static Patron[] patrons; // array for customer threads
	static Barman Sarah;
	public static BlockingQueue<Long> finishTimes = new LinkedBlockingQueue<>();
	public static String algName = "";


public static void writeSummaryToCSV(String filename, int[] throughput) {
    // Collect stats
    long[] turnarounds = new long[noPatrons];
    long[] responses = new long[noPatrons];
    long[] waits = Arrays.copyOf(Sarah.getWaitTimes(), noPatrons);  // Trim to real size

    for (int i = 0; i < noPatrons; i++) {
        turnarounds[i] = patrons[i].getTurnaroundTime();
        responses[i] = patrons[i].getResponseTime();
    }

    try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
        writer.printf(
            "%s,%d,%d,%d,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f%n",
            algName,
            q,
            s,
            noPatrons,
            seed,
            Sarah.getCPUUtilization(),
            StatsUtil.mean(turnarounds),
            StatsUtil.median(turnarounds),
            StatsUtil.stdDev(turnarounds),
            StatsUtil.mean(responses),
            StatsUtil.median(responses),
            StatsUtil.stdDev(responses),
            StatsUtil.mean(waits),
            StatsUtil.median(waits),
            StatsUtil.stdDev(waits),
            StatsUtil.mean(throughput),
            StatsUtil.median(throughput),
            StatsUtil.stdDev(throughput)
        );
    } catch (IOException e) {
        System.err.println("Error writing summary to CSV: " + e.getMessage());
    }
}

	public static void main(String[] args) throws InterruptedException, IOException {

		//deal with command line arguments if provided
		if (args.length>=1) noPatrons=Integer.parseInt(args[0]);  //total people to enter room
		if (args.length>=2) sched=Integer.parseInt(args[1]); 	// alg to use
		if (args.length>=3) s=Integer.parseInt(args[2]);  //context switch 
		if(args.length>=4) q=Integer.parseInt(args[3]);  // time slice for RR
		if(args.length>=5) seed=Integer.parseInt(args[4]); // random number seed- set to compare apples with apples		
		

		startSignal= new CountDownLatch(noPatrons+2);//Barman and patrons and main method must be ready
		//create barman
		System.out.println("BARMAN TO START: " +sched + " " + q + " " + s + " SEED: " + seed);
        Sarah= new Barman(startSignal,sched,q,s); 
     	Sarah.start();
	    //create all the patrons, who all need access to Barman
		patrons = new Patron[noPatrons];
		long startTime = System.nanoTime();
		for (int i=0;i<noPatrons;i++) {
			patrons[i] = new Patron(i,startSignal,Sarah,seed);
			patrons[i].start();
		}
		
		if (seed>0) DrinkOrder.random = new Random(seed);// for consistent Patron behaviour

		
		System.out.println("------Sarah the Barman Scheduling Simulation------");
		System.out.println("-------------- with "+ Integer.toString(noPatrons) + " patrons---------------");
		switch(sched) {
		  case 0:
			  System.out.println("-------------- and FCSF scheduling ---------------");
			  algName = "FCFS";
		    break;
		  case 1:
			  System.out.println("-------------- and SJF scheduling ---------------");
			  algName = "SJF";
		    break;
		  case 2:
			  System.out.println("-------------- and RR scheduling with q="+q+"-------------");
			  algName = "RR";
		}
		
			
      	startSignal.countDown(); //main method ready
      	
      	//wait till all patrons done, otherwise race condition on the file closing!
      	for (int i=0;i<noPatrons;i++)  patrons[i].join();
		long endTime = System.nanoTime();
    	System.out.println("------Waiting for Barman------");
    	Sarah.interrupt();   //tell Barman to close up
    	Sarah.join(); //wait till she has
      	System.out.println("------Bar closed------");
		int numWindows = (int)Math.ceil((endTime-startTime)/1000000000.0); //window being 1 seconds here
		int[] throughput = new int[numWindows];
		long maxTime = 0;
		long minTime = 30000000;
		for (int i=0;i<noPatrons;i++) {
			long patronEnd = finishTimes.take(); // Blocks if the queue is empty
			maxTime = Math.max(maxTime, patronEnd);
			int space = (int)Math.ceil((patronEnd - startTime)/1000000000.0); //calculate in which window the parton finished
			patronEnd = patronEnd/1000000;
			minTime = Math.min(minTime, patronEnd);
			throughput[space-1]++;
		}	
		writeSummaryToCSV("simulation_summary.csv", throughput);	
 	}
}
