package controle;

public class TagFrequencia implements Comparable<TagFrequencia> {

    private String nome;
    private int frequencia;
    private TipoTag tipo;
    private int primeiraLinha;

    public TagFrequencia(String nome, TipoTag tipo, int primeiraLinha) {
        this.nome = nome;
        this.tipo = tipo;
        this.primeiraLinha = primeiraLinha;
        this.frequencia = 0;
    }

    public void incrementar() {
        frequencia++;
    }

    public String getNome() { 
    	return nome; 
    }
    
    public int getFrequencia() { 
    	return frequencia; 
    }
    
    public TipoTag getTipo() { 
    	return tipo; 
    }
    
    public int getPrimeiraLinha() { 
    	return primeiraLinha; 
    }

    @Override
    public int compareTo(TagFrequencia outra) {
        return this.nome.compareTo(outra.nome);
    }
}
