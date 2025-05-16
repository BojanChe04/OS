package tcp.client;
import java.io.*;
import java.net.InetAddress;
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
        BufferedReader reader = null;
        PrintWriter writer = null;
        Scanner scanner = null;

        try {
            socket = new Socket(InetAddress.getByName(this.serverName),this.port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            scanner = new Scanner(System.in);

            // sea citat so reader so mu pratil serverov na klientov, na nego
            String line;
            while ((line = scanner.nextLine()) != null){
                //prakjame od klientot do serverov
                writer.println(line);
                writer.flush();
                //citame od serverot
                String response = reader.readLine();// so reader citame so ni pratil nas serverov
                System.out.println("Server: "+response);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient(8080, "localhost");
        client.start();
    }
}
