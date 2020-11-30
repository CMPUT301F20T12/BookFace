package com.example.bookface.Notifications;

public class Data {

    private String Title;
    private String Message;
    private String ReceiverUserId;

    public Data(String title, String message, String receiverUserId) {
        Title = title;
        Message = message;
        ReceiverUserId = receiverUserId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getReceiverUserId() {
        return ReceiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        ReceiverUserId = receiverUserId;
    }
}
