package tcp.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.ServerSocket;

public class TCPServer extends Thread {
    private static int totalMessages = 0;

    private int port;
    private String filepath;//lak da zapisvit
    public TCPServer(int port, String filepath) {
        this.port = port;
        this.filepath = filepath;
    }

    public static int getTotalMessages() {
        return totalMessages;
    }
    public static synchronized void increment(){
        // increment messages from client
        totalMessages++;
    }

    @Override
    public void run() {
        System.out.println("TCPServer starting...");
        ServerSocket serverSocket = null;

        try{
            serverSocket = new ServerSocket(port);

        }catch (IOException e){
            System.err.println("Socket Server Failed to start");
            return;
        }
        System.out.println("TCPServer started");
        while(true){
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                new WorkerThread(socket,filepath).start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(8080, "logfile.txt");
        tcpServer.start();
    }
}
