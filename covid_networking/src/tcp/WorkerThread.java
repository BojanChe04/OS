package tcp;

import java.io.*;
import java.net.Socket;

public class WorkerThread extends Thread {
    private Socket socket;
    private String filePath;
    public WorkerThread(Socket socket, String filePath) {
        this.socket = socket;
        this.filePath = filePath;
    }
    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        PrintWriter fileWriter = null;

        try{
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            fileWriter = new PrintWriter(new FileWriter(filePath,true),true);

            // da mu ispratime hello na klientot
            String clientIp = socket.getInetAddress().getHostAddress();
            writer.println("HELLO"+clientIp);
            writer.flush();

            //primat poraka so hello
            String msgHello = reader.readLine();
            if(msgHello == null || !msgHello.startsWith("HELLO")){
                System.out.println("Nevalidna poraka, KRAJ");
                return;
            }
            //ako primi poraka so hello
            writer.println("SEND DAILY DATA");

            //sea trejt da ja primat datava so kje mu ja pratit klientov
            String dataLine = reader.readLine();
            if(dataLine == null || dataLine.split(",").length != 4){
                throw new IOException("Invalid data format");
            }
            writer.println("OK");
            //sea trejt da zapisvime vo fajl
            fileWriter.println(dataLine + "\n");
            fileWriter.flush();

            // za krajot
            String quitLine = reader.readLine();
            if(!quitLine.equals("QUIT")){
                System.out.println("Invalid quit");
            }

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(reader != null){
                    reader.close();
                }
                if(writer != null){
                    writer.close();
                }
                if(fileWriter != null){
                    fileWriter.close();
                }
                if(socket != null){
                    socket.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
