package tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private int port;
    private String counterFile;
    private String agrFile;

    public TCPServer(int port, String counterFile, String agrFile) {
        this.port = port;
        this.counterFile = counterFile;
        this.agrFile = agrFile;
    }
    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("TCPServer starting...");
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("TCPServer accepted");
                new WorkerThred(socket,agrFile,counterFile).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(5005,"counter.bin","agrFile.txt");
        tcpServer.start();
    }
}
