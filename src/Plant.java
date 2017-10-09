import java.util.ArrayList;
import java.util.List;

public class Plant implements Runnable {
    // How long do we want to run the plants
    public static final long PROCESSING_TIME = 5 * 1000;

    private static final int NUM_PLANTS = 2;
//    public static final int NUM_WORKERS = 4; //Unused but for reference

    public static final int ORANGES_PER_BOTTLE = 4;

    private final Thread thread;
    private int orangesProvided;
    private int orangesProcessed;
    private volatile boolean timeToWork;

    
    /* Instantiate Worker */
    public Worker fetcher;
    public Worker peeler;
    public Worker squeezer;
    public Worker bottler;
//    public Worker processor;

    
    /* Instantiate Threads */
	public Thread fetchingThread = new Thread();
	public Thread peelingThread = new Thread();
	public Thread squeezingThread = new Thread();
	public Thread bottlingThread= new Thread();
//	public Thread processingThread = new Thread();

	
	/* Instantiate ArrayLists */
	public volatile ArrayList<Orange> toBePeeled = new ArrayList<Orange>();
	public volatile ArrayList<Orange> toBeSqueezed = new ArrayList<Orange>();
	public volatile ArrayList<Orange> toBeBottled = new ArrayList<Orange>();
	public volatile ArrayList<Orange> toBeProcessed = new ArrayList<Orange>();
//	public volatile ArrayList<Orange> doneList = new ArrayList<Orange>();


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


    Plant() {
        orangesProvided = 0;
        orangesProcessed = 0;
        thread = new Thread(this, "Plant");
    }

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
        	/* Makes the workers work*/
           beginFetcher(fetcher);
           beginProcess(peeler);
           beginProcess(squeezer);
           beginProcess(bottler);
        }
        
        /* Tells the workers it's time to stop working */
        fetcher.stopWorkers();
        peeler.stopWorkers();
        squeezer.stopWorkers();
        bottler.stopWorkers();
        
        /* Joins the worker threads*/
        fetcher.waitToStopWorkers(fetchingThread);
        peeler.waitToStopWorkers(peelingThread);
        squeezer.waitToStopWorkers(squeezingThread);
        bottler.waitToStopWorkers(bottlingThread);
        
        orangesProcessed = toBeProcessed.size();
      
        System.out.println("");
    }
    /** creates an orange and the fetcher does work**/
    public void beginFetcher(Worker myWorker){
    	Orange o = new Orange();
    	orangesProvided++;
    	System.out.print(".");
    	o.runProcess();
    	AssemblyLine.sendWork(o, myWorker.getToList());
    }
    /**Non-fetchers do work**/
    public void beginProcess(Worker myWorker){
    	Orange o = AssemblyLine.getWork(myWorker.getFromList());
    	if (o != null){
	    	o.runProcess();
	    	AssemblyLine.sendWork(o,myWorker.getToList());
    	}
    }
    
    /**Creates the workers in the plant**/
	private void createWorkers() {
			
		fetcher = new Worker(fetchingThread, toBePeeled, null);
		fetcher.startWorker(fetchingThread);
		
		peeler = new Worker(peelingThread, toBeSqueezed, toBePeeled);
		peeler.startWorker(peelingThread);
		
		squeezer = new Worker(squeezingThread, toBeBottled, toBeSqueezed);
		squeezer.startWorker(squeezingThread);
		
		bottler = new Worker(bottlingThread, toBeProcessed, toBeBottled);
		bottler.startWorker(bottlingThread);
		
//		processor = new Worker(processingThread, doneList, toBeProcessed);
//		processor.startWorker(processingThread);
				
	}

	public void processEntireOrange(Orange o) {
				
        while (o.getState() != Orange.State.Bottled) {
            o.runProcess();
        }  	
//        orangeCount++;
        orangesProcessed++;
//        if(orangeCount==4){
//        	o.bottleOranges();
//        }
    }

    public int getProvidedOranges() {
        return orangesProvided;
    }

    public int getProcessedOranges() {
        return orangesProcessed;
    }

    public int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    public int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE;
    }
}