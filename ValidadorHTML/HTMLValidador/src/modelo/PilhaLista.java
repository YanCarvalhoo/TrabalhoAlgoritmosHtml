package modelo;


public class PilhaLista<T> implements Pilha<T> {

    private ListaEncadeada<T> lista;

    public PilhaLista() {
        lista = new ListaEncadeada<>();
    }

    @Override
    public void push(T info) {
        lista.inserir(info);
    }

    @Override
    public T peek() {
        if (estaVazia()) 
        {
            throw new PilhaVaziaException();
        }
        return lista.getPrimeiro().getInfo();
    }


    @Override
    public T pop() {
        T valor = peek();
        lista.retirarValor(valor);
        return valor;
    }

    @Override
    public boolean estaVazia() {
        return lista.estaVazia();
    }

    @Override
    public void liberar() {
        while (!estaVazia()) {
            pop();
        }
    }

    @Override
    public String toString() {
        return lista.toString();
    }

    public int getTamanho() {
        return lista.obterComprimento();
    }

    public int buscar(T valor) {
        int pos = 0;
        var p = lista.getPrimeiro();
        while (p != null) 
        {
            if (p.getInfo().equals(valor)) return pos;
            p = p.getProximo();
            pos++;
        }
        return -1;
    }
}
