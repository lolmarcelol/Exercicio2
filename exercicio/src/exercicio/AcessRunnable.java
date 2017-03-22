package exercicio;

import exercicio.cliente.Administrador;
import exercicio.cliente.Usuario;
import exercicio.livro.Livro;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcessRunnable implements Runnable {

    DatagramSocket serverSocket = null;
    DatagramSocket resposta = null;
    private final Gerenciador gerenciador;
    public AcessRunnable(DatagramSocket serverSocket, DatagramSocket resposta,Gerenciador gerenciador) {
        this.serverSocket = serverSocket;
        this.resposta = resposta;
        this.gerenciador = gerenciador;
    }

    static String getAllLivros(ArrayList<Livro>livros){
        synchronized(livros){
            String saida = "\t ID\t Nome\t Data\t Alugado\n";
            for(Iterator<Livro> iterator = livros.iterator();iterator.hasNext();){
                Livro livro = iterator.next();
                saida += "\t"+livro.getId()+"\t"+livro.getTitulo()+"\t"+livro.getAno()+"\t" +livro.isEmprestado()+"\n";        
            }
            return saida;
        }
    }
    
    
    static Usuario getCliente(ArrayList<Usuario>usuarios,String clienteNome){
        synchronized(usuarios){
            for(Iterator<Usuario> iterator = usuarios.iterator();iterator.hasNext();){
                Usuario usuario = iterator.next();
                if(usuario.getNome().equals(clienteNome)){
                    return usuario;
                }
            }
        return null;
        }
    }
    
    static Administrador getAdministrador(ArrayList<Administrador>admins,String clienteNome){
        synchronized(admins){
            for(Iterator<Administrador> iterator = admins.iterator();iterator.hasNext();){
                Administrador admin = iterator.next();
                if(admin.getNome().equals(clienteNome)){
                    return admin;
                }
            }
        }
        return null;
    }
    @Override
    public void run() {
        try {

            InetAddress IPAddress = InetAddress.getByName("localhost");
            DatagramSocket resposta = new DatagramSocket();

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            String mensagem1;           
            while(true){
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String( receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());
                System.out.println("RECEIVED: " + sentence);
                
                String[] usuarioSenha = sentence.split("-");
                String usuarioNome = usuarioSenha[0]; // usuario
                Usuario usuario = null;
                Administrador admin = null;
                if(usuarioSenha[1].equals("admin")){
                    //String senha = usuarioSenha[2];
                    admin = getAdministrador(gerenciador.getAdmins(),usuarioNome);
                    if(admin!=null){
                        mensagem1 = "\tBEM VINDO ADMIN : \n Digite 1 para adicionar livros ou 2 para deletar livros ou 0 para sair";
                    }else{
                        mensagem1="0";
                    }
                }else{
                    usuario = getCliente(gerenciador.getClientes(),usuarioNome);
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
                resposta.send(sendPacket);
                if(!mensagem1.equals("0")){
                    //logica pós login
                    String acao = "";
                    while(!acao.equals("cancel")){
                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
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
                                sendData = getAllLivros(gerenciador.getLivros()).getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                                resposta.send(sendPacket);
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
                                sendData = getAllLivros(gerenciador.getLivros()).getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                                resposta.send(sendPacket);
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
                                sendData = getAllLivros(usuario.getLivros()).getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());//cria pacote
                                resposta.send(sendPacket);
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
    
    
    

