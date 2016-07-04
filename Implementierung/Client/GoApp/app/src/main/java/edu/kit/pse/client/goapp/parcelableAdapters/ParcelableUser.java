package edu.kit.pse.client.goapp.parcelableAdapters;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.User;

/**
 * Created by e6420 on 4.7.2016 г..
 */
public class ParcelableUser implements Parcelable {

    private User user;

    private int userId;
    private String name;
    private GPS gps;
    private List<Meeting> meetings;
    private List<Group> groups;

    public ParcelableUser(User user1) {user = user1; }

    public User getUser() {return user; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user.getId());
        dest.writeString(user.getName());
        dest.writeList(user.getAllMeetigns());
        dest.writeList(user.getAllGroups());
    }

    private ParcelableUser(Parcel in) {
        user.setUserId(in.readInt());
        user.setName(in.readString());
        in.readList(meetings, Meeting.class.getClassLoader());
        user.setMeetings(meetings);
        in.readList(groups, Group.class.getClassLoader());
        user.setGroups(groups);
    }

    public static final Parcelable.Creator<ParcelableUser> CREATOR
            = new Parcelable.Creator<ParcelableUser>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public ParcelableUser createFromParcel(Parcel in) {
            return new ParcelableUser(in);
        }

        @Override
        public ParcelableUser[] newArray(int size) {
            return new ParcelableUser[size];
        }
    };

}
