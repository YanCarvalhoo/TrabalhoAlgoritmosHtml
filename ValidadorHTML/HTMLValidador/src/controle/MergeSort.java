package controle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSort <T extends Comparable<T>>{

    public static <T extends Comparable<T>> List<T> ordenar(List<T> lista) {
        T[] array = (T[]) lista.toArray(new Comparable[0]);
        MergeSort<T> mergeSort = new MergeSort<>();
        mergeSort.setInfo(array);
        mergeSort.ordenar();
        return new ArrayList<>(Arrays.asList(array));
    }

	private T[] info;

    public T[] getInfo() { 
    	return info; 
    }
    
    public void setInfo(T[] info) { 
    	this.info = info; 
    }
    
	public void ordenar() {
        mergeSort(0, info.length - 1);
    }

    private void mergeSort(int inicio, int fim) {
        if (inicio < fim) 
        {
            int meio = (inicio + fim) / 2;
            mergeSort(inicio, meio);
            mergeSort(meio + 1, fim);
            merge(inicio, fim, meio);
        }
    }

    private void merge(int inicio, int fim, int meio) {
        int tamEsquerda = meio - inicio + 1;
        T[] esquerda = (T[]) new Comparable[tamEsquerda];
        for (int i = 0; i < tamEsquerda; i++)
        {
            esquerda[i] = info[inicio + i];
        }

        int tamDireita = fim - meio;
        T[] direita = (T[]) new Comparable[tamDireita];
        for (int i = 0; i < tamDireita; i++) 
        {
            direita[i] = info[meio + 1 + i];
        }

        int cEsq = 0;
        int cDir = 0;
        int i = inicio;
        for (i = inicio; i <= fim; i++) 
        {
            if (cEsq < tamEsquerda && cDir < tamDireita) 
            {
                if (esquerda[cEsq].compareTo(direita[cDir]) <= 0) 
                {
                    info[i] = esquerda[cEsq++];
                } 
                else 
                {
                    info[i] = direita[cDir++];
                }
            } 
            else 
            {
                break;
            }
        }

        while (cEsq < tamEsquerda) 
        {
            info[i++] = esquerda[cEsq++];
        }

        while (cDir < tamDireita) 
        {
            info[i++] = direita[cDir++];
        }
    }
}
