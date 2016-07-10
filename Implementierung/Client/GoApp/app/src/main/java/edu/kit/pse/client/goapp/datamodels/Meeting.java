package edu.kit.pse.client.goapp.datamodels;

import java.util.ArrayList;
import java.util.List;

public class Meeting {

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

		// Todo: ???
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

	// set

	public void setName(String name) { this.name = name; }

	public void setPlace(GPS place) { this.place = place; }

	public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

	public void setDuration(int duration) { this.duration = duration; }

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}


}
