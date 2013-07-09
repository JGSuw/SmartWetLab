package monitor;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import javax.jmdns.JmDNS;

import org.apache.log4j.PropertyConfigurator;

public class RFIDLog {
	
	private static String IP;
	private static String type = "_llrp._tcp.local.";
	private static String hostname;
	private static LLRPListener listener;
	private static Scanner console;
	private static Connector connector;

	
	public static void main(String[] args) {
		hostname = args[0];
		PropertyConfigurator.configure("/home/joey/SmartWetLab/RFIDLogger/config/logconfig.cfg");
		console = new Scanner(System.in);
		JmDNS jmdns;
		listener = new LLRPListener();
		System.out.println("Enter \"STOP\" to end data collection...");
		try {
			System.out.println("Searching for devices....");
			jmdns = JmDNS.create();
			jmdns.addServiceListener(type, listener);
			Timer delay = new Timer();
			connector = new Connector(listener,hostname);
			delay.schedule(connector, (long)1000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//char c = console.next().charAt(0);
		String command = console.nextLine();
		
		if (command.equals("STOP")|| command.equals("stop")){
			//End connection
			connector.monitor.printLog();
			connector.monitor.disconnect();
			System.exit(1);
		}

	}
}

