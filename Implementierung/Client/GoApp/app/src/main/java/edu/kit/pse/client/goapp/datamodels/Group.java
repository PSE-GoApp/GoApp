
package edu.kit.pse.client.goapp.datamodels;


import java.util.*;

public class Group {
	private int groupId;
	private String name;
	private List<User> admins;
	private List<User> members;

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

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public void setAdmins(List<User> admins) {
		this.admins = admins;
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
		Group g = (Group) obj;
		 if(g.getAdmins().equals(admins)&&g.getGroupMembers().equals(members)&&g.getId()==groupId&&g.getName().equals(name)) {
			return true;
		}
		return false;
	}
}