package networking;

import com.dosse.upnp.UPnP;
import model.ChatRoom;
import model.Message;
import ui.ChatRoomApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HostNetwork extends Network {
    private ArrayList<Socket> sockets;
    private ServerSocket ss;

    public HostNetwork(ChatRoom room) {
        super(room);

        sockets = new ArrayList<>();

        try {
            ss = new ServerSocket(ChatRoomApp.PORT);
            ss.setSoTimeout(50);
        } catch (IOException e) {
            System.err.println("Server could not be created");
        }
    }

    // EFFECTS: listens to all the sockets for an update
    @Override
    public void run() {
        while (!quit) {
            for (Socket s: sockets) {
                listen(room, s);
            }
            try {
                sockets.add(ss.accept());
            } catch (Exception e) {
                //
            }
        }
        UPnP.closePortTCP(ChatRoomApp.PORT);
    }

    // setter
    public void setQuit(boolean isQuit) {
        quit = isQuit;
    }

    // EFFECTS: listens for an input from this client
    private void listen(ChatRoom room, Socket s) {
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            if (dis.available() > 0) {
                String username = dis.readUTF();
                String text = dis.readUTF();

                Message message = new Message(username, text);

                room.addMessage(message);

                sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("A user has disconnected");
            sockets.remove(s);
        }
    }

    @Override
    public void sendMessage(Message message) {
        ArrayList<Socket> failed = new ArrayList<>();
        for (Socket s: sockets) {
            try {
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message.getSender());
                dos.writeUTF(message.getText());
                dos.flush();

            } catch (IOException e) {
                failed.add(s);
                System.err.println("A user has disconnected");
            }
        }

        for (Socket s: failed) {
            sockets.remove(s);
        }
    }
}
