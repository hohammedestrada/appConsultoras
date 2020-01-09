package biz.belcorp.consultoras.common.model.facebook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andres.escobar on 25/04/2017.
 */

public class FacebookProfileModel implements Parcelable {

    private String id;
    private String name;
    private String email;
    private String image;
    private String firstName;
    private String lastName;
    private String linkProfile;
    private String birthday;
    private String gender;
    private String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLinkProfile() {
        return linkProfile;
    }

    public void setLinkProfile(String linkProfile) {
        this.linkProfile = linkProfile;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.image);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.linkProfile);
        dest.writeString(this.birthday);
        dest.writeString(this.gender);
        dest.writeString(this.location);
    }

    public FacebookProfileModel() {
    }

    protected FacebookProfileModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.image = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.linkProfile = in.readString();
        this.birthday = in.readString();
        this.gender = in.readString();
        this.location = in.readString();
    }

    public static final Creator<FacebookProfileModel> CREATOR = new Creator<FacebookProfileModel>() {
        @Override
        public FacebookProfileModel createFromParcel(Parcel source) {
            return new FacebookProfileModel(source);
        }

        @Override
        public FacebookProfileModel[] newArray(int size) {
            return new FacebookProfileModel[size];
        }
    };

}
