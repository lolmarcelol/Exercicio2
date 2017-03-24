package exercicio;

import exercicio.cliente.Administrador;
import exercicio.cliente.Usuario;
import exercicio.livro.Livro;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerFixo{

    DatagramSocket serverSocket = null;
    private final Gerenciador gerenciador;
    DatagramPacket receivePacket;
    public ServerFixo(DatagramPacket receivePacket,Gerenciador gerenciador) {
        this.receivePacket = receivePacket;
        this.gerenciador = gerenciador;
    }
    
    public void run() {
        try {

            InetAddress IPAddress = InetAddress.getByName("localhost");
            DatagramSocket socketThread = new DatagramSocket();

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            String mensagem1; 
            
            while(true){
                //DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                //serverSocket.receive(receivePacket);
                String sentence = new String( receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                System.out.println("RECEIVED: " + sentence);
                
                String[] usuarioSenha = sentence.split("-");
                String usuarioNome = usuarioSenha[0]; // usuario
                Usuario usuario = null;
                Administrador admin = null;
                if(usuarioSenha[1].equals("admin")){
                    //String senha = usuarioSenha[2];
                    admin = gerenciador.getAdministrador(gerenciador.getAdmins(),usuarioNome);
                    if(admin!=null){
                        mensagem1 = "\tBEM VINDO ADMIN : \n Digite 1 para adicionar livros ou 2 para deletar livros ou 0 para sair";
                    }else{
                        mensagem1="0";
                    }
                }else{
                    usuario = gerenciador.getCliente(gerenciador.getClientes(),usuarioNome);
                    String senha = usuarioSenha[1];
                    if(usuario !=null){
                        mensagem1 = "\tBEM VINDO NOVAMENTE: "+usuarioNome+" : \n Digite 1 para consultar lista de livros ou 2 para realizar emprestimo ou digite 3 para realizar devolucao ou 0 para sair";
                    }else{
                        usuario = new Usuario(gerenciador.getIdUsuario(),usuarioNome,senha);
                        synchronized(gerenciador){
                            gerenciador.setIdUsuario();
                            gerenciador.getClientes().add(usuario);    
                        }
                        mensagem1 = "\tBEM VINDO,"+usuarioNome+" seu usuario foi criado automaticamente: \n Digite 1 para consultar lista de livros ou 2 para realizar emprestimo ou digite 3 para realizar devolucao ou 0 para sair";
                    }
                }
                
                sendData = mensagem1.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                socketThread.send(sendPacket);
                if(!mensagem1.equals("0")){
                    //logica pós login
                    String acao = "";
                    while(!acao.equals("cancel")){
                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socketThread.receive(receivePacket);
                        sentence = new String( receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                        System.out.println("Action: " + sentence);
                        String[] acaoUsuario = sentence.split("-");
                        acao = acaoUsuario[0];
                        // fazer o switch rolar decentemente
                        switch(acao){
                            case "add":
                                Livro livro = new Livro(gerenciador.getIdLivro(),acaoUsuario[1],Integer.parseInt(acaoUsuario[2]));
                                synchronized(gerenciador){
                                    gerenciador.setIdLivro();
                                    gerenciador.getLivros().add(livro);  
                                }    
                                break;
                            case "delete":
                                sendData = gerenciador.getAllLivros(gerenciador.getLivros()).getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                                socketThread.send(sendPacket);
                                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                                serverSocket.receive(receivePacket);
                                sentence = new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                                int idDelete = Integer.parseInt(sentence);
                                synchronized(gerenciador){
                                    gerenciador.setLivros(admin.removerLivro(gerenciador.getLivros(),idDelete));
                                }
                                break;
                            case "cancel":
                                System.out.println("Deslogou");
                                break;
                            case "list":
                                sendData = gerenciador.getAllLivros(gerenciador.getLivros()).getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                                socketThread.send(sendPacket);
                                break;
                            case "rent":
                                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                                serverSocket.receive(receivePacket);
                                sentence = new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                                int idFind = Integer.parseInt(sentence);
                                synchronized(gerenciador.getLivros()){
                                    usuario.emprestar(gerenciador.getLivros(), idFind);
                                }
                                
                                break;
                            case "devolver":
                                sendData = gerenciador.getAllLivros(usuario.getLivros()).getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                                socketThread.send(sendPacket);
                                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                                serverSocket.receive(receivePacket);
                                sentence = new String(receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                                int idDevolver = Integer.parseInt(sentence);
                                usuario.devovler(idDevolver);
                                break;
                        }
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
    
    
    

