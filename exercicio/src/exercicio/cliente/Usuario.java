package exercicio.cliente;

import exercicio.Livro;
import java.util.ArrayList;

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
    
    public void consultaTodosLivros(ArrayList<Livro>livros){
        for(Livro livro:livros)
            System.out.println(livro.getTitulo() + " - " + livro.getAno());

    }
    
    public boolean emprestar(ArrayList<Livro>livros,Livro livro){
        for(Livro l:livros){
            if(l.getId() == livro.getId() && l.isEmprestado() == false){
                l.setIsEmprestado(true);
                return true;
            }
        }
        return false;
    }
    public boolean devovler(ArrayList<Livro>livros,Livro livro){
        for(Livro l:livros){
            if(l.getId() == livro.getId() && l.isEmprestado() == true){
                l.setIsEmprestado(false);
                return true;
            }
        }
        return false;
        
    }
    
}
