package tcp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends Thread {
    private int port = 9753;
    private String adresa = "194.149.135.49";
    private int index = 233090;
    private String filepath = "ispiti/filename.txt";
    public TCPClient(int port, String adresa) {
        this.port = port;
        this.adresa = adresa;
    }
    @Override
    public void run() {
        Socket socket = null;
        Scanner scanner = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        BufferedReader fileReader = null;
        try {
            socket = new Socket(adresa, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            fileReader = new BufferedReader(new FileReader(filepath));
            scanner = new Scanner(System.in);

            writer.write("hello:"+index);
            writer.newLine();
            writer.flush();

            String hello_response = reader.readLine();
            if(!hello_response.equals(index+":hello") || hello_response == null) {
                System.out.println("Invalid server response");
                return;
            }
            System.out.println("Uspesno se konetira i logra!");
            // ako e sve uspesno prakjame baranje za isprakjanje na fajl
            File file = new File(filepath);
            if(!file.exists()) {
                System.out.println("File not found");
                return;
            }
            //za golemina isprakjanje
            long fileSize = file.length();
            writer.write(index+":fileSize:"+fileSize);
            writer.newLine();
            writer.flush();
            //sea za attach
            writer.write(index+":attach:"+file.getName());
            writer.newLine();
            writer.flush();
            //ja prakjame sodrzinata sea
            String line;
            while((line = fileReader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
                writer.flush();
            }
            //sea za da zavrsam so transmisija
            writer.write(index+":over"+"\n");
            writer.flush();

            //odgovor od serverot
            String server_response = reader.readLine();
            if(server_response!=null) {
                System.out.println("Server response: "+server_response);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(scanner!=null) {
                scanner.close();
            }
            if(reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(writer!=null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(fileReader!=null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
