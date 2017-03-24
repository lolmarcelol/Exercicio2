package exercicio;

import exercicio.cliente.Administrador;
import exercicio.cliente.Usuario;
import exercicio.livro.Livro;
import java.util.ArrayList;
import java.util.Iterator;

public class Gerenciador {

    private ArrayList<Usuario> clientes;
    private ArrayList<Administrador> admins;
    private ArrayList<Livro> livros;
    private int idUsuario;
    private int idLivro;
    private int idAdmin;

    public Gerenciador(ArrayList<Usuario> clientes,ArrayList<Administrador> admins,ArrayList<Livro> livros){
        this.clientes = clientes;
        this.admins = admins;
        this.livros = livros;
        this.idUsuario = 1;
        this.idLivro = 1;
        this.idAdmin =1;
    }

    public synchronized ArrayList<Usuario> getClientes() {
        return clientes;
    }

    public synchronized void setClientes(ArrayList<Usuario> clientes) {
        this.clientes = clientes;
    }

    public synchronized ArrayList<Administrador> getAdmins() {
        return admins;
    }

    public synchronized void setAdmins(ArrayList<Administrador> admins) {
        this.admins = admins;
    }

    public synchronized ArrayList<Livro> getLivros() {
        return livros;
    }

    public synchronized void setLivros(ArrayList<Livro> livros) {
        this.livros = livros;
    }

    public synchronized int getIdUsuario() {
        return idUsuario;
    }

    public synchronized void setIdUsuario() {
        this.idUsuario++;
    }

    public synchronized int getIdLivro() {
        return idLivro;
    }

    public synchronized void setIdLivro() {
        this.idLivro++;
    }

    public synchronized int getIdAdmin() {
        return idAdmin;
    }

    public synchronized void setIdAdmin() {
        this.idAdmin++;
    }
    
    public synchronized Administrador getAdministrador(ArrayList<Administrador>admins,String clienteNome){
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
        
    public synchronized Usuario getCliente(ArrayList<Usuario>usuarios,String clienteNome){
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
    
    public synchronized String getAllLivros(ArrayList<Livro>livros){
        synchronized(livros){
            String saida = "\t ID\t Nome\t Data\t Alugado\n";
            for(Iterator<Livro> iterator = livros.iterator();iterator.hasNext();){
                Livro livro = iterator.next();
                saida += "\t"+livro.getId()+"\t"+livro.getTitulo()+"\t"+livro.getAno()+"\t" +livro.isEmprestado()+"\n";        
            }
            return saida;
        }
    }
       
}
