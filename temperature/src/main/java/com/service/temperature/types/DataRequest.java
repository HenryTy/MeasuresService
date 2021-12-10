package com.service.temperature.types;

import javax.xml.datatype.XMLGregorianCalendar;

public class DataRequest {

    protected XMLGregorianCalendar from;
    protected XMLGregorianCalendar to;
    protected int roomNr;

    public XMLGregorianCalendar getFrom() {
        return from;
    }

    public void setFrom(XMLGregorianCalendar from) {
        this.from = from;
    }

    public XMLGregorianCalendar getTo() {
        return to;
    }

    public void setTo(XMLGregorianCalendar to) {
        this.to = to;
    }

    public int getRoomNr() {
        return roomNr;
    }

    public void setRoomNr(int roomNr) {
        this.roomNr = roomNr;
    }
}
