package kit.edu.pse.goapp.server.datamodels;

public class Tour extends Meeting {
	public static double MAX_CENTER_DISTANCE = 10.00;
	private MeetingCenter center;

	public Tour(int id, String name, GPS place, long timestamp, int duration, Participant creator) {
		super(id, name, place, timestamp, duration, creator);
		this.center = new MeetingCenter(place);

	}

	public MeetingCenter getCenter() {
		return center;
	}

}
