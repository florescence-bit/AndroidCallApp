package com.adivya.myapplication;

public class CallLogEntry {

    private final String name;
    private final String number;
    private final long time;
    private final int type;
    private final String photoUri;

    public CallLogEntry(String name, String number, long time, int type, String photoUri) {
        this.name = name;
        this.number = number;
        this.time = time;
        this.type = type;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public long getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
