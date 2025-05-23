package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private int port;
    private String address;
    public Client(int port, String address) {
        this.port = port;
        this.address = address;
    }
    @Override
    public void run() {
        Socket socket = null;
        Scanner scanner = null;
        BufferedWriter writer = null;
        PrintWriter logWriter = null;
        BufferedReader bufferedReader = null;

        try{
            socket = new Socket(address, port);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            logWriter = new PrintWriter(new FileWriter("chatlog233090.txt", true), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//            scanner = new Scanner(System.in);
//            String line;
//            while ((line = scanner.nextLine()) != null) {
//                writer.write(line);
//                writer.newLine();
//                writer.flush();
//                String response = bufferedReader.readLine();
//                System.out.println("Server response: " + response);
//            }
            new SendThread(writer,logWriter).start();
            new ReceivedThread(logWriter,bufferedReader).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client(9753, "194.149.135.49");
        client.start();
    }
}
