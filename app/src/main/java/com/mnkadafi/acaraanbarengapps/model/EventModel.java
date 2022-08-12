package com.mnkadafi.acaraanbarengapps.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class EventModel implements Parcelable {

    private String idEvent;
    private String eventName;
    private String imageUrl;
    private String category;
    private String location;
    private String dateStart;
    private String cost;
    private String minParticipant, maxParticipant;
    private String requirement;
    private String description;
    private String status;
    private String idUser;

    private String mKey;

    public EventModel() {

    }

    public EventModel(String idEvent, String eventName, String imageUrl,
                      String category, String location,
                      String dateStart, String cost,
                      String minParticipant, String maxParticipant,
                      String requirement, String description,
                      String status, String idUser) {
        this.idEvent = idEvent;
        this.eventName = eventName;
        this.imageUrl = imageUrl;
        this.category = category;
        this.location = location;
        this.dateStart = dateStart;
        this.cost = cost;
        this.minParticipant = minParticipant;
        this.maxParticipant = maxParticipant;
        this.requirement = requirement;
        this.description = description;
        this.status = status;
        this.idUser = idUser;
    }

    protected EventModel(Parcel in) {
        idEvent = in.readString();
        eventName = in.readString();
        imageUrl = in.readString();
        category = in.readString();
        location = in.readString();
        dateStart = in.readString();
        cost = in.readString();
        minParticipant = in.readString();
        maxParticipant = in.readString();
        requirement = in.readString();
        description = in.readString();
        status = in.readString();
        idUser = in.readString();
        mKey = in.readString();
    }

    public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
        @Override
        public EventModel createFromParcel(Parcel in) {
            return new EventModel(in);
        }

        @Override
        public EventModel[] newArray(int size) {
            return new EventModel[size];
        }
    };

    public String getIdEvent() { return idEvent; }

    public String getEventName() {
        return eventName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getCost() {
        return cost;
    }

    public String getMinParticipant() {
        return minParticipant;
    }

    public String getMaxParticipant() {
        return maxParticipant;
    }

    public String getRequirement() {
        return requirement;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        this.mKey = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idEvent);
        parcel.writeString(eventName);
        parcel.writeString(imageUrl);
        parcel.writeString(category);
        parcel.writeString(location);
        parcel.writeString(dateStart);
        parcel.writeString(cost);
        parcel.writeString(minParticipant);
        parcel.writeString(maxParticipant);
        parcel.writeString(requirement);
        parcel.writeString(description);
        parcel.writeString(status);
        parcel.writeString(idUser);
        parcel.writeString(mKey);
    }
}
