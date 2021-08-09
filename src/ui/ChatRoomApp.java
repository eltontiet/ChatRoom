package ui;

import com.dosse.upnp.UPnP;
import model.ChatRoom;
import model.Message;
import model.User;
import networking.ClientRoom;
import networking.HostRoom;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

// Represents a chat room app
public class ChatRoomApp implements Observer {

    public static final int PORT = 45213;
    private static final int RETRIES = 5;

    String username;
    User user;
    Scanner input;
    ArrayList<ChatRoom> chatRooms; // Could be used in the future to have multiple chat rooms.
    ChatRoom currentChatRoom;

    // MODIFIES: this
    // EFFECTS: runs the chat room app
    public ChatRoomApp() {
        init();

        System.out.println("What is your username? \n");
        username = getInput();

        user = new User(username);

        getType();
    }

    // EFFECTS: asks user to host or join a chat room
    private void getType() {
        System.out.println("Type 'h' to host a room, or 'c' to join a room");
        String command = getInput();
        command = command.toLowerCase();

        if (command.equals("h")) {
            host();
        } else if (command.equals("c")) {
            join();
        } else {
            getType();
        }
    }

    // EFFECTS: prompts user, and joins a chat room
    private void join() {
        System.out.println("Input the ip of a chat room to join: ");
        String ip = getInput();

        SocketAddress address = new InetSocketAddress(ip, PORT);

        for (int i = 0; i < RETRIES; i++) {
            try {
                Socket socket = new Socket();
                socket.connect(address);
                ChatRoom room = new ChatRoom();
                new Thread(new ClientRoom(room, socket)).start();
                break;

            } catch (Exception e) {
                System.out.format("Could not connect, retrying %d out of %d times\n", i + 1, RETRIES);
            }
        }

    }

    // EFFECTS: hosts a chat room
    private void host() {
        if (UPnP.isUPnPAvailable()) {
            if (UPnP.isMappedTCP(PORT)) {
                System.err.println("Port is in use");
            } else if (UPnP.openPortTCP(PORT)) {
                System.out.println("Opened port!");

                ChatRoom room = new ChatRoom();
                joinRoom(room);
                chatRooms.add(room);
                currentChatRoom = room;
                new Thread(new HostRoom(room)).start();

            } else {
                System.err.println("Port could not be opened");
            }
        } else {
            System.err.println("UPnP is disabled");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the input scanner
    private void init() {
        input = new Scanner(System.in);
    }

    // EFFECTS: returns the next line in the scanner
    private String getInput() {
        input.hasNext();
        return input.nextLine();
    }

    // EFFECTS: returns a message based on the new update
    @Override
    public void update(Observable o, Object obj) {
        if (obj.getClass().toString().equals("Message")) {
            Message message = (Message) obj;
            System.out.println(message.getSender());
            System.out.println(message.getText());
        } else {
            User user = (User) obj;
            System.out.println(user.getUsername() + " has joined the chat");
        }

    }

    // MODIFIES: this
    // EFFECTS: adds the chatroom to observers.
    public void joinRoom(ChatRoom room) {
        room.addObserver(this);
    }

    public static void main(String[] args) {
        new ChatRoomApp();
    }
}
