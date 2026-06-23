package controle;

/**
 * Representa uma tag encontrada no arquivo HTML.
 * É o tipo usado tanto nos nós da Arvore (hierarquia) quanto
 * nos elementos empilhados na Pilha (validação de abertura/fechamento).
 */
public class TagInfo {

    private String nome;
    private int linha;
    private TipoTag tipo;

    public TagInfo(String nome, int linha, TipoTag tipo) {
        this.nome = nome;
        this.linha = linha;
        this.tipo = tipo;
    }

    public String getNome() { return nome; }
    public int getLinha() { return linha; }
    public TipoTag getTipo() { return tipo; }

    @Override
    public String toString() {
        return nome;
    }
}
