package app;
import java.util.List;

public class Livro extends PdfEntry {
    private String subtitulo;
    private int anoPublicacao;
    private String editora;      
    private int paginas;         

    public Livro(List<String> autores, String titulo, String subtitulo, String pathPdf, String areaConhecimento,
                 int anoPublicacao, String editora, int paginas) {
        super(autores, titulo, pathPdf, areaConhecimento);
        this.subtitulo = subtitulo;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.paginas = paginas;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public String getEditora() {
        return editora;
    }

    public int getPaginas() {
        return paginas;
    }

    @Override
    public String toString() {
        return "Livro: " + getTitulo() + (subtitulo.isEmpty() ? "" : " - " + subtitulo) +
               "\nAutores: " + getAutores() +
               "\nAno: " + anoPublicacao +
               (editora.isEmpty() ? "" : "\nEditora: " + editora) +
               (paginas > 0 ? "\nPaginas: " + paginas : "") +
               "\nArea: " + getAreaConhecimento();
    }
}
