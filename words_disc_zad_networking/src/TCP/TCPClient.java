package TCP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends Thread {
    private int port;
    private String serverName;

    public TCPClient(int port, String serverName) {
        this.port = port;
        this.serverName = serverName;
    }
    @Override
    public void run() {
        Socket socket = null;
        Scanner scanner = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            socket = new Socket(InetAddress.getByName(this.serverName), port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            scanner = new Scanner(System.in);

            writer.write("HANDSHAKE");
            writer.newLine();
            writer.flush();
            String serverResponse = reader.readLine();
            System.out.println("Server response1: "+serverResponse);

            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                writer.write(line);
                writer.newLine();
                writer.flush();

                String response2 = reader.readLine();
                if(response2==null) {
                    System.out.println("Server is CLOSED");
                    break;
                }
                System.out.println("Server response2: "+response2);
                if (response2.contains("LOGGED OUT")) {
                    break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (scanner != null) scanner.close();
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient(7391, "localhost");
        client.start();
    }
}
