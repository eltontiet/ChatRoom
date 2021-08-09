package model;

// Represents a user of the ChatRoom
public class User {
    private String username;

    // EFFECTS: creates a user with a username
    public User(String userName) {
        this.username = userName;
    }

    // EFFECTS: sends a message to room
    public void sendMessage(ChatRoom room, String text) {
        Message message = new Message(getUsername(), text);
        room.addMessage(message);
    }

    // getters
    public String getUsername() {
        return username;
    }
}
