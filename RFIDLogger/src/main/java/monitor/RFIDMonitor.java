package monitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.FileAppender;
import org.llrp.ltk.exceptions.InvalidLLRPMessageException;
import org.llrp.ltk.generated.enumerations.StatusCode;
import org.llrp.ltk.generated.messages.ADD_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.messages.DELETE_ROSPEC;
import org.llrp.ltk.generated.messages.DISABLE_ROSPEC;
import org.llrp.ltk.generated.messages.ENABLE_ROSPEC;
import org.llrp.ltk.generated.messages.GET_READER_CAPABILITIES;
import org.llrp.ltk.generated.messages.GET_READER_CONFIG;
import org.llrp.ltk.generated.messages.GET_ROSPECS;
import org.llrp.ltk.generated.messages.RO_ACCESS_REPORT;
import org.llrp.ltk.generated.messages.START_ROSPEC;
import org.llrp.ltk.net.LLRPConnectionAttemptFailedException;
import org.llrp.ltk.net.LLRPConnector;
import org.llrp.ltk.net.LLRPEndpoint;
import org.llrp.ltk.types.LLRPMessage;
import org.llrp.ltk.types.UnsignedInteger;
import org.llrp.ltk.types.UnsignedShort;
import org.llrp.ltk.util.Util;

// Joseph Sullivan
// Research Assistant
// Smart Wet Lab
//------------------------------------------------------------------------------------
// RFIDMonitor is a class which is characterized by an RFIDReader and a logger object
// This class monitors and configures the RFIDReader, receives messages from the reader 
// and passes messages to it.These messages are logged in a text file. Whenever the Monitor
// receives a RO_ACCESS_REPORT it passes that message to the RFIDReader's inventory

public class RFIDMonitor implements LLRPEndpoint {

	// Fields
	private  RFIDReader reader;
	private static Logger logger;	

	public RFIDMonitor(String IP){
		//Attempt to establish connection with the RFID reader
		try {
			
			reader = GetReader(IP);
			System.out.println("Connection acquired");
			this.getReaderConfiguration();
			this.getReaderCapabilities();
			
		} catch (LLRPConnectionAttemptFailedException e1) {
			
			System.out.println("Exception: Connection Failed");
			e1.printStackTrace();
			
		}
		Date date = new Date();
		logger = logger.getRootLogger();
		logger.debug("Connection Established with " + reader.reader_IP + " at " + Long.toString(date.getTime()));
		
	}

	public void addROSpec(){
		LLRPMessage response;
		try{
			//Delete ROSpec
			logger.info("Delete ROSPEC message ...");
			DELETE_ROSPEC del = new DELETE_ROSPEC();
			del.setROSpecID(new UnsignedInteger(0));
			response =  reader.connection.transact(del, 10000);
			
			
			logger.info("Loading ADD_ROSPEC message from file ADD_ROSPEC.xml ...");		
		
				//InputStream rospec = getClass().getResourceAsStream("ADD_ROSPEC");
				//BufferedReader specreader = new BufferedReader(new InputStreamReader(rospec));
				//try {
				//	File add_rospec = new File("/temp/rfid/ADD_ROSPEC");
				//} catch (Exception e){
			LLRPMessage addRospec = Util.loadXMLLLRPMessage(new File("/rfid/ADD_ROSPEC"));	
				
			
			// send message to LLRP reader and wait for response
			logger.info("Sending ADD_ROSPEC message ...");
			response =  reader.connection.transact(addRospec, 10000);
			
			// check whether ROSpec addition was successful
			StatusCode status = ((ADD_ROSPEC_RESPONSE)response).getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("Addition of ROSPEC was successful");
			}
			else {
			// Terminate program
				logger.info(response.toXMLString());
				logger.info("Addition of ROSPEC was unsuccessful, exiting");
				System.exit(1);
			}
			// Configure the reader
			logger.info("Enable ROSPEC ...");
			ENABLE_ROSPEC enable = new ENABLE_ROSPEC();
			enable.setROSpecID(new UnsignedInteger(3));
			response = reader.connection.transact(enable, 10000);
			logger.info(response);
			
			logger.info("Start ROSPEC ...");	
			START_ROSPEC start = new START_ROSPEC();
			start.setROSpecID(new UnsignedInteger(3));
			response = reader.connection.transact(start, 10000);
			logger.info(response);
			System.out.println("RECORDING...");
			System.out.println();
	
		} catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void sendMessage(String messageName){
		if (messageName.equals("n")){
			// do nothing
		}
		else{
			LLRPMessage response;
			try{
				LLRPMessage request = Util.loadXMLLLRPMessage(new File("/rfid/" + messageName));
				response = reader.connection.transact(request,10000);
			} catch (Exception e){
				e.printStackTrace(); System.exit(1);
			}
		}
	}

	public String getIp(){
		return reader.reader_IP;
	}
	
	public void disconnect(){
		LLRPMessage response;
		try{		
			DISABLE_ROSPEC disable = new DISABLE_ROSPEC();
			disable.setROSpecID(new UnsignedInteger(0));
			response =  reader.connection.transact(disable, 10000);
			this.reader.connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void reconnect(){
		reader.connection.reconnect();
	}
	
	//Gets reader capabilities and writes them to a file
	public void getReaderConfiguration(){
		LLRPMessage response;
		try{
			System.out.println("Attempting to get reader configuration...");
			LLRPMessage request = Util.loadXMLLLRPMessage(new File("/rfid/GET_READER_CONFIG"));
			response = reader.connection.transact(request,10000);
			
			String filename = "/tmp/readerconfig";
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(response.toXMLString());
			bw.close();
			
			System.out.println("Success, configuration saved to \t " + filename);
			
		} catch (Exception e){
			e.printStackTrace(); System.exit(1);
		}
	}
	
	public void setReaderConfiguration(String filename){
		System.out.println("Attempting to set reader configuration");
		LLRPMessage response;
		try {
			LLRPMessage request = Util.loadXMLLLRPMessage(new File ("/rfid/" + filename));
			response = reader.connection.transact(request);
			System.out.print(response.toString());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void getReaderCapabilities(){
		System.out.println("Attempting to get reader capabilities");
		LLRPMessage response;
		try {
			LLRPMessage request = Util.loadXMLLLRPMessage(new File("/rfid/GET_READER_CAPABILITIES"));
			response = reader.connection.transact(request,10000);
			
			String filename = "/tmp/readercapabilities";
			File file = new File(filename);
			if (!file.exists()){
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(response.toXMLString());
			bw.close();
				
			System.out.println("Success, capabilities saved to \t \t " + filename);
			
		}catch (Exception e){
				e.printStackTrace(); System.exit(1);
			}
	}
	
	
	public void printLog(){
		reader.inventory.printLog();
	}

	public RFIDReader GetReader(String IP) throws LLRPConnectionAttemptFailedException{
		//TODO: Re-comment method. Should be straight forward
		
		LLRPConnector connection;
		connection = new LLRPConnector(this,IP);
		reader = new RFIDReader(connection,IP);
		
		//Try to connect. If Connection fails, increment IP and recall method
		try{
	
	
			((LLRPConnector) connection).connect();
		} catch (LLRPConnectionAttemptFailedException e1){
			throw e1;
			} 
		RFIDReader reader = new RFIDReader(connection,IP);
				
		return reader;
	}

	@Override
	public void messageReceived(LLRPMessage arg0) {
		if(arg0.getClass()== RO_ACCESS_REPORT.class)
		{
			Date date = new Date();
			
			try {
				//System.out.println(arg0.toXMLString());
				reader.inventory.update(arg0.encodeXML(), date.getTime());
			} catch (InvalidLLRPMessageException e) {
				e.printStackTrace();
			}
		}
		else {
		}
		
		
	}

	@Override
	public void errorOccured(String arg0) {
		logger.debug("Error Occured: " + arg0);
	}
}
