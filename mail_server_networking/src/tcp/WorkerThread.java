package tcp;

import java.io.*;
import java.net.Socket;

public class WorkerThread extends Thread {
    private int port;
    private Socket socket;
    private String filePath;

    public WorkerThread(Socket socket, String filePath) {
        this.socket = socket;
        this.filePath = filePath;
    }
    private boolean isValidMail(String mail) {
        return mail != null && mail.contains("@");
    }
    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        PrintWriter fileWriter = null;

        try{
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            fileWriter = new PrintWriter(new FileWriter(filePath,true),true);

            //1. mu prakjame poraka na klientnt
            String clientIp = socket.getInetAddress().getHostAddress();
            writer.println("START " + clientIp);
            writer.flush();

            //2. mail_to
            String mail_to_line = reader.readLine();
            if(mail_to_line==null || !mail_to_line.startsWith("MAIL TO:")){
                throw new IOException("Pogresna poraka.... KRAJ");
            }
            //3. za validna email adresa
            String mail_adresa = mail_to_line.substring("MAIL TO: ".length()).trim();
            if(!isValidMail(mail_adresa)){
                writer.println("INVALID E-MAIL");
                writer.flush();
                return;
            }
            writer.println("TNX");
            writer.flush();

            //4. mail from dobivat
            String mail_from_line = reader.readLine();
            if(mail_from_line==null || !mail_from_line.startsWith("MAIL FROM:")){
                throw new IOException("Pogresna poraka.... KRAJ");
            }
            String mail_from = mail_from_line.substring("MAIL FROM: ".length()).trim();
            if(!isValidMail(mail_from)){
                writer.println("INVALID E-MAIL");
                writer.flush();
                return;
            }
            else{
                writer.println("OK");
                writer.flush();
            }
            //6. mail so CC
            String cc_line = reader.readLine();
            if(cc_line==null || !cc_line.startsWith("CC:")){
                throw new IOException("Pogresna poraka.... KRAJ");
            }
            String mail_cc = cc_line.substring("CC: ".length()).trim();
            //7.
            writer.println("RECEIVED: "+"<"+mail_adresa+" "+mail_cc+">");
            //8. prakjat data sea klientov linija po linija
            String data_line;
            while((data_line=reader.readLine())!=null && !data_line.contains("?")){

                synchronized (TCPServer.lock){
                    fileWriter.println(data_line);
                    fileWriter.flush();
                    TCPServer.brojac++;
                }
            }
            writer.println("RECEIVED total: " + TCPServer.brojac);
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (fileWriter != null) fileWriter.close();
                if (socket != null) socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
