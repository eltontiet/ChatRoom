package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    Message testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new Message("Joe", "Hey Boss!");
    }

    @Test
    void getSender() {
        assertEquals(testMessage.getSender(), "Joe");
    }

    @Test
    void setText() {
        assertFalse(testMessage.isEdited());
        assertEquals(testMessage.getText(), "Hey Boss!");
        testMessage.setText("Hello Boss.");
        assertTrue(testMessage.isEdited());
        assertEquals(testMessage.getText(), "Hello Boss.");
    }
}