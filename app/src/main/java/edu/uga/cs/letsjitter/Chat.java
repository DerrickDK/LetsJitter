package edu.uga.cs.letsjitter;

/**
 * This chat class creates the object of how chat is structured
 */
public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String senderName;


    public Chat() {
    }

    /**
     *
     * @param sender id of the one sending a chat
     * @param receiver id of the one receiving a chat
     * @param message the message to be sent
     * @param name the sender's name
     */
    public Chat(String sender, String receiver, String message, String name) {
        this.sender = sender; //current user
        this.receiver = receiver; //other user
        this.message = message;
        this.senderName = name;
    }

    /**
     * @return sender's id
     */
    public String getSender() {
        return sender;
    }

    /**
     * set sender's id
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
    /**
     * @return get receiver id
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * set sender's id
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     *
     * @return get message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message set message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return get sender's name
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     *
     * @param senderName set sender's name
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     *
     * @return string of chat object
     */

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
