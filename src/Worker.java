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

	/* The methods Worker, startWorker, stopWorkers, and waitToStopWorkers are modified from the corresponding
	 * methods in the Plant class as given by Nate Williams. */
	
	Worker(Thread workerThread, ArrayList<Orange> to, ArrayList<Orange> from) {
		fromList = from;
		toList = to;
        workerThread = new Thread(this, "Worker ");
    }
			
	/** starts the worker**/
	public void startWorker(Thread workerThread){
		isWorking = true;
		workerThread.start();
	}
	/** stop the worker**/
	public void stopWorkers(){
		isWorking = false;
	}

	/**joins the worker threads**/
	public void waitToStopWorkers(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
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
				o.runProcess();
				AssemblyLine.sendWork(o, toList);
			}
			
		}
		
	}


}
