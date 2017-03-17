package exercicio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ClienteServer {
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {
        String usuario;
        String senha;
        String pacote1;
        String pacote2 = "";
        String admin;
        String decisao = "null";
        byte[] receiveData = new byte[1024];
        Scanner sc = new Scanner(System.in);
        String hora = LocalDateTime.now().toString();
        DatagramSocket socket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        System.out.println("Logar como admin(1) ou cliente(2)?");
        admin = sc.nextLine();
        if(admin.equals("1")){
            System.out.println("Digite usuario e senha");       
            usuario = sc.nextLine();
            usuario = usuario+"-admin";
            senha = sc.nextLine();
        }else{
            System.out.println("Digite usuario e senha");       
            usuario = sc.nextLine();
            senha = sc.nextLine();
        
        }
        pacote1 = usuario+"-"+senha;
        sendData = pacote1.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);//cria pacote
        socket.send(sendPacket);//envia pacote
        // recebe resposta
        DatagramPacket receivePacket = new DatagramPacket(new byte[1024],1024);
        socket.receive(receivePacket);
        String msg = new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
        // troca de mensagens
        
        while(!decisao.equals("0")){
            System.out.println(msg);
            decisao = sc.nextLine();
            switch (decisao) {
            case "1":
                pacote2 = "add-"; 
                System.out.println("Digite o nome do livro");
                pacote2 += sc.nextLine() + "-";
                System.out.println("Digite o ano do livro");
                pacote2 += sc.nextLine();
                sendData = pacote2.getBytes();
                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);//cria pacote
                socket.send(sendPacket);//envia pacote
                break;
            case "2":
                System.out.println("Escolha o livro a ser deletado");
                pacote2 = "delete";
                sendData = pacote2.getBytes();
                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);//cria pacote
                socket.send(sendPacket);
                socket.receive(receivePacket);
                msg = new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                System.out.println(msg);
                break;
            default:
                 System.out.println("Tchau !");
            }
            System.out.println(pacote2);
        }
        
    }
    
}
