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
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;

public class Inventory {
	
	private Hashtable<String,RFIDTag> inventory;
	private String dataLog;
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
	
	public synchronized void update(Document report, long timestamp){
		// Update the inventory
		String EPC = getTagData(report.getRootElement(),"EPC_96");
		String antennaID = "A" + getTagData(report.getRootElement(),"AntennaID");
		if (inventory.containsKey(EPC)){
			// First test for time out
			if (timestamp - inventory.get(EPC).timeLastSeen <= timeout){
				inventory.get(EPC).timeLastSeen=timestamp;
			}
			if (!antennaID.equals(inventory.get(EPC).antennaID)){
			// Tag exited one field and entered another
				dataLog = dataLog + Long.toString(timestamp) + "," + inventory.get(EPC).antennaID + "," + "EXIT" + "," + EPC + "\n";
				System.out.println(Long.toString(timestamp) + "," + inventory.get(EPC).antennaID + "," + "EXIT" + "," + EPC);
				dataLog = dataLog + Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC + "\n";
				System.out.println(Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC);
				inventory.get(EPC).antennaID = antennaID;
			}
		} else {
			// Add tag to inventory
			dataLog = dataLog + Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC + "\n";
			System.out.println(Long.toString(timestamp) + "," + antennaID + "," + "ENTER" + "," + EPC);
			inventory.put(EPC,new RFIDTag(antennaID, timestamp));
		}	
	}

	
	public synchronized void checkList(){
		long timestamp = new Date().getTime();
		Enumeration<String> e = inventory.keys();
 		while (e.hasMoreElements()== true){
			String temp = e.nextElement();
			if (timestamp - inventory.get(temp).timeLastSeen > timeout){
				dataLog = dataLog + Long.toString(timestamp) + "," + inventory.get(temp).antennaID + "," + "EXIT" + "," + temp + "\n";
				System.out.println(Long.toString(timestamp) + "," + inventory.get(temp).antennaID + "," + "EXIT" + "," + temp);
				inventory.remove(temp);
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
		dataLog = new String("All RFID tags shall be assimilated. \n");
	}
	
	public void printLog(){
		try {
			Date date = new Date();
			String filename = "/home/joey/SmartWetLab/RFIDLogger/dump/InventoryLog_" + Integer.toString(date.getMonth() + 1)+ "_" + Integer.toString(date.getDate()) + "_" + Integer.toString(date.getHours()) + ":" + Integer.toString(date.getMinutes()) + ".txt";
			File file = new File(filename);
 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(dataLog);
			bw.close();
 
			System.out.println("DONE \t" + filename);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
