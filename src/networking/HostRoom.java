package networking;

import model.ChatRoom;
import model.Message;
import ui.ChatRoomApp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HostRoom implements Runnable {
    private ArrayList<Socket> sockets;
    private ServerSocket ss;
    private boolean quit;
    private ChatRoom room;

    public HostRoom(ChatRoom room) {
        this.room = room;

        sockets = new ArrayList<>();

        try {
            ss = new ServerSocket(ChatRoomApp.PORT);
            ss.setSoTimeout(1000);
        } catch (IOException e) {
            System.err.println("Server could not be created");
        }
    }

    // EFFECTS: listens for an input from this client
    private void listen(Socket s) {
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
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
            for (Socket s: sockets) {
                listen(s);
            }
        }


    }

    // setter
    public void setQuit(boolean isQuit) {
        quit = isQuit;
    }
}
