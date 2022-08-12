package com.mnkadafi.acaraanbarengapps;

public class EventParticipantModel {

    private String eventParticipantId;
    private String idEvent;
    private String idUser;

    public EventParticipantModel() {

    }

    public EventParticipantModel(String eventParticipantId, String idEvent, String idUser) {
        this.eventParticipantId = eventParticipantId;
        this.idEvent = idEvent;
        this.idUser = idUser;
    }

    public String getEventParticipantId() { return eventParticipantId; }

    public String getIdEvent() { return idEvent; }

    public String getIdUser() {
        return idUser;
    }
}
