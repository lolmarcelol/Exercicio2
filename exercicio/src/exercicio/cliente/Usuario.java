package exercicio.cliente;

import exercicio.livro.Livro;
import java.util.ArrayList;
import java.util.Iterator;

public class Usuario extends Cliente {
    private ArrayList<Livro> livros;
    
    public Usuario(int id,String nome,String senha){
        super(id,nome,senha);
        livros = new ArrayList<Livro>();
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public void setLivros(ArrayList<Livro> livros) {
        this.livros = livros;
    }
    
    public boolean emprestar(ArrayList<Livro>livros,int id){
        for(Iterator<Livro> iterator = livros.iterator();iterator.hasNext();){
            Livro livro = iterator.next();
            if(livro.getId()==id && livro.isEmprestado() == false){
                livro.setIsEmprestado(true);
                getLivros().add(livro);
                livro.setIdUsuario(getId());
                return true;
            }
        }
        return false;
    }
    public boolean devovler(int id){
        for(Iterator<Livro> iterator = livros.iterator();iterator.hasNext();){
            Livro livro = iterator.next();
            if(livro.getId()==id && livro.isEmprestado() == true){
                livro.setIsEmprestado(false);
                getLivros().remove(livro);
                livro.setIdUsuario(getId());
                return true;
            }
        }
        return false;
        
    }
    
}
