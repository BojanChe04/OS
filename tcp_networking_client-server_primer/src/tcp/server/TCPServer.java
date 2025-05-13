package tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {

    private int port;//portata na koja so kje slusat
    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public void run(){
        System.out.println("TCPServer starting...");
        ServerSocket serverSocket = null; // ova cekat novi konecii celo vreme

        try{
            serverSocket = new ServerSocket(port);//primat br na porta
        }catch (IOException e){
            System.err.println("Socket Server Failed to start");
            return;// aku nastanit exception da se terminirat samiov thread
        }
        System.out.println("TCPServer is started");

        System.out.println("Waiting for connection...");
        while(true){
            //serverot treba beskonecno da obrabotuva podatoci
            //ovde nikogas nemat da prekinit se dur nie racno ne prekinime
            Socket socket = null;// kreirvit soket
            try{//kako se istancirvit
               socket = serverSocket.accept();// ovde postojano sve kje bidit blokirano se dodeka ne se dobie nova konekcija/klient
                //som dobieme nov soket treba da go startuvame WorkerThread
                new WorkerThread(socket).start();
            }catch (IOException e){
                e.printStackTrace();
            }
            //ako iame pokje soketi, pokje klienti morat da naprajme pokje worker threads
            //tie kje rabotet paralelno, kje gi usluzvet klientive
            // zato kreirame klasa WorkerThread
        }
    }

    public static void main(String[] args) {
        //ovde trebit da se istanciret threads
        TCPServer tcpServer = new TCPServer(9000);//9000 e portata na koja so kje slusat
        tcpServer.start();
    }
}
