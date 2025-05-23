package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.Scanner;

public class SendThread extends Thread {
    private BufferedWriter writer;
    private PrintWriter logWriter;

    public SendThread(BufferedWriter writer, PrintWriter logWriter) {
        this.writer = writer;
        this.logWriter = logWriter;
    }
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            String msg;
            while ((msg = scanner.nextLine()) != null) {
                // ja prakjame porakata
                writer.write(msg);
                writer.newLine();
                writer.flush();
                //zapisvime vo fajlot
                logWriter.println(msg);
                logWriter.flush();
            }

        }catch (IOException e){
            throw new RuntimeException();
        }
    }
}
