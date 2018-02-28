Author: Courtney Sechrist
Issues encountered:
*Issues with initial set-up of the threads. Used to help set up threads
	for the server:
http://www.codejava.net/java-se/networking/how-to-create-a-chat-console-application-in-java-using-socket
*When userA types and submits in chat, userB would see the message,
	along with a line as if they had spoken, but no message.
*Issues making sure the username was shared between classes.
	-Issues with passing the username when -u is used.

Time spent on project: Approximately 10 hours.

Instructions:
1. Have at least 3 Command Prompts/Consoles open.
2. Change the directory to that of this program.
3. On any of the windows, type 'javac cs_TCPServer.java' then 'java cs_TCPClient.java' to compile the program.
4. On one window, type 'java cs_TCPServer [port number]', replacing [port number] with a number for the port. 22000 is suggested.
	3.1. This window becomes the Server window which will show when a new user enters, leaves, and when the chat file is deleted.
5. The remaining windows are for the Client. Type 'java cs_TCPClient' to connect to the server to chat. There you will have a prompt to choose a username.
	5.1 You may also automatically select a username by typing 'java cs_TCPClient -u [username]', replacing [username] with your chosen name.
		5.2.1. '-p' is also a possible command to select the port number. If a port number is not specified, the port will default to 22000.
		5.3.2. '-h' can also be used to chose a host.
6. When first connected, a message will be displayed containing the contents of the chat, if any.
7. As the user chats, their message is saved in a text file 'cs_chat.txt', which is shared with the server and the other user.
8. Typing 'DONE' allows the user to leave. Both the server and the other user will be notified of the user leaving.
9. Once the last user leaves, the chat file will be deleted, and a message will be displayed, server side, letting the user know that the file had been successfully deleted.
	
	