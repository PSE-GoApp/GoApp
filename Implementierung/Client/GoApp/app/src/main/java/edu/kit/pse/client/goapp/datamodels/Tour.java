package edu.kit.pse.client.goapp.datamodels;

public class Tour extends Meeting {
	public static double MAX_CENTER_DISTANCE = 30.00;
	private MeetingCenter center;

	public Tour(int id, String name, GPS place, long timestamp, int duration, Participant creator) {
		super(id, name, place, timestamp, duration, creator);
		this.center = new MeetingCenter(place);

	}

	public MeetingCenter getCenter() {
		return center;
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
		Tour u = (Tour) obj;
		if (u.getCenter().equals(center)) {
			Meeting m = (Meeting) obj;
			if (m.getMeetingId() == getMeetingId() && m.getPlace().equals(getPlace()) && m.getCreator().equals(getCreator())
					&& m.getDuration() == getDuration() && m.getName().equals(getName())
					&& m.getParticipants().equals(getParticipants()) && m.getTimestamp() == getTimestamp() ) {
				return true;
			}
		}

		return false;
	}
}
