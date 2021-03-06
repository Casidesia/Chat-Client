//File: cs_TCPClient.java
import java.net.*;
import java.io.*;

public class cs_TCPClient {

    private static InetAddress host;
    private static int p;
    static String u=null;

    public static void main(String[] args) {
        String tmp;
        try {
            // Get server IP-address
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-h":
                        tmp = args[i + 1];
                        host = InetAddress.getByName(args[i + 1]);
                        break;
                    case "-p":
                        tmp = args[i + 1];
                        p = (int) Integer.parseInt(args[i + 1]);
                        break;
                    case "-u":
                        tmp = args[i + 1];
                        u = args[i + 1];
                        break;
                }
            }
            if (host == null) {
                host = InetAddress.getByName("localhost");
            }
            if (p == 0) {
                p = 22000;
            }
            /* If the user does not specify a username, it is
            handled later with the WriteThread class. */

        } catch (UnknownHostException e) {
            System.out.println("Host ID not found!");
            System.exit(1);
        }

        cs_TCPClient client = new cs_TCPClient(host, p);
        client.run();
    }

    public cs_TCPClient(InetAddress hostname, int port) {
        this.host = hostname;
        this.p = port;
    }
    
    public void run() {
        try {
            Socket socket = new Socket(host, p);

            System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }

    void setUserName(String userName) {
        this.u = userName;
    }

    String getUserName() {
        return this.u;
    }

}