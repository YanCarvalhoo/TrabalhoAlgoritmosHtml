package modelo;

public class NoArvore<T> {

    private T info;
    private NoArvore<T> primeiro;
    private NoArvore<T> proximo;

    public NoArvore(T info) {
        this.info = info;
        primeiro = null;
        proximo = null;
    }
    
    public void InserirFilho(NoArvore<T> sa)
    {
    	sa.setProximo(primeiro);
    	setPrimeiro(sa);
    }

    public T getInfo() { return info; }
    public void setInfo(T info) { this.info = info; }

    public NoArvore<T> getPrimeiro() { return primeiro; }
    public void setPrimeiro(NoArvore<T> esquerda) { this.primeiro = esquerda; }

    public NoArvore<T> getProximo() { return proximo; }
    public void setProximo(NoArvore<T> direita) { this.proximo = direita; }
}
