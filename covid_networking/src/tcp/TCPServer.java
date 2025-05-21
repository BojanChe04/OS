package tcp;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private int port;
    private String filePath;
    public TCPServer(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;
    }
    @Override
    public void run() {
        System.out.println("TCPServer is starting...");
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("TCPServer is started...");
        System.out.println("TCPServer is waithing for connections...");
        while(true){
            Socket socket = null;
            try{
                socket = serverSocket.accept();
                new WorkerThread(socket,filePath).start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(5555,"fileOutput.txt");
        tcpServer.start();
    }
}
