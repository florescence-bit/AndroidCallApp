package com.adivya.myapplication;

public class Contact {

    private final String name;
    private final String phoneNumber;
    private final String photoUri;

    public Contact(String name, String phoneNumber, String photoUri) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
