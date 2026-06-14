package com.mycompany.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @BeforeEach
    public void setup() {
        Message.resetMessagesForTesting();
    }

    @Test
    public void testMessageLengthSuccess() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "A".repeat(255);
        Message message = new Message("0012345678", 0, "+27718693002", longMessage);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", message.checkMessageLength());
    }

    @Test
    public void testRecipientNumberCorrectlyFormatted() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }

    @Test
    public void testRecipientNumberIncorrectlyFormatted() {
        Message message = new Message("0012345678", 1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", message.checkRecipientCell());
    }

    @Test
    public void testMessageHashIsCorrect() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        Message[] messages = {
            new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?"),
            new Message("1112345678", 1, "+27838968976", "Hello John")
        };

        String[] expectedHashes = {
            "00:0:HITONIGHT",
            "11:1:HELLOJOHN"
        };

        for (int i = 0; i < messages.length; i++) {
            assertEquals(expectedHashes[i], messages[i].createMessageHash());
        }
    }

    @Test
    public void testMessageIDIsCreated() {
        Message message = new Message();
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
        assertTrue(message.messageIDCreatedMessage().startsWith("Message ID generated: "));
    }

    @Test
    public void testMessageSuccessfullySent() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", message.SentMessage(1));
        assertEquals(1, message.returnTotalMessagess());
    }

    @Test
    public void testMessageDisregarded() {
        Message message = new Message("0012345678", 1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", message.SentMessage(2));
    }

    @Test
    public void testMessageStored() {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", message.SentMessage(3));
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        populatePart3TestData();

        assertEquals("Did you get the cake?", Message.getSentMessages().get(0).getMessage());
        assertEquals("It is dinner time !", Message.getSentMessages().get(1).getMessage());
    }

    @Test
    public void testDisplayLongestStoredMessage() {
        populatePart3TestData();

        assertEquals("Where are you? You are late! I have asked you to be on time.", Message.displayLongestStoredMessage());
    }

    @Test
    public void testSearchForMessageID() {
        populatePart3TestData();

        assertEquals("It is dinner time !", Message.searchByMessageID("0838884567"));
    }

    @Test
    public void testSearchAllMessagesForParticularRecipient() {
        populatePart3TestData();

        String result = Message.searchMessagesByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageUsingMessageHash() {
        populatePart3TestData();

        String messageTwoHash = "22:2:WHERETIME";
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", Message.deleteMessageByHash(messageTwoHash));
    }

    @Test
    public void testDisplayReport() {
        populatePart3TestData();

        String report = Message.displayReport();
        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Recipient"));
        assertTrue(report.contains("Message"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time !"));
    }

    @Test
    public void testReadJsonFileIntoStoredArray() {
        populatePart3TestData();

        assertEquals(2, Message.getStoredMessages().size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", Message.getStoredMessages().get(0).getMessage());
        assertEquals("Ok, I am leaving without you.", Message.getStoredMessages().get(1).getMessage());
    }

    private void populatePart3TestData() {
        Message message1 = new Message("1111111111", 1, "+27834557896", "Did you get the cake?");
        message1.setSender("Developer");
        message1.SentMessage(1);

        Message message2 = new Message("2222222222", 2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        message2.setSender("Developer");
        message2.SentMessage(3);

        Message message3 = new Message("3333333333", 3, "+27834484567", "Yohoooo, I am at your gate.");
        message3.setSender("Developer");
        message3.SentMessage(2);

        Message message4 = new Message("4444444444", 4, "0838884567", "It is dinner time !");
        message4.setSender("Developer");
        message4.SentMessage(1);

        Message message5 = new Message("5555555555", 5, "+27838884567", "Ok, I am leaving without you.");
        message5.setSender("Developer");
        message5.SentMessage(3);
    }
}
