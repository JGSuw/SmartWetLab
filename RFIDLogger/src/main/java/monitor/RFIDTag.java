package monitor;

public class RFIDTag {

public long A1timeLastSeen;
public long A2timeLastSeen;
public long A3timeLastSeen;
public long A4timeLastSeen;
public String location;



public RFIDTag(String antenna, long timestamp){
	
	this.location = antenna;
	switch (location){
	case "A1":
		this.A1timeLastSeen = timestamp;
		break;
	case "A2":
		this.A2timeLastSeen = timestamp;
		break;
	case "A3":
		this.A3timeLastSeen = timestamp;
		break;
	case "A4":
		this.A4timeLastSeen = timestamp;
		break;
	}
}

}
