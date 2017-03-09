package exercicio.cliente;

import exercicio.Livro;
import java.util.ArrayList;

public class Administrador extends Cliente {
    
    public Administrador(int id,String nome,String senha){
        super(id,senha,nome);
    }
    
    public Livro criarLivro(int id,String titulo,int ano){
         Livro livro = new Livro(id,titulo,ano);
         return livro;
    }
    
    public ArrayList<Livro> removerLivro(ArrayList<Livro> livros,Livro livro){
        for(Livro l:livros){
            if(l.getId() == livro.getId()){
                livros.remove(livro);
            }
        }
        return livros;
    }
    
    
}
