package edu.kit.pse.client.goapp.datamodels;
public class Participant {
	private int participantId;
	private User user;
	private MeetingConfirmation confirmation;
	public Participant(int participantId, User user, MeetingConfirmation confirmation) {
		this.participantId = participantId;
		this.user = user;
		this.confirmation = confirmation;
	}
	public int getParticipantId() {
		return participantId;
	}
	public User getUser() {
		return user;
	}
	public MeetingConfirmation getConfirmation() {
		return confirmation;
	}
	
	
	

}
