package View;

import Controller.LivroController;
import Model.Livro;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CrudView extends Application {

    private final LivroController controller = new LivroController();

    private final TextField txtId = new TextField();
    private final TextField txtTitulo = new TextField();
    private final TextField txtPaginas = new TextField();
    private final TextField txtGenero = new TextField();
    private final Label lblStatus = new Label("Pronto.");
    private final TableView<Livro> tabelaLivros = new TableView<>();

    @Override
    public void start(Stage stage) {
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(20));

        txtId.setPromptText("Ex.: 1");
        txtTitulo.setPromptText("Título do livro");
        txtPaginas.setPromptText("Ex.: 320");
        txtGenero.setPromptText("Ex.: Fantasia");

        root.add(new Label("ID:"), 0, 0);
        root.add(txtId, 1, 0);

        root.add(new Label("Título:"), 0, 1);
        root.add(txtTitulo, 1, 1);

        root.add(new Label("Nº de Páginas:"), 0, 2);
        root.add(txtPaginas, 1, 2);

        root.add(new Label("Gênero:"), 0, 3);
        root.add(txtGenero, 1, 3);

        Button btnCriar = new Button("Criar");
        Button btnAtualizar = new Button("Atualizar");
        Button btnRemover = new Button("Remover");
        Button btnLimpar = new Button("Limpar");
        Button btnSair = new Button("Sair");

        HBox caixaBotoes = new HBox(8, btnCriar, btnAtualizar, btnRemover, btnLimpar, btnSair);
        root.add(caixaBotoes, 0, 4, 2, 1);

        configurarTabela();
        root.add(tabelaLivros, 0, 5, 2, 1);
        root.add(lblStatus, 0, 6, 2, 1);

        // Eventos dos botões
        btnCriar.setOnAction(e -> criarLivro());
        btnAtualizar.setOnAction(e -> atualizarLivro());
        btnRemover.setOnAction(e -> removerLivro());
        btnLimpar.setOnAction(e -> limparCampos());
        btnSair.setOnAction(e -> controller.Exit());

        // Preenche os campos ao clicar em uma linha da tabela
        tabelaLivros.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                txtId.setText(String.valueOf(selecionado.getId()));
                txtTitulo.setText(selecionado.getTitulo());
                txtPaginas.setText(String.valueOf(selecionado.getnPaginas()));
                txtGenero.setText(selecionado.getGenero());
            }
        });

        Scene scn = new Scene(root, 720, 480);
        stage.setTitle("CRUD - Cadastro de Livros");
        stage.setScene(scn);
        stage.show();
    }

    private void configurarTabela() {
        TableColumn<Livro, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getId()));
        colId.setPrefWidth(70);

        TableColumn<Livro, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitulo()));
        colTitulo.setPrefWidth(260);

        TableColumn<Livro, Number> colPaginas = new TableColumn<>("Páginas");
        colPaginas.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getnPaginas()));
        colPaginas.setPrefWidth(100);

        TableColumn<Livro, String> colGenero = new TableColumn<>("Gênero");
        colGenero.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGenero()));
        colGenero.setPrefWidth(180);

        tabelaLivros.getColumns().addAll(colId, colTitulo, colPaginas, colGenero);
        tabelaLivros.setItems(controller.getLivrosObservable());
        tabelaLivros.setPrefHeight(220);
    }

    private void criarLivro() {
        try {
            long id = Long.parseLong(txtId.getText().trim());
            int paginas = Integer.parseInt(txtPaginas.getText().trim());
            String titulo = txtTitulo.getText();
            String genero = txtGenero.getText();

            boolean criado = controller.Registrar(id, titulo, paginas, genero);
            if (criado) {
                lblStatus.setText("Livro cadastrado com sucesso.");
                limparCampos();
            } else {
                mostrarAlerta("Não foi possível criar", "Verifique ID duplicado, campos vazios ou número de páginas.");
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("Valor inválido", "ID deve ser numérico inteiro longo e Páginas deve ser um número inteiro.");
        }
    }

    private void atualizarLivro() {
        try {
            long id = Long.parseLong(txtId.getText().trim());
            int paginas = Integer.parseInt(txtPaginas.getText().trim());
            String titulo = txtTitulo.getText();
            String genero = txtGenero.getText();

            boolean atualizado = controller.Atualizar(id, titulo, paginas, genero);
            if (atualizado) {
                tabelaLivros.refresh();
                lblStatus.setText("Livro atualizado com sucesso.");
                limparCampos();
            } else {
                mostrarAlerta("Não foi possível atualizar", "ID não encontrado ou dados inválidos.");
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("Valor inválido", "Informe um ID e um número de páginas válidos.");
        }
    }

    private void removerLivro() {
        Livro selecionado = tabelaLivros.getSelectionModel().getSelectedItem();
        long id;

        try {
            if (selecionado != null && txtId.getText().isBlank()) {
                id = selecionado.getId();
            } else {
                id = Long.parseLong(txtId.getText().trim());
            }

            boolean removido = controller.Remover(id);
            if (removido) {
                lblStatus.setText("Livro removido com sucesso.");
                limparCampos();
            } else {
                mostrarAlerta("Não foi possível remover", "Nenhum livro encontrado com o ID informado.");
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("Valor inválido", "Selecione um livro na tabela ou digite um ID numérico válido.");
        }
    }

    private void limparCampos() {
        txtId.clear();
        txtTitulo.clear();
        txtPaginas.clear();
        txtGenero.clear();
        tabelaLivros.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}