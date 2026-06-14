/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.main;

import java.util.Scanner;

/**
 * Main console application for Part 1, Part 2 and Part 3.
 * Part 2 and Part 3 only become available after a successful login.
 * Added arrays for sent, stored and disregarded messages.
 */
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login signIn = new Login();
        
        System.out.println("===Register===");
        
        System.out.println("Enter Username: ");
        String userName = input.nextLine();
        
        System.out.println("Enter Password: ");
        String password = input.nextLine();
        
        System.out.println("Enter phone number(+27): ");
        String cellNo = input.nextLine();
       
        String registerMessage = signIn.registerUser(userName, password, cellNo);
        System.out.println(registerMessage);
        
        System.out.println("===Login===");
        System.out.println("Enter Username: ");
        String userLogin = input.nextLine();
        
        System.out.println("Enter password: ");
        String loginPass = input.nextLine();
        
        boolean success = signIn.loginUser(userLogin, loginPass);
        String message = signIn.returnLoginStatus(success, "Orifha", "Muthige");
        System.out.println(message);
        
        if (success) {
            runQuickChat(input, cellNo);
        } else {
            System.out.println("You must login successfully before sending messages.");
        }
        
        input.close();
    }

    private static void runQuickChat(Scanner input, String senderCellNumber) {
        System.out.println("Welcome to QuickChat.");
        System.out.println("How many messages would you like to enter?");
        int maximumMessages = readInteger(input);
        int messagesEntered = 0;
        boolean running = true;

        while (running) {
            System.out.println("\nPlease choose one of the following features:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");

            int menuOption = readInteger(input);

            switch (menuOption) {
                case 1:
                    if (messagesEntered >= maximumMessages) {
                        System.out.println("You have already entered the set number of messages.");
                    } else {
                        for (; messagesEntered < maximumMessages; messagesEntered++) {
                            captureMessage(input, messagesEntered, senderCellNumber);
                        }
                    }
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    runStoredMessagesMenu(input);
                    break;
                case 4:
                    Message messageObject = new Message();
                    System.out.println("Total messages sent: " + messageObject.returnTotalMessagess());
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }
        }
    }


    private static void runStoredMessagesMenu(Scanner input) {
        boolean storedMenuRunning = true;

        while (storedMenuRunning) {
            System.out.println("\nStored Messages Menu:");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for messages by recipient");
            System.out.println("5) Delete a message using message hash");
            System.out.println("6) Display stored message report");
            System.out.println("7) Back to main menu");

            int storedOption = readInteger(input);

            switch (storedOption) {
                case 1:
                    System.out.println(Message.displayStoredSendersAndRecipients());
                    break;
                case 2:
                    System.out.println(Message.displayLongestStoredMessage());
                    break;
                case 3:
                    System.out.println("Enter message ID:");
                    String messageID = input.nextLine();
                    System.out.println(Message.searchByMessageID(messageID));
                    break;
                case 4:
                    System.out.println("Enter recipient number:");
                    String recipient = input.nextLine();
                    System.out.println(Message.searchMessagesByRecipient(recipient));
                    break;
                case 5:
                    System.out.println("Enter message hash:");
                    String hash = input.nextLine();
                    System.out.println(Message.deleteMessageByHash(hash));
                    break;
                case 6:
                    System.out.println(Message.displayReport());
                    break;
                case 7:
                    storedMenuRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose a number from 1 to 7.");
            }
        }
    }

    private static void captureMessage(Scanner input, int messageNumber, String senderCellNumber) {
        Message quickMessage = new Message();
        quickMessage.setMessageNumber(messageNumber);
        quickMessage.setSender(senderCellNumber);

        System.out.println(quickMessage.messageIDCreatedMessage());

        System.out.println("Enter recipient number with international code, for example +27718693002:");
        quickMessage.setRecipient(input.nextLine());
        System.out.println(quickMessage.checkRecipientCell());

        if (!quickMessage.isRecipientCellValid()) {
            return;
        }

        System.out.println("Enter your message:");
        quickMessage.setMessage(input.nextLine());
        String messageStatus = quickMessage.checkMessageLength();
        System.out.println(messageStatus);

        if (!quickMessage.isMessageLengthValid()) {
            return;
        }

        quickMessage.createMessageHash();

        System.out.println("Choose one of the following options:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int sendOption = readInteger(input);

        String sendStatus = quickMessage.SentMessage(sendOption);
        System.out.println(sendStatus);

        if (sendOption == 2) {
            int deleteOption = readInteger(input);
            if (deleteOption == 0) {
                System.out.println("Message deleted.");
            }
            return;
        }

        if (sendOption == 1 || sendOption == 3) {
            System.out.println("Message ID: " + quickMessage.getMessageID());
            System.out.println("Message Hash: " + quickMessage.getMessageHash());
            System.out.println("Recipient: " + quickMessage.getRecipient());
            System.out.println("Message: " + quickMessage.getMessage());
        }
    }

    private static int readInteger(Scanner input) {
        while (!input.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            input.nextLine();
        }
        int value = input.nextInt();
        input.nextLine();
        return value;
    }
}
