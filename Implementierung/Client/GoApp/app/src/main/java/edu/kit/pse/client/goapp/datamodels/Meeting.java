package edu.kit.pse.client.goapp.datamodels;

import java.util.ArrayList;
import java.util.List;

public abstract class Meeting {

	public static double RADIUS = 200.00;

	private int meetingId;
	private String name;
	private GPS place;
	private long timestamp;
	private int duration;
	private Participant creator;
	private List<Participant> participants;

	public Meeting(int meetingId, String name, GPS place, long timestamp, int duration, Participant creator) {
		this.meetingId = meetingId;
		this.name = name;
		this.place = place;
		this.timestamp = timestamp;
		this.duration = duration;
		this.creator = creator;
		this.participants = new ArrayList<Participant>();

	}

	public void addParticipant(Participant participant) {
		participants.add(participant);
	}

	public GPS getPlace() {
		return place;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public int getId() {
		return meetingId;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}

	public Participant getCreator() {
		return creator;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
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
		 if(m.getId() == meetingId && m.getPlace().equals(place) && m.getCreator().equals(creator)  && m.getDuration() == duration && m.getName().equals(name) && m.getParticipants().equals(participants)
				 &&equals(m.getTimestamp() == timestamp) ) {
			return true;
		}
		return false;
	}
}
