package edu.kit.pse.client.goapp.datamodels;
public class Participant {
	private int participantId;
	private int meetingId;
	private User user;
	private MeetingConfirmation confirmation;

	public Participant(int participantId, int meetingId, User user, MeetingConfirmation confirmation) {
		this.participantId = participantId;
		this.meetingId = meetingId;
		this.user = user;
		this.confirmation = confirmation;
	}

	public int getParticipantId() {
		return participantId;
	}

	public int getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(int meetingId) {
		this.meetingId = meetingId;
	}

	public User getUser() {
		return user;
	}

	public MeetingConfirmation getConfirmation() {
		return confirmation;
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
		Participant p = (Participant) obj;
		if ((p.getMeetingId() == meetingId) && (p.getParticipantId() == participantId) && (p.getUser().equals(user))
				&& (p.getConfirmation() == confirmation)) {
			return true;
		}

		return false;
	}
}
