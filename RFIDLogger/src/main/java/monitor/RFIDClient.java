package monitor;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import javax.jmdns.JmDNS;

import org.apache.log4j.PropertyConfigurator;

//import org.apache.log4j.PropertyConfigurator;

public class RFIDClient {
	
	private static String IP;
	private static String type = "_llrp._tcp.local.";
	private static String hostname;
	private static LLRPListener listener;
	private static Scanner console;
	private static Connector connector;
	private static RFIDMonitor monitor;
	private static String xmlmessage;

	
	public static void main(String[] args) {
		
		hostname = args[0];
		
		PropertyConfigurator.configure("/rfid/logconfig.cfg");
		console = new Scanner(System.in);
		JmDNS jmdns;
		listener = new LLRPListener();
		System.out.println("Enter \"STOP\" to end data collection...");
		
		try {
			// Set up listener and connector to establish connection to reader
			System.out.println("Searching for devices....");
			jmdns = JmDNS.create();
			jmdns.addServiceListener(type, listener);
			Timer delay = new Timer();
			connector = new Connector(listener,hostname);
			delay.schedule(connector, (long)1000);
			monitor = connector.monitor;
			if (args.length > 1){
				monitor.setReaderConfiguration(args[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//char c = console.next().charAt(0);
		String cmd = console.nextLine();
		
		if (cmd.equals("STOP")|| cmd.equals("stop")){
			//End connection
			connector.monitor.printLog();
			connector.monitor.disconnect();
			System.exit(1);
		}

	}
}

