package udp.client;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {

    private String serverName;
    private String serverPort;

    private DatagramSocket socket;
    private InetAddress address;
    private String message;
    private byte[] buffer;

    public UDPClient(String serverName, String serverPort, String message) {
        //konverzija od InetAddress address vo String
        this.serverName =serverName;
        this.serverPort = serverPort;
        this.message = message;

        //konverzija od InetAddress address vo String
        try{
            this.socket=new DatagramSocket();
            this.address = InetAddress.getByName(serverName);
        }catch(SocketException | UnknownHostException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Integer.parseInt(serverPort));
        try{
            socket.send(packet);//go prakjame paketot do server
            //da procitame to so sme dobile od strana na serverot
            packet = new DatagramPacket(buffer, buffer.length, address, Integer.parseInt(serverPort));
            socket.receive(packet);
            System.out.println(new String(packet.getData(), 0, packet.getLength()));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UDPClient client = new UDPClient("localhost", "4445", "Hola Senorita");
        client.start();
    }
}
