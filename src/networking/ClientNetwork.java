package networking;

import model.ChatRoom;
import model.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


// Represents a client in a chat room
public class ClientNetwork extends Network {

    private Socket socket;


    // EFFECTS: initializes a client with a room and socket
    public ClientNetwork(ChatRoom room, Socket s) {
        super(room);
        this.socket = s;
    }

    @Override
    public void run() {
        while (!quit) {
            listen(socket);
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
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
    public void sendMessage(Message message) {
        try {
            DataOutputStream dis = new DataOutputStream(socket.getOutputStream());
            dis.writeUTF(message.getSender());
            dis.writeUTF(message.getText());
            dis.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
