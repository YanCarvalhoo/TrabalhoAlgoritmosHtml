package app;

import visao.TelaPrincipal;

import javax.swing.SwingUtilities;

/**
 * Ponto de entrada do Validador de Estrutura HTML.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
