package kit.edu.pse.goapp.server.datamodels;

public class Event extends Meeting{

	public Event(int id, String name, GPS place, long timestamp, int duration, Participant creator) {
		super(id, name, place, timestamp, duration, creator);
		
	}

	
}
