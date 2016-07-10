package edu.kit.pse.client.goapp.datamodels;

import java.util.ArrayList;
import java.util.List;


public class MeetingCenter {
	private List<Participant> participants;
	private GPS place;
	
	public MeetingCenter(GPS place) {
		this.place = place;
		this.participants = new ArrayList<Participant>();
	}
	
	public void addParticipantsToCenter(Participant participant) {
		participants.add(participant);
	}
	
	public MeetingCenter calculateCenter() {
		//TODò
		return null;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public GPS getPlace() {
		return place;
	}
	
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	

}
