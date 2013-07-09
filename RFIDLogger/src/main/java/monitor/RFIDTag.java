package monitor;

public class RFIDTag {
public String antennaID;
public long timeLastSeen;

public RFIDTag(String antenna, long timestamp){
	this.antennaID = antenna;
	this.timeLastSeen=timestamp;
}
}
