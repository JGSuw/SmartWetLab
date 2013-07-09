package monitor;

import java.io.File;
import java.io.IOException;
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
import org.llrp.ltk.generated.messages.GET_ROSPECS;
import org.llrp.ltk.generated.messages.RO_ACCESS_REPORT;
import org.llrp.ltk.generated.messages.START_ROSPEC;
import org.llrp.ltk.net.LLRPConnectionAttemptFailedException;
import org.llrp.ltk.net.LLRPConnector;
import org.llrp.ltk.net.LLRPEndpoint;
import org.llrp.ltk.types.LLRPMessage;
import org.llrp.ltk.types.UnsignedInteger;
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

	private void addROSpec(){
		LLRPMessage response;
		try{
			//Delete ROSpec
			logger.info("Delete ROSPEC message ...");
			DELETE_ROSPEC del = new DELETE_ROSPEC();
			del.setROSpecID(new UnsignedInteger(0));
			response =  reader.connection.transact(del, 10000);
			
			// load ADD_ROSPEC message12
			logger.info("Loading ADD_ROSPEC message from file ADD_ROSPEC.xml ...");		
			LLRPMessage addRospec = Util.loadXMLLLRPMessage(new File("/home/joey/SmartWetLab/RFIDLogger/config/ADD_ROSPEC3"));
	
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
	
		} catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
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
	public void errorOccured(String arg0) {
		logger.debug("Error Occured: " + arg0);
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
	
	public RFIDMonitor(String IP){
		//Attempt to establish connection with the RFID reader
		try {
			reader = GetReader(IP);
		} catch (LLRPConnectionAttemptFailedException e1) {
			System.out.println("Exception: Connection Failed");
			e1.printStackTrace();
		}		
		Date date = new Date();
		logger = logger.getRootLogger();
		logger.debug("Connection Established with " + reader.reader_IP + " at " + Long.toString(date.getTime()));
		System.out.println("RECORDING...");
		addROSpec();
		
		
	}
	
	public void printLog(){
		reader.inventory.printLog();
	}
}
