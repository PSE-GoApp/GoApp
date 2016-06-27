
package kit.edu.pse.goapp.server.datamodels;

import java.util.ArrayList;
import java.util.List;

public class User {
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
