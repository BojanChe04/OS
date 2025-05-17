package tcp.server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WorkerThred extends Thread {
    private Socket socket;
    private String agrFile;
    private String counterFile;
    Lock fileLock;// da se osigura deka samo eden thread citat ili picuvat vo counterFile
    public WorkerThred(Socket socket, String agrFile, String counterFile) {
        this.socket = socket;
        this.agrFile = agrFile;
        this.counterFile = counterFile;//total lines counted
        fileLock = new ReentrantLock();
    }
    @Override
    public void run() {
        BufferedReader reader = null;//ctat od klient
        BufferedWriter writer = null;// do klient poraaki
        BufferedWriter agrWriter = null;//dodava logovi vo agrfile
        RandomAccessFile raf = null;// citat i pisuvat vo counterfile
        try{
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            agrWriter = new BufferedWriter(new FileWriter(agrFile,true));
            raf = new RandomAccessFile(new File(counterFile), "rw");

            System.out.println("Worker thread running...");
            String line = reader.readLine();
            System.out.println("Client sent: " + line);

            if (!line.equals("hello")) {
                writer.write("Close\n");
                writer.flush();
                writer.close();
                reader.close();
                agrWriter.close();
                raf.close();
                return;
            }
            writer.write("Send file\n");
            writer.flush();


            // citame podatoci i sobitame poeni
            int logTotalPoints=0;
            int totalLines=0;

            reader.readLine();// go skokame header
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    logTotalPoints += Integer.parseInt(parts[3]);
                    totalLines++;
                }
            }
            //ova zapisvit vo agrfile
            agrWriter.write(String.format("Client %s, Total points: %s\n", socket.getInetAddress().getHostAddress(), logTotalPoints));
            agrWriter.flush();
            agrWriter.close();

            // azuriranje na brojot na obraboteni linii
            fileLock.lock();
            long lineCounter=0;
            try {

                lineCounter = raf.readLong();//ja cita prethodnava vrednost na linii
            }catch (IOException e) {
                e.printStackTrace();
            }
            raf.seek(0);
            raf.writeLong(lineCounter + totalLines);//ja zapisvit novata vrednsot
            raf.close();
            fileLock.unlock();

            writer.write("Total points: " + logTotalPoints + "\n");
            writer.flush();
            writer.close();
            raf.close();
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
