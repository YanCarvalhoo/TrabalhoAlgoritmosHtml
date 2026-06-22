package modelo;

/**
 * Lista encadeada simples, com inserção sempre no início.
 * Implementada porque a PilhaLista.java fornecida pela equipe depende
 * desta classe, que não estava entre os arquivos enviados.
 */
public class ListaEncadeada<T> {

    private NoLista<T> primeiro;
    private int comprimento;

    public ListaEncadeada() {
        primeiro = null;
        comprimento = 0;
    }

    public void inserir(T info) {
        NoLista<T> novo = new NoLista<>(info);
        novo.setProximo(primeiro);
        primeiro = novo;
        comprimento++;
    }

    public NoLista<T> getPrimeiro() {
        return primeiro;
    }

    public boolean estaVazia() {
        return primeiro == null;
    }

    public int obterComprimento() {
        return comprimento;
    }

    public void retirarValor(T valor) {
        if (estaVazia()) {
            return;
        }

        if (primeiro.getInfo().equals(valor)) {
            primeiro = primeiro.getProximo();
            comprimento--;
            return;
        }

        NoLista<T> anterior = primeiro;
        NoLista<T> atual = primeiro.getProximo();
        while (atual != null) {
            if (atual.getInfo().equals(valor)) {
                anterior.setProximo(atual.getProximo());
                comprimento--;
                return;
            }
            anterior = atual;
            atual = atual.getProximo();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        NoLista<T> p = primeiro;
        while (p != null) {
            sb.append(p.getInfo());
            if (p.getProximo() != null) {
                sb.append(",");
            }
            p = p.getProximo();
        }
        return sb.toString();
    }
}
