import java.util.ArrayList;

/** Gets the oranges and sends them to the next place **/
public class AssemblyLine {
/* Cordell Anderson helped me to realize that this made my life much easier
I looked at his code for this class, which he got from Nate Williams in class, for inspiration
but to my knowledge I did not copy it. 
*/	
	
	/**Gets the work to do on the orange**/
	public synchronized static Orange getWork(ArrayList<Orange> fromList){
		// get orange from its queue and send the orange back so that work can be done on it
		
		if(fromList.size()>0){
			Orange o = fromList.get(0);
			fromList.remove(o);
			return o;
		} else{
			return null;
		}
	}
	
	/**Sends the orange to the next place **/
	public synchronized static void sendWork(Orange o, ArrayList<Orange> toList ){
		// adds the orange to the next queue
		toList.add(o);
	}
	

}
