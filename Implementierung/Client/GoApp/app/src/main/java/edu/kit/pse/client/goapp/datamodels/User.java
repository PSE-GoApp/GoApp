
package edu.kit.pse.client.goapp.datamodels;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int userId;
	private String name;

	private GPS gps;
	private boolean notificationEnabled;

	private List<Meeting> meetings;
	private List<Group> groups;

	public User(int userId, String name) {
		this.userId = userId;
		this.name = name;
		meetings = new ArrayList<Meeting>();
		groups = new ArrayList<Group>();
	}

	public int getId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public void setGPS(GPS gps) {
		this.gps = gps;
	}

	public void addGroup(Group group) {
		groups.add(group);
	}

	public void addMeeting(Meeting meeting) {
		meetings.add(meeting);
	}

	public List<Meeting> getAllMeetigns() {
		return meetings;
	}

	public List<Group> getAllGroups() {
		return groups;
	}

	public boolean isNotificationEnabled() {
		return notificationEnabled;
	}

	public void setNotificationEnabled(boolean notificationEnabled) {
		this.notificationEnabled = notificationEnabled;
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
		User u = (User) obj;
		 if((u.getId() == userId)  &&( u.getName().equals(name))) {
			return true;
		}

		return false;
	}

}
