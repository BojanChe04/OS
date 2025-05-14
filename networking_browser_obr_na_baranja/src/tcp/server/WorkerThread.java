package tcp.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class WorkerThread extends Thread {

    private Socket socket = null;

    public WorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run(){
        //socket na nivo na os e obicen file
        //ako sakame da citame i zapisvime podatoci mojme da koristime strimovi
        //zato gi kreirame ovie, za da ni ja ovozmozet komunikacijata
        BufferedReader reader = null;
        PrintWriter writer = null;

        try{
            System.out.printf("Connected:%s:%d\n",socket.getInetAddress(),socket.getPort());

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            //obicna komunikacija klient-server kje naprajme

            String line = null;
            while ((line = reader.readLine()) != null) {

                //pecatime so ni pratil klientot
                System.out.println(line);
                writer.write(line);//eho prajme
                writer.flush();//morat sekogas na kraj da si stavame za da se transmitova porakat
            }
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
            if(reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(writer != null){
                writer.close();
            }
        }
    }
}