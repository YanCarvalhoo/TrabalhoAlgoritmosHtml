package controle;

/**
 * Representa uma linha da tabela de frequência de tags:
 * nome da tag, quantas vezes apareceu, tipo (normal/singleton)
 * e a linha da primeira ocorrência.
 */
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

    public String getNome() { return nome; }
    public int getFrequencia() { return frequencia; }
    public TipoTag getTipo() { return tipo; }
    public int getPrimeiraLinha() { return primeiraLinha; }

    /**
     * Ordenação alfabética pelo nome da tag (usada pelo MergeSort).
     */
    @Override
    public int compareTo(TagFrequencia outra) {
        return this.nome.compareTo(outra.nome);
    }
}
