package tcp.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpWorkerThread extends Thread{

    private Socket socket;
    public HttpWorkerThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader in = null; //citame od browser
        PrintWriter out = null;//vrakjame odg

        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line = null;

            StringBuilder builder = new StringBuilder();
            while((line = in.readLine()) != null && !line.isEmpty()){
                //imat mn podatoci so trejt da gi zacuvame vo string bilder
                builder.append(line).append("\n");
                System.out.println(line);
            }
            //kreirame samo nov request sega od ko go napolanvme bilderov
            ReuestProcessor reuestProcessor = ReuestProcessor.of(builder.toString().split("\r\n"));
            //za da gi ni ja otvorit strtanava na browser morat ova da go ispisime
            out.write("HTTP/1.1 200 OK\n\n");
            //provervime
            if(reuestProcessor.getCommand().equals("GET") && reuestProcessor.getUri().equals("/time")){
                out.printf("<html><body><h1>%s</h1></body></html>", LocalDateTime.now().format(DateTimeFormatter.ISO_TIME));
            }else{
                out.printf("<html><body><h1>HOLA SENORITA</h1></body></html>");
            }
            out.flush();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(in != null){
                try {
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(out != null){
                out.close();
            }
        }
    }
}
