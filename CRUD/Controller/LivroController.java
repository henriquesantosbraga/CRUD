package Controller;

import DAO.LivroDAO;
import Model.Livro;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LivroController {

    private final LivroDAO livroDAO = new LivroDAO();
    private final ObservableList<Livro> livros = FXCollections.observableArrayList();

    public LivroController() {
        carregarLivros();
    }

    public void carregarLivros() {
        try {
            livros.setAll(livroDAO.listarTodos());
        } catch (SQLException e) {
            System.err.println("Erro ao carregar livros do SQL Server: " + e.getMessage());
        }
    }

    public boolean Registrar(long id, String titulo, int nPaginas, String genero) {
        if (titulo == null || titulo.isBlank() || genero == null || genero.isBlank() || nPaginas <= 0) {
            return false;
        }
        try {
            if (livroDAO.buscarPorId(id) != null) {
                return false;
            }
            Livro novoLivro = new Livro(id, titulo.trim(), nPaginas, genero.trim());
            if (livroDAO.inserir(novoLivro)) {
                livros.add(novoLivro);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir livro: " + e.getMessage());
        }
        return false;
    }

    public List<Livro> listar() {
        return new ArrayList<>(livros);
    }

    public ObservableList<Livro> getLivrosObservable() {
        return livros;
    }

    public Livro buscarId(long id) {
        try {
            return livroDAO.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro: " + e.getMessage());
            return null;
        }
    }

    public boolean Atualizar(long id, String novoTitulo, int novasPaginas, String novoGenero) {
        if (novoTitulo == null || novoTitulo.isBlank() || novoGenero == null || novoGenero.isBlank() || novasPaginas <= 0) {
            return false;
        }
        try {
            Livro existente = livroDAO.buscarPorId(id);
            if (existente == null) {
                return false;
            }
            existente.setTitulo(novoTitulo.trim());
            existente.setnPaginas(novasPaginas);
            existente.setGenero(novoGenero.trim());

            if (livroDAO.atualizar(existente)) {
                carregarLivros();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro: " + e.getMessage());
        }
        return false;
    }

    public boolean Remover(long id) {
        try {
            if (livroDAO.remover(id)) {
                livros.removeIf(l -> l.getId() == id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover livro: " + e.getMessage());
        }
        return false;
    }

    public void Exit() {
        Platform.exit();
        System.exit(0);
    }
}