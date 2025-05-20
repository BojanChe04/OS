package TCP;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class WorkThread extends Thread {
    private Socket socket;
    private String filepath;
    public WorkThread(Socket socket, String filepath) {
        this.socket = socket;
        this.filepath = filepath;
    }
    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        PrintWriter fileWriter = null;
        try{
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            fileWriter = new PrintWriter(new FileWriter(filepath,true), true);

            String line = reader.readLine();
            if(!line.equals("HANDSHAKE")){
                writer.println("Invalid handshake");
                writer.flush();
                return;
            }
            writer.println("Logged in" + socket.getInetAddress().getHostAddress());
            writer.flush();
            String word_line;
            while ((word_line = reader.readLine()) != null) {
                if(word_line.equals("STOP")){
                    synchronized (TCPServer.lock){
                        System.out.println("Total: "+TCPServer.brojac);
                    }
                    writer.println("LOGGED OUT");
                    writer.flush();
                    break;
                }
                boolean flag;
                synchronized (TCPServer.lock){
                    flag = TCPServer.words.containsKey(word_line);
                    if(!flag){

                        TCPServer.words.put(word_line, socket.getInetAddress().getHostAddress());
                        TCPServer.brojac++;

                        //zapisvime vo fajlov
                        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        String ip = socket.getInetAddress().getHostAddress();
                        fileWriter.println(time + " " + ip + " " + word_line);
                    }
                }
                if(flag){
                    writer.println("IMA");
                    writer.flush();
                }else {
                    writer.println("NEMA");
                    writer.flush();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(reader != null) reader.close();
                if(writer != null) writer.close();
                if(fileWriter != null) fileWriter.close();
                if(socket != null) socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
