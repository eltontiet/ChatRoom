package model;

import java.util.ArrayList;
import java.util.Observable;

// Represents a chat room with different users
public class ChatRoom extends Observable {
    private ArrayList<Message> messages;
    private ArrayList<User> users;

    // MODIFIES: this
    // EFFECTS: creates a chat room
    public ChatRoom() {
        messages = new ArrayList<>();
        users = new ArrayList<>();
    }
//    TODO
//    // MODIFIES: this
//    // EFFECTS: creates a chat room with a message history
//    public ChatRoom(ArrayList<Message> messages) {
//
//    }

    // getters
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    // MODIFIES: this
    // EFFECTS: adds message to messages, and updates observers.
    public void addMessage(Message message) {
        messages.add(message);

        setChanged();
        notifyObservers(message);
        clearChanged();
    }

    // MODIFIES: this
    // EFFECTS: adds user to users, and updates observers.
    public void addUser(User user) {
        users.add(user);

        setChanged();
        notifyObservers(user);
        clearChanged();
    }
}
