package com.mycompany.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Message class for Part 2 and Part 3 of the QuickChat POE.
 */
public class Message {
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String sender;

    private static int totalMessagesSent = 0;
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIDs = new ArrayList<>();
    private static final String JSON_FILE_NAME = "stored_messages.json";

    public Message() {
        this.messageID = generateMessageID();
    }

    public Message(String messageID, int messageNumber, String recipient, String message) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    public String generateMessageID() {
        Random random = new Random();
        long number = 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
        return String.valueOf(number);
    }

    public String messageIDCreatedMessage() {
        return "Message ID generated: " + messageID;
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Regex adapted from Oracle Java Pattern documentation:
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
     */
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public boolean isRecipientCellValid() {
        return "Cell phone number successfully captured.".equals(checkRecipientCell());
    }

    public String checkMessageLength() {
        if (message != null && message.length() <= 250) {
            return "Message ready to send.";
        }
        int extra = message == null ? 0 : message.length() - 250;
        return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
    }

    public boolean isMessageLengthValid() {
        return message != null && message.length() <= 250;
    }

    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2 || message == null || message.trim().isEmpty()) {
            messageHash = "";
            return messageHash;
        }

        String cleanMessage = message.trim();
        String[] words = cleanMessage.split("\\s+");
        String firstWord = removePunctuation(words[0]);
        String lastWord = removePunctuation(words[words.length - 1]);
        messageHash = (messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    private String removePunctuation(String word) {
        return word.replaceAll("[^a-zA-Z0-9]", "");
    }

    public String SentMessage(int userChoice) {
        switch (userChoice) {
            case 1:
                createMessageHash();
                sentMessages.add(this);
                addMessageTracking(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                createMessageHash();
                disregardedMessages.add(this);
                addMessageTracking(this);
                return "Press 0 to delete the message.";
            case 3:
                createMessageHash();
                storedMessages.add(this);
                addMessageTracking(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    private static void addMessageTracking(Message message) {
        if (message.getMessageID() != null && !messageIDs.contains(message.getMessageID())) {
            messageIDs.add(message.getMessageID());
        }
        if (message.getMessageHash() != null && !message.getMessageHash().isEmpty() && !messageHashes.contains(message.getMessageHash())) {
            messageHashes.add(message.getMessageHash());
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to display.";
        }

        StringBuilder output = new StringBuilder();
        for (Message sentMessage : sentMessages) {
            output.append("Message ID: ").append(sentMessage.getMessageID()).append(System.lineSeparator());
            output.append("Message Hash: ").append(sentMessage.getMessageHash()).append(System.lineSeparator());
            output.append("Recipient: ").append(sentMessage.getRecipient()).append(System.lineSeparator());
            output.append("Message: ").append(sentMessage.getMessage()).append(System.lineSeparator());
        }
        return output.toString().trim();
    }

    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    /**
     * JSON file writing adapted from Oracle FileWriter documentation:
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/FileWriter.html
     */
    public void storeMessage() {
        try {
            writeStoredMessagesAsJson();
        } catch (IOException exception) {
            System.out.println("Could not store message: " + exception.getMessage());
        }
    }

    private static void writeStoredMessagesAsJson() throws IOException {
        try (FileWriter writer = new FileWriter(JSON_FILE_NAME)) {
            writer.write("[" + System.lineSeparator());
            for (int i = 0; i < storedMessages.size(); i++) {
                Message current = storedMessages.get(i);
                writer.write("  {" + System.lineSeparator());
                writer.write("    \"messageID\": \"" + escapeJson(current.getMessageID()) + "\"," + System.lineSeparator());
                writer.write("    \"messageHash\": \"" + escapeJson(current.getMessageHash()) + "\"," + System.lineSeparator());
                writer.write("    \"sender\": \"" + escapeJson(current.getSender()) + "\"," + System.lineSeparator());
                writer.write("    \"recipient\": \"" + escapeJson(current.getRecipient()) + "\"," + System.lineSeparator());
                writer.write("    \"message\": \"" + escapeJson(current.getMessage()) + "\"" + System.lineSeparator());
                writer.write("  }");
                if (i < storedMessages.size() - 1) {
                    writer.write(",");
                }
                writer.write(System.lineSeparator());
            }
            writer.write("]" + System.lineSeparator());
        }
    }

    /**
     * JSON file reading adapted from Java Files.readString documentation:
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/file/Files.html
     */
    public static void readStoredMessagesFromJson() {
        Path path = Path.of(JSON_FILE_NAME);
        if (!Files.exists(path)) {
            return;
        }

        try {
            String json = Files.readString(path);
            Pattern objectPattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
            Matcher objectMatcher = objectPattern.matcher(json);

            storedMessages.clear();
            while (objectMatcher.find()) {
                String objectText = objectMatcher.group(1);
                Message stored = new Message(
                        extractJsonValue(objectText, "messageID"),
                        0,
                        extractJsonValue(objectText, "recipient"),
                        extractJsonValue(objectText, "message")
                );
                stored.setMessageHash(extractJsonValue(objectText, "messageHash"));
                stored.setSender(extractJsonValue(objectText, "sender"));
                storedMessages.add(stored);
                addMessageTracking(stored);
            }
        } catch (IOException exception) {
            System.out.println("Could not read stored messages: " + exception.getMessage());
        }
    }

    private static String extractJsonValue(String objectText, String key) {
        Pattern fieldPattern = Pattern.compile("\\\"" + key + "\\\"\\s*:\\s*\\\"(.*?)\\\"", Pattern.DOTALL);
        Matcher fieldMatcher = fieldPattern.matcher(objectText);
        if (fieldMatcher.find()) {
            return unescapeJson(fieldMatcher.group(1));
        }
        return "";
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    public static String displayStoredSendersAndRecipients() {
        readStoredMessagesFromJson();
        if (storedMessages.isEmpty()) {
            return "No stored messages to display.";
        }

        StringBuilder output = new StringBuilder();
        for (Message storedMessage : storedMessages) {
            output.append("Sender: ").append(storedMessage.getSender()).append(System.lineSeparator());
            output.append("Recipient: ").append(storedMessage.getRecipient()).append(System.lineSeparator());
        }
        return output.toString().trim();
    }

    public static String displayLongestStoredMessage() {
        readStoredMessagesFromJson();
        if (storedMessages.isEmpty()) {
            return "No stored messages to display.";
        }

        String longestMessage = "";
        for (Message storedMessage : storedMessages) {
            if (storedMessage.getMessage() != null && storedMessage.getMessage().length() > longestMessage.length()) {
                longestMessage = storedMessage.getMessage();
            }
        }
        return longestMessage;
    }

    public static String searchByMessageID(String searchID) {
        readStoredMessagesFromJson();
        for (Message storedMessage : storedMessages) {
            if (storedMessage.getMessageID().equals(searchID)) {
                return "Recipient: " + storedMessage.getRecipient() + System.lineSeparator()
                        + "Message: " + storedMessage.getMessage();
            }
        }
        for (Message sentMessage : sentMessages) {
            if (sentMessage.getMessageID().equals(searchID) || sentMessage.getRecipient().equals(searchID)) {
                return sentMessage.getMessage();
            }
        }
        return "Message ID not found.";
    }

    public static String searchMessagesByRecipient(String searchRecipient) {
        readStoredMessagesFromJson();
        StringBuilder output = new StringBuilder();
        for (Message storedMessage : storedMessages) {
            if (storedMessage.getRecipient().equals(searchRecipient)) {
                output.append(storedMessage.getMessage()).append(System.lineSeparator());
            }
        }

        if (output.length() == 0) {
            return "No messages found for recipient.";
        }
        return output.toString().trim();
    }

    public static String deleteMessageByHash(String hash) {
        readStoredMessagesFromJson();
        for (int i = 0; i < storedMessages.size(); i++) {
            Message storedMessage = storedMessages.get(i);
            if (storedMessage.getMessageHash().equals(hash)) {
                String deletedMessage = storedMessage.getMessage();
                storedMessages.remove(i);
                messageHashes.remove(hash);
                try {
                    writeStoredMessagesAsJson();
                } catch (IOException exception) {
                    return "Could not delete message: " + exception.getMessage();
                }
                return "Message: \"" + deletedMessage + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    public static String displayReport() {
        readStoredMessagesFromJson();
        if (sentMessages.isEmpty() && storedMessages.isEmpty()) {
            return "No messages to report.";
        }

        StringBuilder report = new StringBuilder("Message Report" + System.lineSeparator());
        for (Message sentMessage : sentMessages) {
            report.append("Message Hash: ").append(sentMessage.getMessageHash()).append(System.lineSeparator());
            report.append("Recipient: ").append(sentMessage.getRecipient()).append(System.lineSeparator());
            report.append("Message: ").append(sentMessage.getMessage()).append(System.lineSeparator());
        }
        for (Message storedMessage : storedMessages) {
            report.append("Message Hash: ").append(storedMessage.getMessageHash()).append(System.lineSeparator());
            report.append("Recipient: ").append(storedMessage.getRecipient()).append(System.lineSeparator());
            report.append("Message: ").append(storedMessage.getMessage()).append(System.lineSeparator());
        }
        return report.toString().trim();
    }

    public static void resetMessagesForTesting() {
        totalMessagesSent = 0;
        sentMessages.clear();
        storedMessages.clear();
        disregardedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        File file = new File(JSON_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    public static List<Message> getSentMessages() {
        return sentMessages;
    }

    public static List<Message> getStoredMessages() {
        readStoredMessagesFromJson();
        return storedMessages;
    }

    public static List<Message> getDisregardedMessages() {
        return disregardedMessages;
    }

    public static List<String> getMessageHashes() {
        return messageHashes;
    }

    public static List<String> getMessageIDs() {
        return messageIDs;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHash() {
        if (messageHash == null || messageHash.isEmpty()) {
            return createMessageHash();
        }
        return messageHash;
    }

    public void setMessageHash(String messageHash) {
        this.messageHash = messageHash;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
