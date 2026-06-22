package modelo;

/**
 * Implementação DINÂMICA de pilha usando ListaEncadeada.
 * O topo da pilha corresponde ao PRIMEIRO elemento da lista.
 *
 * LIFO — Last In, First Out.
 */
public class PilhaLista<T> implements Pilha<T> {

    private ListaEncadeada<T> lista;

    // -------------------------------------------------------
    // Construtor
    // -------------------------------------------------------
    public PilhaLista() {
        lista = new ListaEncadeada<>();
    }

    // -------------------------------------------------------
    // push — inserção no início da lista = topo da pilha
    // -------------------------------------------------------
    @Override
    public void push(T info) {
        lista.inserir(info);
    }

    // -------------------------------------------------------
    // peek — consulta o topo (primeiro da lista)
    // -------------------------------------------------------
    @Override
    public T peek() {
        if (estaVazia()) {
            throw new PilhaVaziaException();
        }
        return lista.getPrimeiro().getInfo();
    }

    // -------------------------------------------------------
    // pop — remove e retorna o topo
    // -------------------------------------------------------
    @Override
    public T pop() {
        T valor = peek();
        lista.retirarValor(valor);
        return valor;
    }

    // -------------------------------------------------------
    // estaVazia
    // -------------------------------------------------------
    @Override
    public boolean estaVazia() {
        return lista.estaVazia();
    }

    // -------------------------------------------------------
    // liberar
    // -------------------------------------------------------
    @Override
    public void liberar() {
        while (!estaVazia()) {
            pop();
        }
    }

    // -------------------------------------------------------
    // toString — topo → base, separados por vírgula
    // -------------------------------------------------------
    @Override
    public String toString() {
        return lista.toString();
    }

    // -------------------------------------------------------
    // EXTRAS — prováveis na prova
    // -------------------------------------------------------

    /** Tamanho atual da pilha. */
    public int getTamanho() {
        return lista.obterComprimento();
    }

    /**
     * Busca valor na pilha.
     * @return posição a partir do topo (0 = topo) ou -1 se não encontrado.
     */
    public int buscar(T valor) {
        int pos = 0;
        var p = lista.getPrimeiro();
        while (p != null) {
            if (p.getInfo().equals(valor)) return pos;
            p = p.getProximo();
            pos++;
        }
        return -1;
    }
}
