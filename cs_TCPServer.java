//File: cs_TCPServer.java

import java.io.*;
import java.net.*;
import java.util.*;

public class cs_TCPServer {

    private static int port;
    private final Set<String> userNames = new HashSet<>();
    private final Set<UserThread> userThreads = new HashSet<>();
    private static ServerSocket servSock;
    Scanner input = new Scanner(System.in);
    public static int g = 0;
    public static int n = 0;

    public static void main(String[] args) throws IOException {

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-p")) {
                    int port = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-g")) {
                    g = Integer.parseInt(args[i + 1]);
                } else if (args[i].equals("-n")) {
                    n = Integer.parseInt(args[i + 1]);
                }
                i++;
            }
        }
        if(port==0){
            port=22000;
        }
        if (n == 0) {
            n = 250;
        }
        if (g == 0) {
            g = 230;
        }
        cs_TCPServer server = new cs_TCPServer(port);
        if (servSock == null) {
            servSock = new ServerSocket(Integer.parseInt(args[0]));
        }

        //System.out.println("g="+g+", n="+cs_TCPClient.n);
        server.run();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Opening port...");
            while (true) {
                //System.out.println("What is your public code?");
                //g= input.nextInt();
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
    void broadcast(String message, UserThread excludeUser, String userName) {
        String emessage = UserThread.encrypt(message, cs_TCPClient.skey);
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(userName + ": " + message);
            }
        }
    }

    void broadcast(String message, UserThread excludeUser) {
        String emessage = UserThread.encrypt(message, cs_TCPClient.skey);
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
