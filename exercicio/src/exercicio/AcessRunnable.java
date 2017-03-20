package exercicio;

import exercicio.cliente.Usuario;
import exercicio.livro.Livro;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcessRunnable implements Runnable {

    DatagramSocket serverSocket = null;
    DatagramSocket resposta = null;

    public AcessRunnable(DatagramSocket serverSocket, DatagramSocket resposta) {
        this.serverSocket = serverSocket;
        this.resposta = resposta;
    }
    
     
    public String getAllLivros(ArrayList<Livro>livros){
        String saida = "\t ID\t Nome\t Data\n";
        for(Livro l : livros){
            saida += "\t"+l.getId()+"\t"+l.getTitulo()+"\t"+l.getAno()+"\n";
        }
        return saida;
    }
    
    public void deleteLivro(ArrayList<Livro>livros,int idDelete){
        for(Livro l: livros){
            if(l.getId()==idDelete){
                livros.remove(l);
            }
        }
    }

    public void run() {
        try {
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
            
            livros.add(new Livro(1,"livro1",1999));
            livros.add(new Livro(2,"livro2",1923));
            livros.add(new Livro(3,"livro3",1955));
            
            
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
                    mensagem1 = "\tBEM VINDO ADMIN : \n Digite 1 para adicionar livros ou 2 para deletar livros ou 0 para sair";
                }else{
                    String senha = usuarioSenha[1];
                    mensagem1 = "\tBEM VINDO usuario : \n Digite 1 para consultar lista de livros ou 2 para realizar emprestimo ou digite 3 para realizar devolucao ou 0 para sair";
                }
                sendData = mensagem1.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                resposta.send(sendPacket);
                //logica pós login
                String acao = "";
                while(!acao.equals("cancel")){
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    sentence = new String( receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                    System.out.println("livro: " + sentence);
                    String[] acaoUsuario = sentence.split("-");
                    acao = acaoUsuario[0];
                    // fazer o switch rolar decentemente
                    switch(acao){
                        case "add":
                            Livro livro = new Livro(id,acaoUsuario[1],Integer.parseInt(acaoUsuario[2]));
                            id++;
                            livros.add(livro);
                            break;
                        case "delete":
                            System.out.println("ESSE É O PRINT");
                            System.out.println(getAllLivros(livros));
                            sendData = getAllLivros(livros).getBytes();
                            System.out.println("CABO O PRINT");
                            System.out.println(sendData);
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                            resposta.send(sendPacket);
                            receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            serverSocket.receive(receivePacket);
                            sentence = new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                            int idDelete = Integer.parseInt(sentence);
                            deleteLivro(livros,idDelete);
                            break;

                    }
                }
                
            }
        } catch (SocketException ex) {            
            Logger.getLogger(AcessRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AcessRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    
    
    

