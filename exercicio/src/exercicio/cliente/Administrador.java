package exercicio.cliente;

import exercicio.livro.Livro;
import java.util.ArrayList;
import java.util.Iterator;

public class Administrador extends Cliente {
    public boolean administrador;
  
    public Administrador(int id,String nome,String senha){
        super(id,senha,nome);
        administrador = true;
    }
    
    public Livro criarLivro(int id,String titulo,int ano){
         Livro livro = new Livro(id,titulo,ano);
         return livro;
    }
    
     public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }
    public ArrayList<Livro> removerLivro(ArrayList<Livro> livros,int id){
        Livro delete = null;
        for(Iterator<Livro> iterator = livros.iterator();iterator.hasNext();){
            Livro livro = iterator.next();
            if(livro.getId()==id){
                delete = livro;
            }
        }
        livros.remove(delete);
        return livros;
    }
    
    
}
