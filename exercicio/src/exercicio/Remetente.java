package exercicio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

public class Remetente {
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {

        String hora = LocalDateTime.now().toString();
        DatagramSocket remetente = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        sendData = hora.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        remetente.send(sendPacket);
        remetente.close();
    }
    
}
