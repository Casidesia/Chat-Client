//File: cs_TCPClient.java
import java.net.*;
import java.io.*;
import java.util.Random;


public class cs_TCPClient {

    private static InetAddress host;
    private static int p=0;
    static String u=null;
    public static int skey;

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
                        //tmp = args[i + 1];
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
            int skey=handshake(cs_TCPServer.g,cs_TCPServer.n);
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
    public static int handshake(int g, int n){
        
        Random rand = new Random();
        int x = rand.nextInt(247)+10;
        //int x = 145;
        System.out.println("The random number created at the handshake is "+x+".");
        System.out.println("skey: "+skey+" g: "+g+" x:"+x+" n: "+n);
        //int skey = g^x%n;//MAY HAVE AN OVERFLOW WITH THE POWER, with the x being too large.
        int skey=1;
        for(int i=1;i<=x;i++){
            skey= (skey*g)%n;
        }
        System.out.println("skey: "+skey+" g: "+g+" x:"+x+" n: "+n);//DEBUGGING
        return skey;     
    }

}