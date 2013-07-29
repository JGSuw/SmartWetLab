//Joseph Sullivan
//Research Assistant
//Smart Wet Lab
//
//This class contains the inventory of RFID tags currently within range
//of a given reader's antenna array. Whenever the RFIDMonitor receives
//a message from the RFIDReader it sends that message to the inventory
//which extracts the electronic product code of the tag identified and
//adds that tag to the inventory. 
//
//Additionally,
//the Inventory class checks to make sure all tags currently "in" inventory
//have been observed recently by the reader. If a tag has not been observed
//at some point during the previous five seconds it is removed from the inventory.
package monitor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;
import java.util.Timer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;

public class Inventory {
	
	private Hashtable<String,RFIDTag> inventory;
	private String eventLog;
	private String inventoryLog;
	private long timeout = 1000;
	private Timer timer;
	private CheckInventory checker;

	private String getTagData(Element root, String name){
		// Isolate the EPC code using a very painful recursive method.
		Element tagReportData = root.getChild("TagReportData",root.getNamespace());
		Element dataElement = tagReportData.getChild(name,root.getNamespace());
		String data = dataElement.getContent(0).getValue();
		return data.toString();
			
	}
	
	// New version lists both antenna fields if tag is seen by both
	public synchronized void update(Document report, long timestamp){
		
		// Update the inventory
		String EPC = getTagData(report.getRootElement(),"EPC_96");
		String antennaID = "A" + getTagData(report.getRootElement(),"AntennaID");
		if (inventory.containsKey(EPC)){
			
			//Update correct timeLastSeen field
			switch(antennaID){
			case "A1":
				inventory.get(EPC).A1timeLastSeen = timestamp;
				break;
			case "A2":
				inventory.get(EPC).A2timeLastSeen = timestamp;
				break;
			case "A3":
				inventory.get(EPC).A3timeLastSeen = timestamp;
				break;
			case "A4":
				inventory.get(EPC).A4timeLastSeen = timestamp;
				break;
			}
			
			//Update location
			if (!inventory.get(EPC).location.contains(antennaID)){
				inventory.get(EPC).location += antennaID;
				eventLog = eventLog + Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC + "\n";
				System.out.println(Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC);
				
			}		
			
			// OLD LOGIC BLOCK
			if (!antennaID.equals(inventory.get(EPC).location)){
			// Tag exited one field and entered another
				
				// Taking out code
				//dataLog = dataLog + Long.toString(timestamp) + "," + inventory.get(EPC).antennaID + "," + "EXIT" + "," + EPC + "\n";
				//System.out.println(Long.toString(timestamp) + "," + inventory.get(EPC).antennaID + "," + "EXIT" + "," + EPC);
				//dataLog = dataLog + Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC + "\n";
				//System.out.println(Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC);
				//inventory.get(EPC).antennaID = antennaID;
				
				
			}
		} else {
			// Add tag to inventory
			eventLog = eventLog + Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC + "\n";
			inventoryLog = inventoryLog + Long.toString(timestamp) + "," + "1" + "," + EPC +"\n";
			System.out.println(Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC);
			inventory.put(EPC,new RFIDTag(antennaID, timestamp));
		}	
	}

	
	public synchronized void checkList(){
		long timestamp = new Date().getTime();
		Enumeration<String> e = inventory.keys();
 		while (e.hasMoreElements()== true){
			String EPC = e.nextElement();

			// Check for currency in each antenna field
			if (inventory.get(EPC).location.contains("A1")){
				if (timestamp - inventory.get(EPC).A1timeLastSeen >= timeout){
				inventory.get(EPC).location = inventory.get(EPC).location.replace("A1", "");
				eventLog = eventLog + Long.toString(timestamp) + "," + "A1" + "," + "EXIT" + "," + EPC + "\n";
				System.out.println(Long.toString(timestamp) + "," + "A1" + "," + "EXIT" + "," + EPC);
				}
			}
			if (inventory.get(EPC).location.contains("A2")){
				if (timestamp - inventory.get(EPC).A2timeLastSeen >= timeout){
				inventory.get(EPC).location = inventory.get(EPC).location.replace("A2", "");
				eventLog = eventLog + Long.toString(timestamp) + "," + "A2" + "," + "EXIT" + "," + EPC + "\n";
				System.out.println(Long.toString(timestamp) + "," + "A2" + "," + "EXIT" + "," + EPC);
				}
			}
			if (inventory.get(EPC).location.contains("A3")){
				if (timestamp - inventory.get(EPC).A3timeLastSeen >= timeout){
				inventory.get(EPC).location = inventory.get(EPC).location.replace("A3", "");
				eventLog = eventLog + Long.toString(timestamp) + "," + "A3" + "," + "EXIT" + "," + EPC + "\n";
				System.out.println(Long.toString(timestamp) + "," + "A3" + "," + "EXIT" + "," + EPC);
				}
			}
			if (inventory.get(EPC).location.contains("A4")){
				if (timestamp - inventory.get(EPC).A4timeLastSeen >= timeout){
				inventory.get(EPC).location = inventory.get(EPC).location.replace("A4", "");
				eventLog = eventLog + Long.toString(timestamp) + "," + "A4" + "," + "EXIT" + "," + EPC + "\n";
				System.out.println(Long.toString(timestamp) + "," + "A4" + "," + "EXIT" + "," + EPC);
				}
			}
			
			if (inventory.get(EPC).location.isEmpty()){
				//System.out.println("tag" + " " + EPC + " no longer visible");
				//timestamp,epc,1/0 one represents in a field and zero represents not in a field
				inventoryLog = inventoryLog + Long.toString(timestamp) + "," + "0" + "," + EPC + "\n";
				inventory.remove(EPC);
			}
 		}
	}
	
	public void on(){
		timer.schedule(checker, (long)(10), (long)(10));
	}
	
	public Inventory(){
		// Constructor
		inventory = new Hashtable<String, RFIDTag>(128);
		this.checker = new CheckInventory(this);
		this.timer = new Timer();
		eventLog = new String("Your RFID tags will be assimilated. \n");
		inventoryLog = new String("Your RFID tags will be assimilated. \n");
	}
	
	public void printLog(){
		try {
			Date date = new Date();
			String filename = "/tmp/eventlog_" + Integer.toString(date.getMonth() + 1)+ "_" + Integer.toString(date.getDate()) + "_" + Integer.toString(date.getHours()) + ":" + Integer.toString(date.getMinutes()) + ".txt";
			File file = new File(filename);
 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(eventLog);
			bw.close();
			
			String filename2 = "/tmp/inventoryLog_" + Integer.toString(date.getMonth() + 1) + "_" + Integer.toString(date.getDate()) + "_" + Integer.toString(date.getHours()) + ":" + Integer.toString(date.getMinutes()) + ".txt";
			File file2 = new File(filename2);
			
			if(!file2.exists()){
				file2.createNewFile();
			}
			
			fw = new FileWriter(file2.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(inventoryLog);
			bw.close();
			
			//Testing printing using CUPS and thermal printer, just for fun
			//FileInputStream octowriter = new FileInputStream(file);
			//try {
			//	CupsClient client = new CupsClient();
			//	CupsPrinter def = client.getDefaultPrinter();
			//	PrintRequestResult response;
			//	response = def.print(new PrintJob.Builder(octowriter).build());
			//	if (response.isSuccessfulResult()){
			//		System.out.println("Hurray! Printed!");
			//	} else {
			//		System.out.println("Boo! Failure!");
			//	}
			//} catch (Exception e) {
			//	// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
			System.out.println();
			System.out.println("DONE. Log file located at \t" + filename);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
