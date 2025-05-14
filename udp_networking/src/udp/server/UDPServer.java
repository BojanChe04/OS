package udp.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer extends Thread {

    //sega namesto soket koristime Datagram
    private DatagramSocket socket;
    private byte[] buffer = new byte[256];

    public UDPServer(int port) {
        try {
            socket = new DatagramSocket(port);
        }catch(SocketException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        //kaj udp najceto se razmenuvaat datagrami
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);//koj bafer sakame da go koristime i negovata golemina
        while(true) {
            try {
                //paketive so gi primvit soketov gi primat preku metodava receave
                socket.receive(packet);
                String recaved = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received" + recaved);//pecatit to so primil
                InetAddress inetAddress = packet.getAddress();
                int port = packet.getPort();

                packet = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                socket.send(packet);// za da mu pratime podatoci na klientot send metod
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        UDPServer udpServer = new UDPServer(4445);
        udpServer.start();
    }
}
