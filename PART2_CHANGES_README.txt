Part 2 integration summary

This project keeps the Part 1 registration and login flow, then opens QuickChat only after a successful login.

Part 2 features added:
- Welcome to QuickChat message.
- Numeric menu with options 1) Send Messages, 2) Show recently sent messages, 3) Quit.
- Option 2 displays Coming Soon.
- While loop keeps the app running until Quit is selected.
- User enters the maximum number of messages at the start.
- For loop lets the user enter only the selected number of messages.
- Message class added with the required methods:
  checkMessageID(), checkRecipientCell(), createMessageHash(), SentMessage(), printMessages(), returnTotalMessagess(), storeMessage().
- Ten-digit random Message ID generation.
- Message number uses the loop counter.
- Recipient validation using +27 followed by 9 digits.
- Message length validation for 250 characters.
- Message hash using first two message ID digits, message number, first word and last word in uppercase.
- Send / Disregard / Store options.
- Stored messages are written to stored_messages.json.
- JUnit tests added for the Part 2 test data and expected outputs.
- GitHub Actions workflow added at .github/workflows/TestJava.yml.

Note: The PDF also requires GitHub submission with enough commits and a video presentation. Those must still be done in your own GitHub/Arc submission.
