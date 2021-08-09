package networking;

import model.ChatRoom;
import model.Message;

public abstract class Network implements Runnable {

    protected ChatRoom room;
    protected boolean quit;

    public Network(ChatRoom room) {
        this.room = room;
    }

    // setter
    public void setQuit(boolean isQuit) {
        quit = isQuit;
    }



    // EFFECTS: sends a message to the network
    public abstract void sendMessage(Message message);
}
