package edu.kit.pse.client.goapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.datamodels.User;

/**
 * Created by e6420 on 30.6.2016 г..
 */
public class ParcelableGroup implements Parcelable{

    private Group group;
    private List<User> admins;
    private List<User> members;

    public ParcelableGroup(Group group1) {
        group = group1;
    }

    public Group getGroup(){
        return group;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(group.getId());
        dest.writeString(group.getName());
        dest.writeList(group.getAdmins());
        dest.writeList(group.getGroupMembers());
    }

    private ParcelableGroup(Parcel in) {
        group.setGroupId(in.readInt());
        group.setName(in.readString());
        in.readList(admins, User.class.getClassLoader());
        group.setAdmins(admins);
        in.readList(members, User.class.getClassLoader());
        group.setMembers(members);
    }

    public static final Parcelable.Creator<ParcelableGroup> CREATOR
            = new Parcelable.Creator<ParcelableGroup>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public ParcelableGroup createFromParcel(Parcel in) {
            return new ParcelableGroup(in);
        }

        @Override
        public ParcelableGroup[] newArray(int size) {
            return new ParcelableGroup[size];
        }
    };

}
