package networking;

import model.ChatRoom;
import model.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

// TODO: HostRoom and ClientRoom have a lot of overlap

// Represents a client in a chat room
public class ClientRoom implements Runnable {

    private ChatRoom room;
    private Socket socket;
    private boolean quit;

    // EFFECTS: initializes a client with a room and socket
    public ClientRoom(ChatRoom room, Socket s) {
        this.room = room;
        this.socket = s;
    }

    // EFFECTS: listens for an input from this client
    private void listen() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            if (dis.available() > 0) {
                String username = dis.readUTF();
                String message = dis.readUTF();
                room.addMessage(new Message(username, message));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!quit) {
            listen();
        }
    }

    // setter
    public void setQuit(boolean isQuit) {
        quit = isQuit;
    }
}
