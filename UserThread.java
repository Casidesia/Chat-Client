//File:UserThread.java
import java.io.*;
import java.net.*;
import java.nio.file.*;

/* This thread handles connection for each connected client, so the
server can handle multiple clients at the same time. */
public class UserThread extends Thread {

    private final Socket socket;
    private final cs_TCPServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, cs_TCPServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            //set up for I/O
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader
                (new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            BufferedWriter outtext = new BufferedWriter
                (new FileWriter("cs_chat.txt", true));

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);
            String serverMessage = "\nNew user connected: " + userName;
            server.broadcast(serverMessage, this);
            //prints the previous contents of chat to the new user
            printChat(); 

            String clientMessage = null;
            FileReader intxt = new FileReader("cs_chat.txt");
            BufferedReader fileRead = new BufferedReader(intxt);
            /* section of code is synchronized so that the chat file 
            is locked to only one user at a time */
            synchronized (this) {
                do {
                    clientMessage = reader.readLine();
                    if (clientMessage != null) 
                        if (!clientMessage.equals("DONE")) {
                            outtext.write(userName + ": " 
                                    + clientMessage + "\n");
                            outtext.flush();
                            server.broadcast(userName + ": " 
                                    + clientMessage, this);
                        }
                } while (!clientMessage.equals("DONE"));
                //close the readers and make deleting the file possible.
                outtext.close();
                intxt.close();
                server.removeUser(userName, this);
                socket.close();
                /*once the last user leaves the chat, the chat
                file is deleted.*/
                serverMessage = userName + " has left.";
                server.broadcast(serverMessage, this);
                if (!server.hasUsers()) {
                    try {
                        Files.delete(Paths.get("cs_chat.txt"));
                        System.out.println("Last user left."
                                + " Chat file successfully deleted.");
                    } catch (IOException ex) {
                        System.out.println("Chat file could "
                                + "not be deleted.");
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error:" + ex.getMessage());
        }
    }

    //Sends a list of online users to the newly connected user.
    void printUsers() throws IOException {
        if (server.hasUsers())
            writer.println("Connected users: " + server.getUserNames());
        else 
            writer.println("No other users connected");
    }

    //Sends a message to the client.
    void sendMessage(String message) {
        writer.println(message);
    }

    /* Method is used when a user first connects, and prints the
    contents of the chat file for the new user. */
    private void printChat() throws IOException {
        FileReader intxt = new FileReader("cs_chat.txt");
        BufferedReader fileRead = new BufferedReader(intxt);
        String line = null;
        writer.println("\n----------\nThe chat before you joined:");
        while ((line = fileRead.readLine()) != null) {
            if (line != "DONE")
                writer.println(line);//send to the server
        }
        if (line == null)
            writer.println("----------");
        intxt.close();
    }
}
