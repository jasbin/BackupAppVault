package com.app.backup.data.model;

import com.google.gson.annotations.SerializedName;

public class SmsModel {

    public static final int TYPE_INBOX = 1;
    public static final int TYPE_SENT = 2;
    public static final int TYPE_DRAFT = 3;
    public static final int TYPE_OUTBOX = 4;
    public static final int TYPE_FAILED = 5;

    @SerializedName("id")
    private String id;

    @SerializedName("address")
    private String address;

    @SerializedName("body")
    private String body;

    @SerializedName("date")
    private long date;

    @SerializedName("date_sent")
    private long dateSent;

    @SerializedName("type")
    private int type;

    @SerializedName("read")
    private int read;

    @SerializedName("thread_id")
    private String threadId;

    @SerializedName("person_name")
    private String personName;

    @SerializedName("subject")
    private String subject;

    @SerializedName("service_center")
    private String serviceCenter;

    // Constructors
    public SmsModel() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public long getDateSent() { return dateSent; }
    public void setDateSent(long dateSent) { this.dateSent = dateSent; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public int getRead() { return read; }
    public void setRead(int read) { this.read = read; }

    public String getThreadId() { return threadId; }
    public void setThreadId(String threadId) { this.threadId = threadId; }

    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getServiceCenter() { return serviceCenter; }
    public void setServiceCenter(String serviceCenter) { this.serviceCenter = serviceCenter; }

    public String getTypeLabel() {
        switch (type) {
            case TYPE_INBOX: return "Received";
            case TYPE_SENT: return "Sent";
            case TYPE_DRAFT: return "Draft";
            case TYPE_OUTBOX: return "Outbox";
            case TYPE_FAILED: return "Failed";
            default: return "Unknown";
        }
    }
}
