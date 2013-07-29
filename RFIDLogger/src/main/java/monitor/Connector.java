package monitor;
import java.util.TimerTask;

public class Connector extends TimerTask {

// FIELDS
private static final Exception NoSuchHost = new Exception();
private LLRPListener listener;
private String host;
public RFIDMonitor monitor;

public Connector(LLRPListener l, String hostname){
	this.listener = l;
	this.host = hostname;
}

// TIMER TASK
public void run() {
	try {
		checkHost();
	} catch (Exception NoSuchHost){
		System.out.println("Exception: No such host " + host + "... terminating program.");
		System.exit(1);
	}
}

// Attempts to connect to host
private void checkHost() throws Exception {
	String temp;
	if (listener.llrpDevices.containsKey(host)){
		temp = listener.llrpDevices.get(host);
		System.out.println();
		System.out.println("Attempting connection with " + host + " at " + temp);
		monitor = new RFIDMonitor(temp);
		
	} else
		{
			throw NoSuchHost;
		}
	}
}
