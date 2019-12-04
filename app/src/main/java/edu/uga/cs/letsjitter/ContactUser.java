package edu.uga.cs.letsjitter;

/**
 * This class create the object of how a user is defined
 */
public class ContactUser {
    private String id;
    private String username;
    private String imageURL;

    public ContactUser(){

    }

    public ContactUser(String userID, String username, String imageURL) {
        this.id = userID;
        this.username = username;
        this.imageURL = imageURL;
    }

    public String getUserID() {
        return id;
    }

    public void setUserID(String userID) {
        this.id = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "ContactUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
