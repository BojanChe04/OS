package TCP;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TCPServer extends Thread {
    private int port;
    public static HashMap<String,String> words = new HashMap<>();
    public static int brojac=0;
    public static final Object lock = new Object();
    private String filepath;

    public TCPServer(int port,String filepath) {
        this.port = port;
        this.filepath = filepath;
    }

    @Override
    public void run() {
        System.out.println("Server starting");
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Server started");
        System.out.println("Waiting for connections...");
        while(true){
            try{
                Socket socket = serverSocket.accept();
                new WorkThread(socket,filepath).start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(7391,"wordsFile.txt");
        tcpServer.start();
    }
}
