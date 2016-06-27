package kit.edu.pse.goapp.server.datamodels;

import java.util.ArrayList;
import java.util.List;

public abstract class Meeting {

	public static double RADIUS = 200.00;

	private int meetingId;
	private String name;
	private GPS place;
	private long timespamp;
	private int duration;
	private Participant creator;
	private List<Participant> participants;

	public Meeting(int meetingId, String name, GPS place, long timestamp, int duration, Participant creator) {
		this.meetingId = meetingId;
		this.name = name;
		this.place = place;
		this.timespamp = timestamp;
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

	public long getTimespamp() {
		return timespamp;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public int getMeetingId() {
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

}
