package monitor;


import org.llrp.ltk.net.LLRPConnector;

public class RFIDReader {
//Fields
	public LLRPConnector connection;
	public String reader_IP;
	public Inventory inventory;
	
	//Constructor
	public RFIDReader(LLRPConnector c, String ip){
		connection = c;
		reader_IP = ip;
		inventory = new Inventory();
		inventory.on();
	}
	
}

