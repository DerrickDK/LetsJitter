package edu.uga.cs.letsjitter;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String senderName;

    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String name) {
        this.sender = sender; //current user
        this.receiver = receiver; //other user
        this.message = message;
        this.senderName = name;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", senderName='" + senderName + '\'' +
                '}';
    }
}
