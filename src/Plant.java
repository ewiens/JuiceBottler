import java.util.ArrayList;
import java.util.List;


/* https://howtoprogramwithjava.com/java-multithreading/ 
 * This source was used to achieve a better understanding of multithreading.
 * To my knowledge I did use any code from them. 
 */
public class Plant implements Runnable {
    // How long do we want to run the plants
    public static final long PROCESSING_TIME = 5 * 1000;
    
    public final static int ORANGES_PER_BOTTLE = 4;

    private static final int NUM_PLANTS = 2;
//    public static final int NUM_WORKERS = 4; //Unused but for reference

    Plant() {
        orangesProvided = 0;
        orangesProcessed = 0;
        thread = new Thread(this, "Plant");
    }

    public static void main(String[] args) {
        // Startup the plants
        Plant[] plants = new Plant[NUM_PLANTS];
        for (int i = 0; i < NUM_PLANTS; i++) {
           plants[i] = new Plant();
           plants[i].startPlant();
        }

        // Give the plants time to do work
        delay(PROCESSING_TIME, "Plant malfunction");

        // Stop the plant, and wait for it to shutdown
        for (Plant p : plants) {
           p.stopPlant();
        }
        for (Plant p : plants) {
           p.waitToStop();
        }

        // Summarize the results
        int totalProvided = 0;
        int totalProcessed = 0;
        int totalBottles = 0;
        int totalWasted = 0;
        for (Plant p : plants) {
            totalProvided += p.getProvidedOranges();
            totalProcessed += p.getProcessedOranges();
            totalBottles += p.getBottles();
            totalWasted += p.getWaste();
        }
       System.out.println("Total provided/processed = " + totalProvided + "/" + totalProcessed);
       System.out.println("Created " + totalBottles +" bottles, wasted " + totalWasted + " oranges");
       System.out.println("Oranges per bottle "+ORANGES_PER_BOTTLE);
    }


    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }

    public int orangesProvided;   
    private final Thread thread;
    private int orangesProcessed;
    private volatile boolean timeToWork;

    
    /* Instantiate Worker */
    private Worker fetcher;
    private Worker peeler;
    private Worker squeezer;
    private Worker bottler;
//    public Worker processor;
    
	/* Instantiate ArrayLists */
	public ArrayList<Orange> toBePeeled = new ArrayList<Orange>(); // make these lists
	public ArrayList<Orange> toBeSqueezed = new ArrayList<Orange>();
	public ArrayList<Orange> toBeBottled = new ArrayList<Orange>();
	public ArrayList<Orange> toBeProcessed = new ArrayList<Orange>();
//	public volatile ArrayList<Orange> doneList = new ArrayList<Orange>();



    public void startPlant() {
        timeToWork = true;
        thread.start();
    }

    public void stopPlant() {
        timeToWork = false;
    }

    public void waitToStop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
        }
    }
    

    public void run() {
        System.out.println(Thread.currentThread().getName() + " Processing oranges");
        
        createWorkers();
        
        while (timeToWork) {
        }
        
        /* Tells the workers it's time to stop working */
        fetcher.stopWorkers();
        peeler.stopWorkers();
        squeezer.stopWorkers();
        bottler.stopWorkers();
      
        System.out.println("");
    }
    
    /** creates an orange and the fetcher does work**/
    public void beginFetcher(Worker myWorker){
    	Orange o = new Orange();
    	orangesProvided++;
    	System.out.print(".");
//    	o.runProcess();
    	AssemblyLine.sendWork(o, myWorker.getToList());
    }
    /**Non-fetchers do work**/
    public void beginProcess(Worker myWorker){
    	Orange o = AssemblyLine.getWork(myWorker.getFromList());
    	if (o != null){
//	    	o.runProcess();
	    	AssemblyLine.sendWork(o,myWorker.getToList());
    	}
    }
    
    /**Creates the workers in the plant**/
	private void createWorkers() {
		// Worker(name, toList, fromList)
		fetcher = new Worker("fetcher", toBePeeled, null);
		
		peeler = new Worker("peeler", toBeSqueezed, toBePeeled);

		squeezer = new Worker("squeezer", toBeBottled, toBeSqueezed);
		
		bottler = new Worker("botter", toBeProcessed, toBeBottled);
				
	}

    private int getProvidedOranges() {
    	orangesProvided = fetcher.getOrangeCount();
        return orangesProvided;
    }

    private int getProcessedOranges() {
    	  orangesProcessed = toBeProcessed.size();
        return orangesProcessed;
    }

    private int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    private int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE+(orangesProvided-orangesProcessed);
    }
}