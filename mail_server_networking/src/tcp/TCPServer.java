package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private int port;
    private String filePath;
    public static int brojac=0;
    public static final Object lock = new Object();

    public TCPServer(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;

    }
    @Override
    public void run() {
        System.out.println("Server is starting...");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("TCPServer is started...");
        System.out.println("TCPServer is waithing for connections...");
        while(true) {
            Socket socket = null;
            try{
                socket = serverSocket.accept();
                new WorkerThread(socket,filePath).start();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(8765, "fileOutput.txt");
        tcpServer.start();
    }
}
