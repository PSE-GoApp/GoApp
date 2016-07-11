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
		@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		// Class name is Employ & have lastname
		MeetingCenter c = (MeetingCenter) obj;
		 if(c.getPlace().equals(place) && c.getParticipants().equals(participants)) {
			return true;
		}
		return false;
	}

}
