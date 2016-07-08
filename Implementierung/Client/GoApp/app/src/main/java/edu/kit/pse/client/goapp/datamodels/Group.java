
package edu.kit.pse.client.goapp.datamodels;


import java.util.*;

public class Group {
	private int groupId;
	private String name;
	private List<User> admins;
	private List<User> members;

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public Group(int groupId, String name) {
		this.groupId = groupId;
		this.name = name;
		admins = new ArrayList<User>();
		members = new ArrayList<User>();
	}

	public void addAdmin(User user) {
		admins.add(user);
	}

	public void addGroupMember(User user) {
		members.add(user);
	}

	public List<User> getAdmins() {
		return admins;
	}

	public List<User> getGroupMembers() {
		return members;
	}

	public int getId() {
		return groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}