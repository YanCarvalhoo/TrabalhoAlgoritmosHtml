# Validador de Estrutura HTML

Programa em Java (com interface gráfica Swing) que analisa um arquivo
`.html` ou `.txt`, valida o balanceamento das tags, identifica erros
estruturais, calcula a frequência das tags, ordena a tabela com **MergeSort**
próprio e exibe a hierarquia do documento.

## Estrutura do projeto

```
src/
  modelo/      -> estruturas de dados (Pilha, Fila, Árvore)
  negocio/     -> regras de negócio (leitura, validação, ordenação)
  visao/       -> interface gráfica (Swing)
  app/         -> ponto de entrada (Main.java)
testes/        -> arquivos .html de exemplo (1 válido + 4 com erros)
```

### Classes reaproveitadas dos arquivos da equipe (lógica original mantida)
- `Arvore` / `NoArvore` → usadas para montar a **hierarquia do HTML**
  (cada nó da árvore = uma tag; os filhos são as tags aninhadas).
- `Pilha` / `PilhaLista` → usadas para **validar abertura/fechamento**
  das tags (pilha obrigatória pelo enunciado).
- `Fila` / `FilaVetor` → usada para **armazenar as mensagens de erro**
  encontradas durante a leitura do arquivo.

> Os pacotes originais (`Modelo`, `Prova2`, `pilha`) foram unificados em
> um único pacote `modelo`, pois vinham de exercícios diferentes e
> precisavam compilar juntos como um projeto só. A lógica interna de cada
> classe **não foi alterada**.
>
> A `PilhaLista.java` da equipe depende de uma classe `ListaEncadeada`
> que não estava entre os arquivos enviados — ela foi implementada
> (`modelo/ListaEncadeada.java` e `modelo/NoLista.java`) só para que a
> `PilhaLista` de vocês funcionasse exatamente como foi escrita, sem
> reescrever a lógica de pilha.

### Classes novas (lógica do trabalho)
- `negocio.TagInfo` / `TipoTag` — dados de cada tag (nome, linha, tipo).
- `negocio.TagFrequencia` — uma linha da tabela de frequência.
- `negocio.MergeSort` — ordenação alfabética implementada pela equipe
  (exigência obrigatória do trabalho).
- `negocio.AnalisadorHTML` — motor da análise: lê o arquivo linha a
  linha, extrai as tags com expressão regular, valida com a `Pilha`,
  monta a `Arvore`, conta as frequências e registra os erros na `Fila`.
- `visao.TelaPrincipal` — interface gráfica (Swing): campo para
  informar/selecionar o arquivo e três abas (Erros, Frequência de Tags,
  Hierarquia HTML).

## Regras implementadas

- Leitura linha a linha; linhas em branco são ignoradas.
- Tags tratadas sem diferenciar maiúsculas/minúsculas.
- Atributos são ignorados na validação (`<a href="...">` conta só como `a`).
- Tags **singleton** (sem fechamento), reconhecidas e **não empilhadas**:
  `meta, base, br, col, command, embed, hr, img, input, link, param, source, !doctype`
  (e qualquer tag escrita como autofechada, ex.: `<br/>`).
- Comentários HTML (`<!-- ... -->`) são ignorados.
- Erros identificados:
  1. **Tag final inesperada** — fechamento não confere com o topo da pilha.
  2. **Tag final sem tag inicial** — fechamento encontrado com a pilha vazia.
  3. **Tags não finalizadas** — pilha não esvaziou ao final do arquivo.
  4. **Tag malformada** — `<` ou `>` "soltos" fora de uma tag válida, ou
     nome de tag inválido.
- Se o arquivo está bem formatado: tabela (tag, frequência, tipo,
  primeira linha) ordenada alfabeticamente com MergeSort, mais a
  hierarquia do documento com indentação.

## Como compilar e executar (linha de comando)

Pré-requisito: JDK 11 ou superior instalado.

```bash
# a partir da pasta do projeto (onde está esta pasta "src")
javac -d bin $(find src -name "*.java")
java -cp bin app.Main
```

A janela do **Validador de Estrutura HTML** vai abrir. Clique em
"Procurar..." (ou digite o caminho do arquivo) e depois em "Analisar".

## Como importar em uma IDE (Eclipse / NetBeans / IntelliJ)

1. Crie um novo projeto Java.
2. Copie a pasta `src` (com as subpastas `modelo`, `negocio`, `visao`,
   `app`) para dentro do projeto, substituindo a pasta `src` padrão.
3. Defina `app.Main` como classe principal (Main Class) do projeto.
4. Execute normalmente.

## Arquivos de teste (pasta `testes/`)

| Arquivo | O que demonstra |
|---|---|
| `valido.html` | arquivo corretamente formatado (tabela + hierarquia) |
| `invalido_tag_inesperada.html` | tag final inesperada |
| `invalido_sem_abertura.html` | tag final sem tag inicial correspondente |
| `invalido_nao_finalizado.html` | tags não finalizadas no fim do arquivo |
| `invalido_malformado.html` | tag malformada (sem fechar com `>`) |

## Limitações conhecidas (escopo do trabalho)

- Não trata `>` dentro de valores de atributo (ex.: `href="x>y"`), caso
  raro e fora do escopo do enunciado.
- Texto comum contendo `<` ou `>` soltos (fora de uma tag real) é
  reportado como tag malformada, conforme pedido no enunciado.
