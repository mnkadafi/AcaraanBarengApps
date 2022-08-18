package com.mnkadafi.acaraanbarengapps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BookmarkModel implements Parcelable {
     private String bookmarkId;
     private String idUser;
     private String idEvent;
     private String eventName;
     private String category;
     private String imageUrl;

     public BookmarkModel() {

     }

     public BookmarkModel(String bookmarkId, String idUser,
                          String idEvent, String eventName,
                          String category, String imageUrl) {
        this.bookmarkId = bookmarkId;
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.eventName = eventName;
        this.category = category;
        this.imageUrl = imageUrl;
     }

    protected BookmarkModel(Parcel in) {
        bookmarkId = in.readString();
        idUser = in.readString();
        idEvent = in.readString();
        eventName = in.readString();
        category = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<BookmarkModel> CREATOR = new Creator<BookmarkModel>() {
        @Override
        public BookmarkModel createFromParcel(Parcel in) {
            return new BookmarkModel(in);
        }

        @Override
        public BookmarkModel[] newArray(int size) {
            return new BookmarkModel[size];
        }
    };

     public String getBookmarkId() { return bookmarkId; }
     public String getIdUser() { return idUser; }
     public String getIdEvent() { return idEvent; }
     public String getEventName() { return eventName; }
     public String getCategory() { return category; }
     public String getImageUrl() { return imageUrl; }

     public void setBookmarkId() { this.bookmarkId = bookmarkId; }
     public void setIdUser() { this.idUser = idUser; }
     public void setIdEvent() { this.idEvent = idEvent; }
     public void setEventName() { this.eventName = eventName; }
     public void setCategory() { this.category = category; }
     public void setImageUrl() { this.imageUrl = imageUrl; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bookmarkId);
        parcel.writeString(idUser);
        parcel.writeString(idEvent);
        parcel.writeString(eventName);
        parcel.writeString(category);
        parcel.writeString(imageUrl);
    }
}
