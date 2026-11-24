package DAO;

import Model.Livro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public boolean inserir(Livro livro) throws SQLException {
        String sql = "INSERT INTO Livros (id, titulo, n_paginas, genero) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, livro.getId());
            stmt.setString(2, livro.getTitulo());
            stmt.setInt(3, livro.getnPaginas());
            stmt.setString(4, livro.getGenero());

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Livro> listarTodos() throws SQLException {
        String sql = "SELECT id, titulo, n_paginas, genero FROM Livros ORDER BY id";
        List<Livro> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getLong("id"),
                        rs.getString("titulo"),
                        rs.getInt("n_paginas"),
                        rs.getString("genero")
                );
                lista.add(livro);
            }
        }
        return lista;
    }

    public Livro buscarPorId(long id) throws SQLException {
        String sql = "SELECT id, titulo, n_paginas, genero FROM Livros WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Livro(
                            rs.getLong("id"),
                            rs.getString("titulo"),
                            rs.getInt("n_paginas"),
                            rs.getString("genero")
                    );
                }
            }
        }
        return null;
    }

    public boolean atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE Livros SET titulo = ?, n_paginas = ?, genero = ? WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getnPaginas());
            stmt.setString(3, livro.getGenero());
            stmt.setLong(4, livro.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean remover(long id) throws SQLException {
        String sql = "DELETE FROM Livros WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}