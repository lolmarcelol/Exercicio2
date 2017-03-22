package exercicio;

import exercicio.cliente.Administrador;
import exercicio.cliente.Usuario;
import exercicio.livro.Livro;
import java.util.ArrayList;

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

    public ArrayList<Usuario> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<Usuario> clientes) {
        this.clientes = clientes;
    }

    public ArrayList<Administrador> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<Administrador> admins) {
        this.admins = admins;
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public void setLivros(ArrayList<Livro> livros) {
        this.livros = livros;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario() {
        this.idUsuario++;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro() {
        this.idLivro++;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin() {
        this.idAdmin++;
    }
    
       
}
