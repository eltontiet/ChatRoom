package model;

// Represents a message in a chat room, with a sender and text.
public class Message {

    private String sender;
    private String text;
    private boolean edited;

    // MODIFIES: this
    // EFFECTS: creates a message with a sender and text
    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        edited = false;
    }

    // getters
    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public boolean isEdited() {
        return edited;
    }

    // MODIFIES: this
    // EFFECTS: edits this messages text, and sets edited to true;
    public void setText(String text) {
        this.text = text;
        edited = true;
    }
}
