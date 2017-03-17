package exercicio;

import exercicio.cliente.Usuario;
import exercicio.livro.Livro;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Servidor {
    
    static String getAllLivros(ArrayList<Livro>livros){
        String saida = "\t ID\t Nome\t Data";
        for(Livro l : livros){
            saida += l.getId()+"\t"+l.getTitulo()+"\t"+l.getAno();
        }
        return saida;
    }
    
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        DatagramSocket serverSocket = new DatagramSocket(9876);
        InetAddress IPAddress = InetAddress.getByName("localhost");
        DatagramSocket resposta = new DatagramSocket();
        ArrayList<Livro> livros = new ArrayList<Livro>();
        int id =0;
        byte[] receiveData = new byte[1024];
        boolean isAdmin;
        byte[] sendData = new byte[1024];
        String mensagem1;
        while(true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
            System.out.println("RECEIVED: " + sentence);

            String[] usuarioSenha = sentence.split("-");
            String usuario = usuarioSenha[0]; // usuario
            if(usuarioSenha[1].equals("admin")){
                isAdmin = true;
                String senha = usuarioSenha[2];
                mensagem1 = "\tBEM VINDO ADMIN\n Digite 1 para adicionar livros ou 2 para deletar livros ou 0 para sair";
            }else{
                String senha = usuarioSenha[1];
                mensagem1 = "\tBEM VINDO usuario\n Digite 1 para consultar lista de livros ou 2 para realizar emprestimo ou digite 3 para realizar devolucao ou 0 para sair";
            }
            sendData = mensagem1.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
            resposta.send(sendPacket);
            //logica pós login
         
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence2 = new String( receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
            System.out.println("livro: " + sentence2);
            String[] acaoUsuario = sentence.split("-");
            String acao = acaoUsuario[0];
            // fazer o switch rolar decentemente
            switch(acao){
                case "add":
                    Livro livro = new Livro(id,acaoUsuario[1],Integer.parseInt(acaoUsuario[2]));
                    id++;
                    livros.add(livro);
                    break;
                case "delete":
                    sendData = getAllLivros(livros).getBytes();
                    System.out.println(sendData);
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                    resposta.send(sendPacket);
                    
                    break;
                    
            }
            
        }
    }
    
}
