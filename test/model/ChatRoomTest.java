package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChatRoomTest {

    ChatRoom testChatRoom;

    @BeforeEach
    void setUp() {
        testChatRoom = new ChatRoom();
    }

    @Test
    void addMessage() {
        testChatRoom.addMessage(new Message("Jon", "Hello"));
        ArrayList<Message> messages = testChatRoom.getMessages();
        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0).getText());
        assertEquals("Jon", messages.get(0).getSender());
    }

    @Test
    void addUser() {
        testChatRoom.addUser(new User("Mary"));
        ArrayList<User> users = testChatRoom.getUsers();
        assertEquals("Mary", users.get(0).getUsername());
    }

}