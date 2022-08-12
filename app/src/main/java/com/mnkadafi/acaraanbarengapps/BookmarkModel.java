package com.mnkadafi.acaraanbarengapps;

public class BookmarkModel {
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
}
