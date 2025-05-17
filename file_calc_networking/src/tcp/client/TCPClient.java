package tcp.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient extends Thread {
    private String adress;
    private int port;
    private String filePath;
    public TCPClient(String adress, int port, String filePath) {
        this.adress = adress;
        this.port = port;
        this.filePath = filePath;
    }
    @Override
    public void run() {
        Socket socket = null;
        BufferedReader fileReader = null;
        BufferedReader socketReader = null;
        BufferedWriter writer = null;

        try {
            socket = new Socket(InetAddress.getByName(this.adress),port);
            System.out.println("Client connected to server");
            fileReader = new BufferedReader(new FileReader(this.filePath));// da citat od datoteka
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));// da pisit kon server
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));// da citat od soket

            writer.write("hello");// se isprakjat inicijalna poraka
            writer.newLine();
            writer.flush();

            String line;
            line = socketReader.readLine();// cekame odg od serverot
            System.out.println("Server responded: " + line);

            if(line.equals("Send file")) {
                while ((line=fileReader.readLine()) != null) {// go prakjame fajlot linija po linija
                    writer.write(line);
                    writer.newLine();
                }
                writer.newLine();
                writer.flush();

                line = socketReader.readLine();// citat krajna poraka od serverov, neso so kje vratit serverot
                System.out.println(line);
            }
            else{
                System.out.println("Error in messages");
            }
            fileReader.close();
            socketReader.close();
            writer.flush();
            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient("localhost", 5005, "C:\\Users\\bojan\\OneDrive\\Desktop\\operativni\\file_calc_networking\\src\\data\\points3.txt");
        client.start();
    }
}
