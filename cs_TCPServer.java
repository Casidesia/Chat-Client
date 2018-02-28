//File: cs_TCPServer.java
import java.io.*;
import java.net.*;
import java.util.*;

public class cs_TCPServer {
    
    private final int port;
    private final Set<String> userNames = new HashSet<>();
    private final Set<UserThread> userThreads = new HashSet<>();
    private static ServerSocket servSock;

    public static void main(String[] args) throws IOException {
        if (args == null) 
            servSock = new ServerSocket(Integer.parseInt(args[0]));
        int port = Integer.parseInt(args[0]);

        cs_TCPServer server = new cs_TCPServer(port);
        server.run();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Opening port...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }
        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    //Delivers a message from one user to others (broadcasting)
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

     //Stores username of the newly connected client.
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /* When a client is disconneted, removes the associated username and 
     UserThread */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println(userName + " left the chat.");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    /*Returns true if there are other users connected*/
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
    
    public cs_TCPServer(int port) {
        this.port = port;
    }
}