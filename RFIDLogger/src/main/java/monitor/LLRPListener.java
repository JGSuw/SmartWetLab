package monitor;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

public class LLRPListener implements ServiceListener {
	public Hashtable<String,String> llrpDevices;
	public Enumeration<String> keys;
	
 	@Override
	public void serviceAdded(ServiceEvent event) {
		// TODO Auto-generated method stub
		}

	@Override
	public void serviceRemoved(ServiceEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Service removed : " + event.getName() + "." + event.getType());
	}

	@Override
	public void serviceResolved(ServiceEvent event) {
		// TODO Auto-generated method stub
		llrpDevices.put(event.getInfo().getName(), event.getInfo().getHostAddress());
		System.out.println("Service resolved: \t" + "Host Name: " + event.getInfo().getName() + "\t" + "IP Address: " + event.getInfo().getHostAddress());
		
	}
	
	public LLRPListener(){
		llrpDevices = new Hashtable<String,String>(10);
	}
	public Enumeration<String> getHostNames(){
		return llrpDevices.keys();
	}
}
