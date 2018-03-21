//File: ReadThread.java
import java.io.*;
import java.net.*;

/* This thread is responsible for reading server's input and 
printing it to the console. It runs in an infinite loop until
the client disconnects from the server. */
public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private cs_TCPClient client;
    

    public ReadThread(Socket socket, cs_TCPClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader
                (new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " 
                    + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                if(response!="DONE")
                    System.out.println(response);
            } catch (IOException ex) {
                System.out.println("-You have left the chat!-");
                break;
            }
        }
    }
    
}