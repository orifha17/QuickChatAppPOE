PART 3 INTEGRATION NOTES

This version keeps the existing Part 1 registration/login logic and Part 2 message-sending logic, then extends the application for Part 3.

Added for Part 3:
1. Arrays/lists for:
   - Sent messages
   - Stored messages
   - Disregarded messages
   - Message hashes
   - Message IDs
2. A fourth QuickChat menu option: Stored Messages.
3. Stored Messages submenu options for:
   - Display sender and recipient of stored messages
   - Display the longest stored message
   - Search by message ID
   - Search by recipient
   - Delete by message hash
   - Display a message report
4. JSON reading support so stored_messages.json can be read back into the stored messages array/list.
5. JUnit tests for the Part 3 test data and expected outputs.

The project still uses a console application only.
