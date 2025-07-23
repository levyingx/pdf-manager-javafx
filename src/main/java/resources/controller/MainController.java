package resources.controller;

import java.util.List;

import app.Biblioteca;
import app.Colecao;
import app.GerenciadorDeColecoes;
import app.Livro;
import app.NotaAula;
import app.PdfEntry;
import app.Persistencia;
import app.Slide;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class MainController {

    Persistencia persistencia = new Persistencia();
    Biblioteca biblioteca = persistencia.carregar();
    GerenciadorDeColecoes gerenciador = new GerenciadorDeColecoes();

    @FXML
    private MenuItem save_btn;
    @FXML
    private MenuItem quit_btn;
    @FXML
    private MenuItem lib_add_btn;
    @FXML
    private MenuItem collection_new_btn;
    @FXML
    private MenuItem collection_add_btn;
    @FXML
    private MenuItem collection_remove_btn;
    @FXML
    private MenuItem collection_list_btn;
    @FXML
    private MenuItem collection_export_bib_btn;
    @FXML
    private MenuItem collection_export_zip_btn;
    @FXML
    private javafx.scene.control.TextField searchField;
    @FXML
    private javafx.scene.control.RadioButton radioAuthor;
    @FXML
    private javafx.scene.control.RadioButton radioTitle;
    @FXML
    private javafx.scene.control.Button searchButton;
    @FXML
    private javafx.scene.control.ListView<String> listView;
    @FXML
    private javafx.scene.control.ToggleGroup search_filter;

    @FXML
    private MenuItem load_btn;
    @FXML
    private javafx.scene.control.TableView<PdfEntry> table;
    @FXML
    private javafx.scene.control.TableColumn<PdfEntry, String> table_title;
    @FXML
    private javafx.scene.control.TableColumn<PdfEntry, String> table_author;
    @FXML
    private javafx.scene.control.TableColumn<PdfEntry, String> table_type;
    @FXML
    private javafx.scene.control.TableColumn<PdfEntry, String> table_path;

    @FXML
    public void initialize() {
        // Carregar as entradas
        listView.getItems().clear();
        for (PdfEntry entry : biblioteca.getEntradas()) {
            listView.getItems().add(entry.getTitulo() + " - " + entry.getAutores());
        }

        radioAuthor.setToggleGroup(search_filter);
        radioTitle.setToggleGroup(search_filter);
        radioTitle.setSelected(true);
    }

    // Métodos da biblioteca
    @FXML
    private void add(ActionEvent event) {
        // NotaAula(List<String> autores, String titulo, String subtitulo, String pathPdf, String areaConhecimento, String disciplina, String instituicao, int paginas)
        // Livro(List<String> autores, String titulo, String subtitulo, String pathPdf, String areaConhecimento,int anoPublicacao, String editora, int paginas)
        // Slide(List<String> autores, String titulo, String pathPdf, String areaConhecimento, String disciplina, String instituicao)

        // Cria nova janela, coleta informações e adiciona entrada
        javafx.stage.Stage popup = new javafx.stage.Stage();
        popup.setTitle("Adicionar nova entrada");

        GridPane layout = new GridPane();

        TextField tituloField = new TextField();
        TextField autorField = new TextField();
        TextField caminhoField = new TextField();
        TextField areaField = new TextField();
        TextField disciplinaField = new TextField();
        TextField instituicaoField = new TextField();
        TextField paginasField = new TextField();

        // verifica se o usuario quer adicionar um livro, nota de aula ou slide
        // depois permite ele preencher os campos especificos do tipo escolhido
        ChoiceBox<String> tipoBox = new ChoiceBox<>();
        tipoBox.getItems().addAll("Livro", "Nota de Aula", "Slide");
        tipoBox.getSelectionModel().selectFirst();

        Button confirmarBtn = new Button("Adicionar");

        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new javafx.geometry.Insets(10));

        layout.add(new javafx.scene.control.Label("Título:"), 0, 0);
        layout.add(tituloField, 1, 0);

        layout.add(new javafx.scene.control.Label("Autor(es) (separados por vírgula):"), 0, 1);
        layout.add(autorField, 1, 1);

        layout.add(new javafx.scene.control.Label("Caminho do PDF:"), 0, 2);
        layout.add(caminhoField, 1, 2);

        layout.add(new javafx.scene.control.Label("Área de Conhecimento:"), 0, 3);
        layout.add(areaField, 1, 3);

        layout.add(new javafx.scene.control.Label("Tipo:"), 0, 4);
        layout.add(tipoBox, 1, 4);

        // Campos específicos
        layout.add(new javafx.scene.control.Label("Disciplina:"), 0, 5);
        layout.add(disciplinaField, 1, 5);

        layout.add(new javafx.scene.control.Label("Instituição:"), 0, 6);
        layout.add(instituicaoField, 1, 6);

        layout.add(new javafx.scene.control.Label("Páginas:"), 0, 7);
        layout.add(paginasField, 1, 7);

        confirmarBtn.setOnAction(e -> {
            String titulo = tituloField.getText().trim();
            String autoresStr = autorField.getText().trim();
            String caminho = caminhoField.getText().trim();
            String area = areaField.getText().trim();
            String disciplina = disciplinaField.getText().trim();
            String instituicao = instituicaoField.getText().trim();
            String paginasStr = paginasField.getText().trim();
            String tipoSelecionado = tipoBox.getValue();

            if (titulo.isEmpty() || autoresStr.isEmpty() || caminho.isEmpty() || area.isEmpty()) {
                System.out.println("Preencha os campos obrigatórios.");
                return;
            }

            List<String> autores = List.of(autoresStr.split("\\s*,\\s*"));

            PdfEntry novaEntrada = null;

            switch (tipoSelecionado) {
                case "Livro":
                    TextField anoField = new TextField();
                    TextField editoraField = new TextField();
                    layout.add(new javafx.scene.control.Label("Ano de Publicação:"), 0, 8);
                    layout.add(anoField, 1, 8);
                    layout.add(new javafx.scene.control.Label("Editora:"), 0, 9);
                    layout.add(editoraField, 1, 9);

                    String anoStr = anoField.getText().trim();
                    String editora = editoraField.getText().trim();
                    int paginas = paginasStr.isEmpty() ? 0 : Integer.parseInt(paginasStr);
                    int ano = anoStr.isEmpty() ? 0 : Integer.parseInt(anoStr);

                    novaEntrada = new Livro(autores, titulo, "", caminho, area, ano, editora, paginas);
                    break;
                case "Nota de Aula":
                    int paginasNota = paginasStr.isEmpty() ? 0 : Integer.parseInt(paginasStr);
                    novaEntrada = new NotaAula(autores, titulo, "", caminho, area, disciplina, instituicao, paginasNota);
                    break;
                case "Slide":
                    novaEntrada = new Slide(autores, titulo, caminho, area, disciplina, instituicao);
                    break;
                default:
                    System.out.println("Tipo inválido.");
                    return;
            }

            biblioteca.adicionarEntrada(novaEntrada);
            listView.getItems().add(novaEntrada.getTitulo() + " - " + novaEntrada.getAutores());
            popup.close();
        });

        layout.add(confirmarBtn, 1, 10);

        popup.setScene(new javafx.scene.Scene(layout, 500, 400));
        popup.show();
    }

    @FXML
    private void save(ActionEvent event) {
        persistencia.salvar(biblioteca);
        System.out.println("Biblioteca salva.");
    }

    @FXML
    private void load(ActionEvent event) {
        biblioteca = persistencia.carregar();
        System.out.println("Biblioteca carregada.");
        listView.getItems().clear();
        for (PdfEntry entry : biblioteca.getEntradas()) {
            listView.getItems().add(entry.getTitulo() + " - " + entry.getAutores());
        }
    }

    @FXML
    private void search(ActionEvent event) {
        String termo = searchField.getText().trim();
        if (termo.isEmpty()) {
            System.out.println("Campo de busca vazio");
            return;
        }

        boolean buscarPorAutor = radioAuthor.isSelected();
        List<PdfEntry> resultados = buscarPorAutor
                ? biblioteca.buscarPorAutor(termo)
                : biblioteca.buscarPorTitulo(termo);

        listView.getItems().setAll(resultados.stream()
                .map(entry -> entry.getTitulo() + " - " + entry.getAutores())
                .toList());
    }

    // Métodos de coleção
    @FXML
    private void createCollection(ActionEvent event) {
        javafx.stage.Stage popup = new javafx.stage.Stage();
        popup.setTitle("Criar nova coleção");

        // Input
        TextField nomeField = new javafx.scene.control.TextField();
        TextField autorField = new javafx.scene.control.TextField();
        TextField limiteField = new javafx.scene.control.TextField();

        ChoiceBox<String> tipoBox = new javafx.scene.control.ChoiceBox<String>();
        tipoBox.getItems().addAll("Livro", "Nota de Aula", "Slide");
        tipoBox.getSelectionModel().selectFirst();

        Button confirm = new javafx.scene.control.Button("Criar");

        confirm.setOnAction(e -> {
            String nome = nomeField.getText().trim();
            String autor = autorField.getText().trim();
            String limiteStr = limiteField.getText().trim();
            String tipoSelecionado = (String) tipoBox.getValue();

            if (nome.isEmpty() || autor.isEmpty() || limiteStr.isEmpty()) {
                System.out.println("Preencha todos os campos.");
                return;
            }

            int limite;
            try {
                limite = Integer.parseInt(limiteStr);
            } catch (NumberFormatException ex) {
                System.out.println("Limite deve ser um número.");
                return;
            }

            Class<? extends PdfEntry> tipo = null;
            switch (tipoSelecionado) {
                case "Livro":
                    tipo = Livro.class;
                    break;
                case "Nota de Aula":
                    tipo = NotaAula.class;
                    break;
                case "Slide":
                    tipo = Slide.class;
                    break;
                default:
                    tipo = null;
                    break;
            }

            if (tipo == null) {
                System.out.println("Tipo inválido.");
                return;
            }

            boolean sucesso = gerenciador.criarColecao(nome, autor, limite, tipo);
            if (sucesso) {
                System.out.println("Coleção criada: " + nome);
                popup.close();
            } else {
                System.out.println("Erro: nome já em uso.");
            }
        });

        // Layout
        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new javafx.geometry.Insets(10));

        layout.add(new javafx.scene.control.Label("Nome:"), 0, 0);
        layout.add(nomeField, 1, 0);

        layout.add(new javafx.scene.control.Label("Autor:"), 0, 1);
        layout.add(autorField, 1, 1);

        layout.add(new javafx.scene.control.Label("Limite:"), 0, 2);
        layout.add(limiteField, 1, 2);

        layout.add(new javafx.scene.control.Label("Tipo:"), 0, 3);
        layout.add(tipoBox, 1, 3);

        layout.add(confirm, 1, 4);

        popup.setScene(new javafx.scene.Scene(layout));
        popup.show();
    }

    @FXML
    private void addCollection(ActionEvent event) {
        javafx.stage.Stage popup = new javafx.stage.Stage();
        popup.setTitle("Adicionar entrada à coleção");

        TextField nomeColecaoField = new javafx.scene.control.TextField();
        TextField entradaField = new javafx.scene.control.TextField();

        Button confirm = new javafx.scene.control.Button("Adicionar");

        confirm.setOnAction(e -> {
            String nomeColecao = nomeColecaoField.getText().trim();
            String entradaStr = entradaField.getText().trim();

            Colecao<? extends PdfEntry> colecao = gerenciador.buscarColecao(nomeColecao);
            if (colecao == null) {
                System.out.println("Coleção não encontrada.");
                return;
            }

            List<PdfEntry> entrada = biblioteca.buscarPorTitulo(entradaStr);
            if (entrada == null || entrada.isEmpty()) {
                System.out.println("Entrada não encontrada.");
                return;
            }

            boolean sucesso = gerenciador.adicionarEntrada(nomeColecao, entrada.get(0));
            if (sucesso) {
                System.out.println("Entrada adicionada à coleção: " + nomeColecao);
                popup.close();
            } else {
                System.out.println("Erro ao adicionar entrada.");
            }
        });

        // Layout
        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new javafx.geometry.Insets(10));

        layout.add(new javafx.scene.control.Label("Nome da Coleção:"), 0, 0);
        layout.add(nomeColecaoField, 1, 0);

        layout.add(new javafx.scene.control.Label("Entrada (título):"), 0, 1);
        layout.add(entradaField, 1, 1);

        layout.add(confirm, 1, 2);

        popup.setScene(new javafx.scene.Scene(layout));
        popup.show();
    }

    @FXML
    private void removeCollection(ActionEvent event) {
        javafx.stage.Stage popup = new javafx.stage.Stage();
        popup.setTitle("Remover entrada da coleção");

        TextField nomeColecaoField = new javafx.scene.control.TextField();
        TextField entradaField = new javafx.scene.control.TextField();

        Button confirm = new javafx.scene.control.Button("Remover");

        confirm.setOnAction(e -> {
            String nomeColecao = nomeColecaoField.getText().trim();
            String entradaStr = entradaField.getText().trim();

            Colecao<? extends PdfEntry> colecao = gerenciador.buscarColecao(nomeColecao);
            if (colecao == null) {
                System.out.println("Coleção não encontrada.");
                return;
            }

            List<PdfEntry> entrada = biblioteca.buscarPorTitulo(entradaStr);
            if (entrada == null || entrada.isEmpty()) {
                System.out.println("Entrada não encontrada.");
                return;
            }

            boolean sucesso = gerenciador.removerEntrada(nomeColecao, entrada.get(0));
            if (sucesso) {
                System.out.println("Entrada removida da coleção: " + nomeColecao);
                popup.close();
            } else {
                System.out.println("Erro ao remover entrada.");
            }
        });

        // Layout
        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new javafx.geometry.Insets(10));

        layout.add(new javafx.scene.control.Label("Nome da Coleção:"), 0, 0);
        layout.add(nomeColecaoField, 1, 0);

        layout.add(new javafx.scene.control.Label("Entrada (título):"), 0, 1);
        layout.add(entradaField, 1, 1);

        layout.add(confirm, 1, 2);

        popup.setScene(new javafx.scene.Scene(layout));
        popup.show();
    }

    @FXML
    private void exportBib(ActionEvent event) {
        javafx.stage.Stage popup = new javafx.stage.Stage();
        popup.setTitle("Exportar coleção para .bib");

        TextField nomeColecaoField = new TextField();
        TextField caminhoField = new TextField();

        Button confirm = new Button("Exportar");

        confirm.setOnAction(e -> {
            String nomeColecao = nomeColecaoField.getText().trim();
            String caminhoBib = caminhoField.getText().trim();

            if (nomeColecao.isEmpty() || caminhoBib.isEmpty()) {
                System.out.println("Precisa preencher todos os campos.");
                return;
            }

            try {
                Colecao<?> colBib = gerenciador.buscarColecao(nomeColecao);
                if (colBib != null) {
                    if (colBib.getTipo().equals(Livro.class)) {
                        @SuppressWarnings("unchecked")
                        Colecao<Livro> colecaoLivros = (Colecao<Livro>) colBib;
                        colecaoLivros.exportarBibTex(caminhoBib);
                        System.out.println("Arquivo .bib exportado.");
                        popup.close();
                    } else {
                        System.out.println("Coleção não é de livros.");
                    }
                } else {
                    System.out.println("Coleção não encontrada.");
                }
            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
        });

        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new javafx.geometry.Insets(10));

        layout.add(new javafx.scene.control.Label("Nome da Coleção:"), 0, 0);
        layout.add(nomeColecaoField, 1, 0);

        layout.add(new javafx.scene.control.Label("Caminho do arquivo .bib:"), 0, 1);
        layout.add(caminhoField, 1, 1);

        layout.add(confirm, 1, 2);

        popup.setScene(new javafx.scene.Scene(layout));
        popup.show();
    }

    @FXML
    private void exportZip(ActionEvent event) {
        javafx.stage.Stage popup = new javafx.stage.Stage();
        popup.setTitle("Exportar coleção para .zip");

        TextField nomeColecaoField = new TextField();
        TextField caminhoField = new TextField();

        Button confirm = new Button("Exportar");

        confirm.setOnAction(e -> {
            String nomeColecao = nomeColecaoField.getText().trim();
            String caminhoZip = caminhoField.getText().trim();

            if (nomeColecao.isEmpty() || caminhoZip.isEmpty()) {
                System.out.println("Preencha todos os campos.");
                return;
            }

            try {
                Colecao<?> colZip = gerenciador.buscarColecao(nomeColecao);
                if (colZip != null) {
                    colZip.exportarZip(caminhoZip);
                    System.out.println("Arquivo .zip exportado.");
                    popup.close();
                } else {
                    System.out.println("Coleção não encontrada.");
                }
            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
        });

        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new javafx.geometry.Insets(10));

        layout.add(new javafx.scene.control.Label("Nome da Coleção:"), 0, 0);
        layout.add(nomeColecaoField, 1, 0);

        layout.add(new javafx.scene.control.Label("Caminho do arquivo .zip:"), 0, 1);
        layout.add(caminhoField, 1, 1);

        layout.add(confirm, 1, 2);

        popup.setScene(new javafx.scene.Scene(layout));
        popup.show();
    }

    @FXML
    private void listCollections(ActionEvent event) {
        gerenciador.listarColecoes();
        javafx.stage.Stage popup = new javafx.stage.Stage();
        popup.setTitle("Listar Coleções");

        javafx.scene.control.ListView<String> listView = new javafx.scene.control.ListView<>();
        for (Colecao<? extends PdfEntry> colecao : gerenciador.getTodas()) {
            listView.getItems().add(colecao.toString());
        }

        popup.setScene(new javafx.scene.Scene(listView, 400, 300));
        popup.show();
    }

    @FXML
    private void quit(ActionEvent event) {
        System.exit(0);
    }
}
