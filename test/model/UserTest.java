package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User testUser;
    ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        chatRoom = new ChatRoom();
        testUser = new User("test");
    }

    @Test
    void sendMessage() {
        testUser.sendMessage(chatRoom, "Hello");
        Message message = chatRoom.getMessages().get(0);
        assertEquals(message.getText(), "Hello");
        assertEquals(message.getSender(), "test");
    }
}