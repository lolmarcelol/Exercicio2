package exercicio;

public class Livro {

    private int id;
    private String titulo;
    private int ano;
    private boolean isEmprestado;
    
    public Livro(int id,String titulo,int ano){
        this.id = id;
        this.titulo = titulo;
        this.ano = ano;
        this.isEmprestado = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public boolean isEmprestado() {
        return isEmprestado;
    }

    public void setIsEmprestado(boolean isEmprestado) {
        this.isEmprestado = isEmprestado;
    }
    
}
