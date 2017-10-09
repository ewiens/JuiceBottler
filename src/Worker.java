import java.util.ArrayList;
import java.util.List;
//TODO stop baby threads in the big threads.
//TODO do this with the work boolean. Separate run methods for each thread?
public class Worker implements Runnable {

//	private Thread[] workerThreads = new Thread[5];
//	private Thread workerThread;
	private boolean isWorking;
	
	private ArrayList<Orange> fromList = new ArrayList<Orange>();
	private ArrayList<Orange> toList = new ArrayList<Orange>();
	
	private Thread workerThread;
	private volatile int orangeCount;

	/* The methods Worker, startWorker, stopWorkers, and waitToStopWorkers are modified from the corresponding
	 * methods in the Plant class as given by Nate Williams. */
	
	Worker(String workerName, ArrayList<Orange> to, ArrayList<Orange> from) {
		fromList = from;
		toList = to;

        workerThread = new Thread(this, "Worker "+ workerName);
        startWorker();
    }
			
	/** starts the worker**/
	public void startWorker(){
		isWorking = true;
//		System.out.println("Start worker "+workerThread);
		orangeCount = 0;
		workerThread.start();
	}
	/** stop the worker**/
	public void stopWorkers(){
		isWorking = false;
		waitToStopWorkers();
	}

	/**joins the worker threads**/
	public void waitToStopWorkers() {
        try {
            workerThread.join();
        } catch (InterruptedException e) {
            System.err.println(workerThread.getName() + " stop malfunction");
        }
    }

    /**gets worker to list**/
    public ArrayList<Orange> getToList(){
    	return toList;
    }
    /**Gets worker from list**/
    public ArrayList<Orange> getFromList(){
    	return fromList;
    }

    /**Run method for Worker threads**/
	public void run() {
		//TODO
//		System.out.println("running thread"+this);
		while(isWorking){
			//Checks to see if that worker gets oranges from a queue
			if(fromList != null){
				Orange o = AssemblyLine.getWork(fromList);
				// Checks to make sure that there is an orange before processing it
		    	if (o != null){
			    	o.runProcess();
			    	AssemblyLine.sendWork(o,toList);
		    	}		
			}else{
				//Creates the orange so that it can be processed.
				Orange o = new Orange();
				System.out.print(".");
				orangeCount++;		    
				o.runProcess();
				AssemblyLine.sendWork(o, toList);
			}
			
		}
		
	}

	public int getOrangeCount() {
		// TODO Auto-generated method stub
		return orangeCount;
	}


}
