package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReceivedThread extends Thread {
    private PrintWriter logWriter;
    private BufferedReader reader;
    public ReceivedThread(PrintWriter logWriter, BufferedReader reader) {
        this.logWriter = logWriter;
        this.reader = reader;
    }
    @Override
    public void run() {
        try{
            String msg;
            //tuka citame od server povraten odgovor
            while ((msg = reader.readLine())!=null){
                System.out.println(msg);
                //zapisvime vo fajl
                logWriter.println(msg);
                logWriter.flush();
            }
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
}
