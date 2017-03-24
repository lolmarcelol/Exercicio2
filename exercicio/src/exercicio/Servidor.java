package exercicio;

import exercicio.cliente.Administrador;
import exercicio.cliente.Usuario;
import exercicio.livro.Livro;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

public class Servidor {       
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {
            ArrayList<Usuario> clientes = new ArrayList<Usuario>();
            ArrayList<Administrador> admins = new ArrayList<Administrador>();

            DatagramSocket serverSocket = new DatagramSocket(9876);
            ArrayList<Livro> livros = new ArrayList<Livro>();
            Gerenciador gerenciador = new Gerenciador(clientes,admins,livros);

            byte[] receiveData = new byte[1024];
            
            gerenciador.getLivros().add(new Livro(gerenciador.getIdLivro(),"livro1",1999));
            gerenciador.setIdLivro();
            gerenciador.getLivros().add(new Livro(gerenciador.getIdLivro(),"livro2",1923));
            gerenciador.setIdLivro();
            gerenciador.getLivros().add(new Livro(gerenciador.getIdLivro(),"livro3",1955));
            gerenciador.setIdLivro();
            gerenciador.getClientes().add(new Usuario(gerenciador.getIdUsuario(),"usuario","usuario"));
            gerenciador.getAdmins().add(new Administrador(gerenciador.getIdAdmin(),"marcelo","marcelo"));
            
            while(true){
                DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(packet);
                System.out.println("Nova conexao");
                new Thread(new AcessRunnable(packet,gerenciador)).start();
            }

    }  
}
