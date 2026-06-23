package controle;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação própria do algoritmo MergeSort, usada para ordenar
 * alfabeticamente a tabela de frequência de tags (exigência do trabalho:
 * "A ordenação deverá utilizar obrigatoriamente o algoritmo MergeSort
 * implementado pela equipe.").
 */
public class MergeSort {

    /**
     * Ordena a lista in-place, em ordem crescente, usando o compareTo
     * de cada elemento.
     */
    public static <T extends Comparable<T>> void ordenar(List<T> lista) {
        if (lista.size() <= 1) {
            return;
        }
        List<T> auxiliar = new ArrayList<>(lista);
        dividir(lista, auxiliar, 0, lista.size() - 1);
    }

    private static <T extends Comparable<T>> void dividir(List<T> lista, List<T> auxiliar, int inicio, int fim) {
        if (inicio >= fim) {
            return;
        }
        int meio = (inicio + fim) / 2;
        dividir(lista, auxiliar, inicio, meio);
        dividir(lista, auxiliar, meio + 1, fim);
        intercalar(lista, auxiliar, inicio, meio, fim);
    }

    private static <T extends Comparable<T>> void intercalar(List<T> lista, List<T> auxiliar, int inicio, int meio, int fim) {
        for (int i = inicio; i <= fim; i++) {
            auxiliar.set(i, lista.get(i));
        }

        int i = inicio;
        int j = meio + 1;
        int k = inicio;

        while (i <= meio && j <= fim) {
            if (auxiliar.get(i).compareTo(auxiliar.get(j)) <= 0) {
                lista.set(k, auxiliar.get(i));
                i++;
            } else {
                lista.set(k, auxiliar.get(j));
                j++;
            }
            k++;
        }

        while (i <= meio) {
            lista.set(k, auxiliar.get(i));
            i++;
            k++;
        }

        while (j <= fim) {
            lista.set(k, auxiliar.get(j));
            j++;
            k++;
        }
    }
}
