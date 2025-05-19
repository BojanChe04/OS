package Client;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socketServer;
    private String logFile = "chatlog236001.txt";
    public ClientThread() throws IOException {
        this.socketServer = new Socket("194.149.135.49", 9753);
    }

    @Override
    public void run() {
        BufferedReader reader = null;//citame od server
        PrintWriter writer = null;//mu pisime na server
        BufferedWriter logWriter = null;
        try{
            reader = new BufferedReader(new InputStreamReader(socketServer.getInputStream()));
            writer = new PrintWriter(socketServer.getOutputStream(), true);
            logWriter = new BufferedWriter(new FileWriter(logFile, true));

            //user input
            writer.println("hello:233090");
            writer.flush();
            //sea citame od server
            String response = reader.readLine();
            System.out.println("Server response: "+response);
//            writer.println("hello:123413");
//            System.out.println("Server: " + response);
            //sea kje zapisime vo logWriter so sme dobile i so sme pratile
            logWriter.write("login:233090:");
            logWriter.newLine();
            logWriter.write(response);
            logWriter.newLine();
            logWriter.flush();

            //ja prakjame hello porakata
            writer.println("hello:233090");
            writer.flush();

            String response2 = reader.readLine();
            System.out.println("Server response2: "+response2);
            //sea pak zapisvime vo fajlot
            logWriter.write("hello:233090:");
            logWriter.newLine();
            logWriter.write(response2);
            logWriter.newLine();
            logWriter.flush();

            // sea ni trebet posedni threads za prakjanje i posebna za primanje poraki od i do drugi klienti


        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        ClientThread clientThread = new ClientThread();
//        clientThread.start();
//        ClientThread clientThread1 = new ClientThread();
//        clientThread1.start();
//
//        clientThread.join();
//        clientThread1.join();
//    }
}