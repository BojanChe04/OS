package tcp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends Thread {


    private String serverName;
    private int port;
    public TCPClient(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }
    @Override
    //kreirvit nova konekcija so serverot
    public void run() {
        Socket socket = null;

        Scanner scanner = null;
        PrintWriter printWriter = null;//eho porakata od serverot
        BufferedReader bufferedReader = null;// za porakite na nego so kje mu pristignet od server
        try{
            socket = new Socket(serverName,port);// na serverot,kaj accept mu prakajme
            printWriter = new PrintWriter(socket.getOutputStream());


            scanner = new Scanner(System.in);// aku sakame od standarden vlez podatoci
            // se dodeka se prakaat podatoci preku standarden vlez so while
            while(true){
                String line = scanner.nextLine();//vcitvime
                printWriter.println(line);//to so vcitavme prakjame do server, za server da ja ispecatit istata poraka
                printWriter.flush();
            }
        }catch(IOException e){

        }
    }

    public static void main(String[] args){
        TCPClient client = new TCPClient("localhost",9000);// go istancirame klientot
        client.start();
    }
}