package edu.kit.pse.client.goapp.datamodels;

/*
 * @version 1.0
 * @author PSE group
 */
public class GroupMember {

    private int groupId;
    private int userId;
    private boolean isAdmin;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
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
        GroupMember g = (GroupMember) obj;
        if (g.getGroupId() == groupId && g.getUserId() == userId && g.isAdmin() == isAdmin) {
            return true;
        }
        return false;
    }

}

