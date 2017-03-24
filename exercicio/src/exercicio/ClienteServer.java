package exercicio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
        DatagramSocket socket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        System.out.println("Logar como admin(1) ou cliente(2)?");
        admin = sc.nextLine();
        boolean isAdmin = false;
        if(admin.equals("1")){
            System.out.println("Digite usuario e senha");       
            usuario = sc.nextLine();
            usuario = usuario+"-admin";
            senha = sc.nextLine();
            isAdmin = true;
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
        if(msg.equals("0")){
            System.out.println("Admin invalido");
            socket.close();
        }else{
            String welcome = msg;
            System.out.println(welcome);
            String[] welcomeComands = welcome.split(":");
            while(!decisao.equals("0")){
                System.out.println(welcomeComands[1]);            
                decisao = sc.nextLine();
                if(isAdmin){
                    switch (decisao){
                        case "0":
                            pacote2 = "cancel";
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,receivePacket.getPort());
                            socket.send(sendPacket);
                            break;
                        case "1":
                            pacote2 = "add-"; 
                            System.out.println("Digite o nome do livro");
                            pacote2 += sc.nextLine() + "-";
                            System.out.println("Digite o ano do livro");
                            pacote2 += sc.nextLine();
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                            socket.send(sendPacket);//envia pacote
                            break;
                        case "2":
                            System.out.println("Escolha o livro a ser deletado");
                            pacote2 = "delete";
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                            socket.send(sendPacket);
                            socket.receive(receivePacket);
                            msg = new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());

                            System.out.println(msg);
                            System.out.println("Escreva o ID do livro quer voce quer que seja deletado");
                            pacote2="";
                            pacote2 = sc.nextLine();
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
                            socket.send(sendPacket);
                            break;           
                        default:
                             System.out.println("Opção inválida");
                    }
                }else{
                    switch(decisao){
                         case "0":
                            pacote2 = "cancel";
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
                            socket.send(sendPacket);
                            break;
                        case "1":
                            pacote2 = "list";
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                            socket.send(sendPacket);
                            socket.receive(receivePacket);
                            msg = new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                            System.out.println(msg);
                            break;
                            
                        case "2":
                            System.out.println("Selecione o id do livro q quer emprestado");
                            pacote2="rent";
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                            socket.send(sendPacket);
                            pacote2="";
                            pacote2 = sc.nextLine();
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
                            socket.send(sendPacket);
                            break;
                            
                        case "3":
                            System.out.println("Selecione o id do livro q quer devolver");
                            pacote2="devolver";
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                            socket.send(sendPacket);
                            socket.receive(receivePacket);
                            msg = new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                            System.out.println(msg);
                            pacote2="";
                            pacote2 = sc.nextLine();
                            sendData = pacote2.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
                            socket.send(sendPacket);   
                            break;
                    }
                }

            }
        }

        
    }
    
}
