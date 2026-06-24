package visao;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import controle.AnalisadorHTML;
import controle.ResultadoAnalise;
import controle.TagFrequencia;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

/**
 * Tela principal do Validador de Estrutura HTML.
 * Permite informar/selecionar um arquivo .html ou .txt, executa a análise
 * e mostra o resultado em três abas: Erros, Frequência de Tags e Hierarquia.
 */
public class TelaPrincipal extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField campoCaminho = new JTextField();
    private final JButton botaoProcurar = new JButton("Procurar...");
    private final JButton botaoAnalisar = new JButton("Analisar");

    private final JTextArea areaErros = new JTextArea();
    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new Object[]{"Tag", "Frequência", "Tipo", "1ª Linha"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabelaFrequencia = new JTable(modeloTabela);
    private final JTextArea areaHierarquia = new JTextArea();
    private final JLabel labelStatus = new JLabel(" ");

    public TelaPrincipal() {
        super("Validador de Estrutura HTML");
        montarInterface();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 600);
        setLocationRelativeTo(null);
    }

    private void montarInterface() {
        setLayout(new BorderLayout(8, 8));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        add(montarPainelTopo(), BorderLayout.NORTH);
        add(montarAbas(), BorderLayout.CENTER);

        labelStatus.setBorder(new EmptyBorder(6, 4, 0, 0));
        add(labelStatus, BorderLayout.SOUTH);
    }

    private JPanel montarPainelTopo() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        painel.add(new JLabel("Arquivo HTML/TXT:"), c);

        c.gridx = 1; c.weightx = 1;
        painel.add(campoCaminho, c);

        c.gridx = 2; c.weightx = 0;
        painel.add(botaoProcurar, c);

        c.gridx = 3;
        painel.add(botaoAnalisar, c);

        botaoProcurar.addActionListener(e -> selecionarArquivo());
        botaoAnalisar.addActionListener(e -> analisarArquivo());

        return painel;
    }

    private JTabbedPane montarAbas() {
        JTabbedPane abas = new JTabbedPane();

        areaErros.setEditable(false);
        areaErros.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaErros.setText("Selecione um arquivo e clique em \"Analisar\".");
        abas.addTab("Erros", new JScrollPane(areaErros));

        tabelaFrequencia.getTableHeader().setReorderingAllowed(false);
        abas.addTab("Frequência de Tags", new JScrollPane(tabelaFrequencia));

        areaHierarquia.setEditable(false);
        areaHierarquia.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        abas.addTab("Hierarquia HTML", new JScrollPane(areaHierarquia));

        return abas;
    }

    private void selecionarArquivo() {
        JFileChooser seletor = new JFileChooser();
        seletor.setFileFilter(new FileNameExtensionFilter("Arquivos HTML/TXT", "html", "htm", "txt"));
        int resultado = seletor.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            campoCaminho.setText(seletor.getSelectedFile().getAbsolutePath());
        }
    }

    private void analisarArquivo() {
        String caminho = campoCaminho.getText().trim();
        if (caminho.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Informe ou selecione um arquivo .html ou .txt antes de analisar.",
                    "Nenhum arquivo informado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File arquivo = new File(caminho);
        if (!arquivo.exists() || !arquivo.isFile()) {
            JOptionPane.showMessageDialog(this,
                    "O arquivo informado não foi encontrado:\n" + caminho,
                    "Arquivo inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            AnalisadorHTML analisador = new AnalisadorHTML();
            ResultadoAnalise resultado = analisador.analisar(arquivo);
            exibirResultado(resultado);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao ler o arquivo: " + ex.getMessage(),
                    "Erro de leitura", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirResultado(ResultadoAnalise resultado) {
        modeloTabela.setRowCount(0);
        areaHierarquia.setText("");

        StringBuilder textoErros = new StringBuilder();
        int quantidadeErros = 0;
        while (!resultado.getErros().estaVazia()) {
            textoErros.append(resultado.getErros().retirar()).append("\n");
            quantidadeErros++;
        }

        if (resultado.isBemFormatado()) {
            labelStatus.setText("Arquivo bem formatado.");
            areaErros.setText("Nenhum erro encontrado. O arquivo está corretamente formatado.");

            for (TagFrequencia tf : resultado.getTabelaFrequencia()) {
                modeloTabela.addRow(new Object[]{
                        tf.getNome(),
                        tf.getFrequencia(),
                        tf.getTipo().name().toLowerCase(),
                        tf.getPrimeiraLinha()
                });
            }

            areaHierarquia.setText(resultado.getHierarquia());
        } else {
            labelStatus.setText("Arquivo mal formatado — " + quantidadeErros + " erro(s) encontrado(s).");
            areaErros.setText(textoErros.toString());
        }
    }
}
