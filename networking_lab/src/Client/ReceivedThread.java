package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ReceivedThread extends Thread {
    private BufferedWriter logWriter;
    private BufferedReader reader;
    public ReceivedThread(BufferedWriter logWriter, BufferedReader reader) {
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
                logWriter.write(msg);
                logWriter.newLine();
                logWriter.flush();
            }
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
}
