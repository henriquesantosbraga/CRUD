package Model;

public class Livro {
    private long id;
    private String titulo;
    private int nPaginas;
    private String genero;

    public Livro(long id, String titulo, int nPaginas, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.nPaginas = nPaginas;
        this.genero = genero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getnPaginas() {
        return nPaginas;
    }

    public void setnPaginas(int nPaginas) {
        this.nPaginas = nPaginas;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nPaginas=" + nPaginas +
                ", genero='" + genero + '\'' +
                '}';
    }
}