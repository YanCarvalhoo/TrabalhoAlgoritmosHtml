package modelo;


public class FilaVetor<T> implements Fila<T> {

    private T[] info;
    private int limite;
    private int tamanho;
    private int inicio;

    @SuppressWarnings("unchecked")
    public FilaVetor(int limite) {
        this.info = (T[]) new Object[limite];
        this.limite = limite;
        this.tamanho = 0;
        this.inicio = 0;
    }

    @Override
    public void inserir(T valor) {
        if (tamanho == limite) 
        {
            throw new RuntimeException();
        }
        int posicaoInserir = (inicio + tamanho) % limite;
        info[posicaoInserir] = valor;
        tamanho++;
    }
    

    @Override
    public T peek() {
        if (estaVazia()) 
        {
            throw new RuntimeException();
        }
        return (T) info[inicio];
    }

    @Override
    public T retirar() {
        T valor = peek();
        info[inicio] = null;
        inicio = (inicio + 1) % limite;
        tamanho--;
        return valor;
    }

    @Override
    public boolean estaVazia() {
        return tamanho == 0;
    }

   
    @Override
    public void liberar() {
        while (!estaVazia()) 
        {
            retirar();
        }
    }


    @Override
    public String toString() {
        String texto = "";
        for (int i = 0; i < tamanho; i++) 
        {
            int idx = (inicio + i) % limite;
            texto += (info[idx]);
            if (i < tamanho - 1) 
            {
            	texto += ",";
            }
        }
        return texto;
    }

    public int getLimite() {
        return limite;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean estaCheia() {
        return tamanho == limite;
    }
    
    @SuppressWarnings("unchecked")
    public void encolher() {
    	T[] temp = (T[]) new Object[tamanho];
    	int tamanhoEncolhido = 0;
    	for(int i = 0; i < tamanho; i++)
    	{
    		if(inicio + i > limite - 1) 
    		{
    			temp[i] = info[inicio + i - limite];
    		}
    		else 
    		{
    			temp[i] = info[inicio + i];        		
    		}
    		tamanhoEncolhido++;
    	}
    	
    	inicio = 0;
    	tamanho = tamanhoEncolhido;
    	limite = tamanhoEncolhido;
    	info = temp;
    }
}
