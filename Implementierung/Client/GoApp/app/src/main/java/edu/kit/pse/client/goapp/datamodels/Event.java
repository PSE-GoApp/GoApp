package edu.kit.pse.client.goapp.datamodels;

public class Event extends Meeting{

	public Event(int id, String name, GPS place, long timestamp, int duration, Participant creator) {
		super(id, name, place, timestamp, duration, creator);
		
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		// Class name is Employ & have lastname		
			Meeting m = (Meeting) obj;
			if (m.getMeetingId() == this.getMeetingId() && m.getPlace().equals(this.getPlace()) && m.getCreator().equals(this.getCreator())
					&& m.getDuration() == this.getDuration() && m.getName().equals(this.getName())
					&& m.getParticipants().equals(this.getParticipants()) && equals(m.getTimestamp() == this.getTimestamp())) {
				return true;
			}	

		return false;
	}
	
}
