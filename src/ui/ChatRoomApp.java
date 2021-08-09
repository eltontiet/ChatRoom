package ui;

import com.dosse.upnp.UPnP;
import model.ChatRoom;
import model.Message;
import model.User;
import networking.ClientNetwork;
import networking.HostNetwork;
import networking.Network;

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
    public static final String USER_JOIN_MESSAGE = "A user has joined the chat";

    String username;
    User user;
    Scanner input;
    ArrayList<ChatRoom> chatRooms; // Could be used in the future to have multiple chat rooms.
    ChatRoom currentChatRoom;
    Network networkRoom;
    boolean isHost = false;
    boolean quit = false;

    // MODIFIES: this
    // EFFECTS: runs the chat room app
    public ChatRoomApp() {
        init();

        System.out.println("What is your username?");
        username = getInput();

        user = new User(username);

        getType();

        while (!quit) {
            chat();
        }
    }

    // EFFECTS: allows the user to chat in the room
    private void chat() {
        String command = getInput();
        if (command.length() > 0) {
            if (command.charAt(0) == '/') {
                checkCommands(command);
            } else {
                networkRoom.sendMessage(new Message(username, command));
            }
            System.out.println();
        }
    }

    // EFFECTS: allows the user to input a command
    private void checkCommands(String command) {
        command = command.toLowerCase();
        switch(command) {
            case "/help":
                printHelp();
                break;
            case "/quit":
                networkRoom.setQuit(true);
                quit = true;
                System.out.println("Quitting the app...");
                break;
            default:
                System.out.println("Command could not be recognized, ");
        }
    }

    // EFFECTS: prints a help screen that displays all the commands
    private void printHelp() {
        System.out.println("/quit to quit");
        System.out.println("/help for help");
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

        boolean failed = true;

        for (int i = 0; i < RETRIES; i++) {
            try {
                Socket socket = new Socket();
                socket.connect(address);

                ChatRoom room = new ChatRoom();
                networkRoom = new ClientNetwork(room, socket);
                currentChatRoom = room;
                chatRooms.add(room);
                joinRoom(room);

                new Thread(networkRoom).start();
                System.out.println("Connected Successfully");
                failed = false;
                break;

            } catch (Exception e) {
                System.out.format("Could not connect, retrying %d out of %d times\n", i + 1, RETRIES);
            }
        }

        if (failed) {
            System.err.println("Could not connect");

            getType();
        }
    }

    // EFFECTS: hosts a chat room
    private void host() {
        if (UPnP.isUPnPAvailable()) {
            if (UPnP.isMappedTCP(PORT)) {
                System.err.println("Port is in use");
                System.out.println("Would you like to try closing the port? (y/n)");
                String command = getInput().toLowerCase();
                if (command.equals("y")) {
                    UPnP.closePortTCP(PORT);
                    host();
                } else {
                    getType();
                }
            } else if (UPnP.openPortTCP(PORT)) {
                System.out.println("Opened port!");

                ChatRoom room = new ChatRoom();
                joinRoom(room);
                chatRooms.add(room);
                currentChatRoom = room;

                networkRoom = new HostNetwork(room);
                new Thread(networkRoom).start();
                isHost = true;

            } else {
                System.err.println("Port could not be opened");
                getType();
            }
        } else {
            System.err.println("UPnP is disabled");
            getType();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the input scanner
    private void init() {
        input = new Scanner(System.in);
        chatRooms = new ArrayList<>();
    }

    // EFFECTS: returns the next line in the scanner
    private String getInput() {
        input.hasNext();
        return input.nextLine();
    }

    // EFFECTS: returns a message based on the new update
    @Override
    public void update(Observable o, Object obj) {
        if (obj.getClass() == Message.class) {
            Message message = (Message) obj;
            if (!username.equals(message.getSender())) {
                System.out.println(message.getSender() + ":");
                System.out.println(message.getText() + "\n");
            }
        } else {
            User user = (User) obj;
            System.out.println(user.getUsername() + " has joined the chat\n");
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
