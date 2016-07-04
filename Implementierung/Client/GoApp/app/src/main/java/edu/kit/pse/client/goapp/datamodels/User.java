
package edu.kit.pse.client.goapp.datamodels;

import java.util.ArrayList;
import java.util.List;

public class User {
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGps(GPS gps) {
		this.gps = gps;
	}

	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	private int userId;
	private String name;
	
	private GPS gps;
	
	private List<Meeting> meetings;
	private List<Group> groups;
	
	public User(int userId, String name)
	{
		this.userId = userId;
		this.name = name;
		meetings = new ArrayList<Meeting>();
		groups = new ArrayList<Group>();
	}
	public int getId()
	{
		return userId;
	}
	
	public String getName()
	{
		return name;
	}
	
    public void setGPS(GPS gps)
    {
    	this.gps = gps;
    }
    
    public void addGroup(Group group)
    {
    	groups.add(group);
    }
    
    public void addMeeting(Meeting meeting)
    {
    	meetings.add(meeting);
    }
    
    public  List<Meeting> getAllMeetigns()
    {
    	return meetings;
    }
    public  List<Group> getAllGroups()
    {
    	return groups;
    }
    
    
}
