//File: WriteThread.java
import java.io.*;
import java.net.*;

/* This thread is responsible for reading user's input and send it 
to the server. It runs in an infinite loop until the user types
'DONE' to quit. */
public class WriteThread extends Thread {

    private PrintWriter writer;
    private Socket socket;
    private cs_TCPClient client;

    public WriteThread(Socket socket, cs_TCPClient client) {
        this.socket = socket;
        this.client = client;
        

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " 
                    + ex.getMessage());
        }
    }

    public void run() {
        Console console = System.console();
        String userName = cs_TCPClient.u;
        if(userName==null)
            userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println(userName);
        String text;
        do {
            text = console.readLine();
            if(text!="DONE")
                writer.println(text);
        } while (!text.equals("DONE"));
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error writing to server: " 
                    + ex.getMessage());
        }
    }
}
