package tcp.server;

import java.io.*;
import java.net.Socket;

import static tcp.server.TCPServer.increment;

public class WorkerThread extends Thread {
    private Socket socket = null;
    private String logFilePath;
    public WorkerThread(Socket socket, String logFilePath) {
        this.socket = socket;
        this.logFilePath = logFilePath;
    }
    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        PrintWriter logWriter = null;

        try {
            System.out.printf("Connected:%s:%d\n",socket.getInetAddress(),socket.getPort());

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            logWriter = new PrintWriter(new FileWriter(logFilePath, true), true);
            //log file

            //read form client
            String line = null;
            while ((line = reader.readLine()) != null) {
                if(line.equals("LOGIN")){
                    writer.println("LOGIN IN CLIENT: " + socket.getInetAddress().getHostAddress());
                    writer.flush();
                }else if (line.contains("LOGOUT")){
                    writer.println("LOGGED OUT IN CLIENT: " +socket.getInetAddress().getHostAddress());
                    writer.flush();
                    logWriter.println(socket.getInetAddress().getHostAddress() + " " +TCPServer.getTotalMessages()+ "\n");
                    logWriter.flush();
                    break;
                }
                else{
                    increment();
                    writer.println("Message in client " + socket.getInetAddress().getHostAddress() + " : " + line);
                    writer.flush();
                    logWriter.println(socket.getInetAddress().getHostAddress() + " " + "\n");
                    logWriter.flush();
                }
            }
            socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (logWriter != null) logWriter.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
