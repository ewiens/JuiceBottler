public class Orange {
	/** The orange class was given to us by Nate Williams.
	 * I only changed one line and added comments. **/
	
    public enum State {
        Fetched(15), // status(time)
        Peeled(38),
        Squeezed(29),
        Bottled(10),
        Processed(1);
    	
        private static final int finalIndex = State.values().length - 1;
        
        final int timeToComplete;

        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }
        
        State getState() {
        	/**Gets the current state of the orange to do work on it**/
        	return State.values()[this.ordinal()];
        }

        State getNext() {
            int currIndex = this.ordinal();
            if (currIndex >= finalIndex) {
                throw new IllegalStateException("Already at final state");
            }
            return State.values()[currIndex + 1];
        }
        int getNextIndex(){
        	int currIndex = this.ordinal();
        	if(currIndex >= finalIndex){
        		throw new IllegalStateException("Already at final state");
        	}
        	return currIndex+1;
        }
    }// end state

    private State state;

    public Orange() {
        state = State.Fetched;
        doWork();
    }

    public State getState() {
        return state;
    }

    public void runProcess() {
        // Don't attempt to process an already completed orange
        if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed");
        }
        doWork();
        state = state.getNext();
    	
    }
    
    public void doWork() {
        // Sleep for the amount of time necessary to do the work
//    	System.out.println("doing work: "+ state);
        try {
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            System.err.println("Incomplete orange processing, juice may be bad");
        }
    }
    
}