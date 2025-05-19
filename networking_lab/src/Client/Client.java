package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;

        try{
            socket = new Socket(address, port);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            scanner = new Scanner(System.in);
            String line;
            while ((line = scanner.nextLine()) != null) {
                printWriter.println(line);
                printWriter.flush();
                String response = bufferedReader.readLine();
                System.out.println("Server response: " + response);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client(9753, "194.149.135.49");
        client.start();
    }
}
