package Client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {
    private String serverName;
    private int serverPort;

    private DatagramSocket socket;
    private InetAddress address;
    private String message;
    private byte[] buffer;

    public UDPClient(String serverName, int serverPort,String message) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.message = message;
        try{
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(serverName);
        }catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try{
        byte[] sendBuffer = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, serverPort);

            socket.send(sendPacket);

        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            socket.receive(receivePacket);

        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Odgovor od serverot: " + response);
        socket.close();
    }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UDPClient udpClient = new UDPClient("194.149.135.49", 9753, "233090");
        udpClient.start();
    }
}
