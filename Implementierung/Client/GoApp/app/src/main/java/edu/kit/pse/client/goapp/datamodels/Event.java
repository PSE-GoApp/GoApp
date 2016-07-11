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
			if (m.getMeetingId() == meetingId && m.getPlace().equals(place) && m.getCreator().equals(creator)
					&& m.getDuration() == duration && m.getName().equals(name)
					&& m.getParticipants().equals(participants) && equals(m.getTimespamp() == timespamp)) {
				return true;
			}	

		return false;
	}
	
}
