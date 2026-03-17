package com.app.backup.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ContactModel {

    @SerializedName("id")
    private String id;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("phones")
    private List<PhoneNumber> phones;

    @SerializedName("emails")
    private List<EmailAddress> emails;

    @SerializedName("organization")
    private String organization;

    @SerializedName("title")
    private String title;

    @SerializedName("notes")
    private String notes;

    @SerializedName("photo_uri")
    private String photoUri;

    // Constructors
    public ContactModel() {}

    public ContactModel(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public List<PhoneNumber> getPhones() { return phones; }
    public void setPhones(List<PhoneNumber> phones) { this.phones = phones; }

    public List<EmailAddress> getEmails() { return emails; }
    public void setEmails(List<EmailAddress> emails) { this.emails = emails; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }

    // Inner classes
    public static class PhoneNumber {
        @SerializedName("number")
        private String number;

        @SerializedName("type")
        private int type;

        @SerializedName("label")
        private String label;

        public PhoneNumber() {}

        public PhoneNumber(String number, int type, String label) {
            this.number = number;
            this.type = type;
            this.label = label;
        }

        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public int getType() { return type; }
        public void setType(int type) { this.type = type; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
    }

    public static class EmailAddress {
        @SerializedName("address")
        private String address;

        @SerializedName("type")
        private int type;

        @SerializedName("label")
        private String label;

        public EmailAddress() {}

        public EmailAddress(String address, int type, String label) {
            this.address = address;
            this.type = type;
            this.label = label;
        }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public int getType() { return type; }
        public void setType(int type) { this.type = type; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
    }
}
