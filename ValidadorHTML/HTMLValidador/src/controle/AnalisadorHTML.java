package controle;

import modelo.Arvore;
import modelo.Fila;
import modelo.FilaVetor;
import modelo.NoArvore;
import modelo.Pilha;
import modelo.PilhaLista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lê um arquivo HTML/TXT linha a linha, valida o balanceamento das tags
 * usando uma Pilha, monta a hierarquia do documento usando uma Arvore,
 * conta a frequência de cada tag e registra os erros encontrados em uma Fila.
 */
public class AnalisadorHTML {

    // Tags que não exigem fechamento (não devem ser empilhadas) lista 
    private static final Set<String> TAGS_SINGLETON = new HashSet<>(Arrays.asList(
            "meta", "base", "br", "col", "command", "embed", "hr",
            "img", "input", "link", "param", "source", "!doctype"
    ));

    // Reconhece qualquer trecho "<...>" sem '<' ou '>' dentro (já exclui comentários completos tratados à parte)
    private static final Pattern PADRAO_TAG = Pattern.compile("<[^<>]*>"); //  - expressão regular

    private Pilha<NoArvore<TagInfo>> pilha;
    private Arvore<TagInfo> arvore;
    private Fila<String> filaErros;
    private Map<String, TagFrequencia> frequencias;
    private boolean bemFormatado;

    public ResultadoAnalise analisar(File arquivo) throws IOException { // File para arquivos
        pilha = new PilhaLista<>();
        arvore = new Arvore<>();
        frequencias = new LinkedHashMap<>(); // - mantem a ordem de acesso 
        bemFormatado = true;

        List<String> linhas = lerLinhas(arquivo);
        filaErros = new FilaVetor<>(Math.max(200, linhas.size() * 5));

        NoArvore<TagInfo> raizVirtual = new NoArvore<>(new TagInfo("#documento", 0, TipoTag.NORMAL));
        arvore.setRaiz(raizVirtual);

        int numeroLinha = 0;
        for (String linhaTexto : linhas) {
            numeroLinha++;
            if (linhaTexto.trim().isEmpty()) {
                continue; // linhas em branco são ignoradas
            }
            processarLinha(linhaTexto, numeroLinha);
        }

        verificarTagsNaoFinalizadas();

        ResultadoAnalise resultado = new ResultadoAnalise();
        resultado.setBemFormatado(bemFormatado);
        resultado.setErros(filaErros);

        if (bemFormatado) {
            resultado.setTabelaFrequencia(construirTabelaOrdenada());
            resultado.setHierarquia(construirHierarquia(raizVirtual));
        }

        return resultado;
    }

    // ---------------------------------------------------------------
    // Leitura do arquivo
    // ---------------------------------------------------------------

    private List<String> lerLinhas(File arquivo) throws IOException {
        List<String> linhas = new ArrayList<>();
        try (BufferedReader leitor = new BufferedReader( // ler texto de forma eficiente, linha por linha BufferedReader.
                new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                linhas.add(linha);
            }
        }
        return linhas;
    }

    // ---------------------------------------------------------------
    // Processamento de cada linha / tag
    // ---------------------------------------------------------------
    
    
    //Percorre uma linha do HTML, identifica tags com regex e processa cada uma enquanto valida textos entre elas 
    //e possíveis erros de formatação.
    
    private void processarLinha(String linha, int numeroLinha) {

        Matcher matcher = PADRAO_TAG.matcher(linha); // -- procurar padrões dentro de um texto.
        int posicaoAtual = 0;

        while (matcher.find()) { // find () procura a próxima ocorrência do padrão no texto.

            String trechoAntes = linha.substring(posicaoAtual, matcher.start()); // -- Ele corta uma parte do texto e retorna só aquela parte.
            validarTextoSolto(trechoAntes, numeroLinha);

            posicaoAtual = matcher.end();

            processarTag(matcher.group(), numeroLinha); // -
        }

        String resto = linha.substring(posicaoAtual);
        validarTextoSolto(resto, numeroLinha);
    }

    private void validarTextoSolto(String trecho, int numeroLinha) { // -- verificar se um texto contém outro texto dentro dele.
        if (trecho.contains("<") || trecho.contains(">")) {
            registrarErroMalformada(numeroLinha);
        }
    }

    private void processarTag(String tagBruta, int numeroLinha) {

        if (tagBruta.startsWith("<!--")) { // -- verificar se uma String começa com um determinado texto.
            return;
        }

        String conteudo = tagBruta.substring(1, tagBruta.length() - 1).trim();

        boolean fechamento = false;
        boolean autoFechada = false;

        if (conteudo.startsWith("/")) {
            fechamento = true;
            conteudo = conteudo.substring(1).trim();
        }

        if (conteudo.endsWith("/")) { // -- termina com um determinado texto.
            autoFechada = true;
            conteudo = conteudo.substring(0, conteudo.length() - 1).trim();
        }

        String nomeTag = extrairNomeTag(conteudo);

        if (nomeTag.isEmpty() || !nomeValido(nomeTag)) {
            registrarErroMalformada(numeroLinha);
            return;
        }

        boolean singleton = TAGS_SINGLETON.contains(nomeTag) || autoFechada;
        TipoTag tipo = singleton ? TipoTag.SINGLETON : TipoTag.NORMAL;

        if (fechamento) {
            tratarFechamento(nomeTag, singleton, numeroLinha);
        } else {
            tratarAbertura(nomeTag, tipo, singleton, numeroLinha);
        }
    }

    private String extrairNomeTag(String conteudo) {
        int i = 0;

        while (i < conteudo.length() &&
               !Character.isWhitespace(conteudo.charAt(i))) { // -- le caracter sem espaço em branco
            i++;
        }

        return conteudo.substring(0, i).toLowerCase().trim();
    }

    private boolean nomeValido(String nome) {
        return nome.matches("^!?[a-zA-Z][a-zA-Z0-9]*$");
    }

    private void tratarFechamento(String nomeTag, boolean singleton, int numeroLinha) {

        if (singleton) {
            return;
        }

        if (pilha.estaVazia()) {
            bemFormatado = false;
            filaErros.inserir("Erro na linha " + numeroLinha +
                    ": Foi encontrada a tag final </" + nomeTag +
                    ">, mas nao existe tag inicial correspondente.");
            return;
        }

        NoArvore<TagInfo> topo = pilha.peek();
        String nomeTopo = topo.getInfo().getNome();

        if (!nomeTopo.equals(nomeTag)) {
            bemFormatado = false;
            filaErros.inserir("Erro na linha " + numeroLinha +
                    ": Foi encontrada a tag final </" + nomeTag +
                    ">, mas era esperada a tag final </" + nomeTopo + ">.");
            return;
        }

        pilha.pop();
    }

    private void tratarAbertura(String nomeTag, TipoTag tipo, boolean singleton, int numeroLinha) {

        registrarFrequencia(nomeTag, tipo, numeroLinha);

        TagInfo info = new TagInfo(nomeTag, numeroLinha, tipo);
        NoArvore<TagInfo> novoNo = new NoArvore<>(info);

        NoArvore<TagInfo> pai = pilha.estaVazia()
                ? arvore.getRaiz()
                : pilha.peek();

        pai.inserirFilho(novoNo);

        if (!singleton) {
            pilha.push(novoNo);
        }
    }

    private void registrarErroMalformada(int numeroLinha) {
        bemFormatado = false;
        filaErros.inserir("Erro na linha " + numeroLinha +
                ": Foi encontrada uma tag malformada.");
    }

    private void verificarTagsNaoFinalizadas() {

        if (pilha.estaVazia()) {
            return;
        }

        bemFormatado = false;

        List<String> tagsEsperadas = new ArrayList<>();

        while (!pilha.estaVazia()) {
            NoArvore<TagInfo> no = pilha.pop();
            tagsEsperadas.add("</" + no.getInfo().getNome() + ">");
        }

        filaErros.inserir("Erro: Faltam tags finais no arquivo. Tags esperadas: "
                + String.join(", ", tagsEsperadas));
    }

    // ---------------------------------------------------------------
    // Frequência de tags (ordenada com MergeSort)
    // ---------------------------------------------------------------

    private void registrarFrequencia(String nome, TipoTag tipo, int linha) {
        TagFrequencia tf = frequencias.get(nome);
        if (tf == null) {
            tf = new TagFrequencia(nome, tipo, linha);
            frequencias.put(nome, tf);
        }
        tf.incrementar();
    }

    private List<TagFrequencia> construirTabelaOrdenada() {
        List<TagFrequencia> lista = new ArrayList<>(frequencias.values());
        return MergeSort.ordenar(lista);
					 
    }

    // ---------------------------------------------------------------
    // Hierarquia (texto indentado a partir da Arvore)
    // ---------------------------------------------------------------

    private String construirHierarquia(NoArvore<TagInfo> raizVirtual) {
        StringBuilder texto = new StringBuilder();
        for (NoArvore<TagInfo> filho : filhosEmOrdemDeInsercao(raizVirtual)) {
            adicionarHierarquia(texto, filho, 0);
        }
        return texto.toString();
    }

    private void adicionarHierarquia(StringBuilder texto, NoArvore<TagInfo> no, int nivel) {
        for (int i = 0; i < nivel; i++) {
            texto.append("    ");
        }
        texto.append("<").append(no.getInfo().getNome()).append(">\n");

        for (NoArvore<TagInfo> filho : filhosEmOrdemDeInsercao(no)) {
            adicionarHierarquia(texto, filho, nivel + 1);
        }
    }

    /**
     * NoArvore.InserirFilho() sempre insere no início da lista de filhos,
     * então a ordem natural de "getPrimeiro()/getProximo()" fica invertida
     * em relação à ordem de leitura do arquivo. Aqui reconstituímos a
     * ordem correta apenas para exibição, sem alterar a Arvore original.
     */
    private List<NoArvore<TagInfo>> filhosEmOrdemDeInsercao(NoArvore<TagInfo> no) {
        List<NoArvore<TagInfo>> filhos = new ArrayList<>();
        NoArvore<TagInfo> p = no.getPrimeiro();
        while (p != null) {
            filhos.add(p);
            p = p.getProximo();
        }
        Collections.reverse(filhos);
        return filhos;
    }
}
