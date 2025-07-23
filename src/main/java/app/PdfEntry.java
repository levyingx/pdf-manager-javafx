package app;
import java.util.List;

public abstract class PdfEntry {
    private List<String> autores;
    private String titulo;
    private String pathPdf;
    private String areaConhecimento;

    public PdfEntry(List<String> autores, String titulo, String pathPdf, String areaConhecimento) {
        this.autores = autores;
        this.titulo = titulo;
        this.pathPdf = pathPdf;
        this.areaConhecimento = areaConhecimento;
    }

    public List<String> getAutores() {
        return autores;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPathPdf() {
        return pathPdf;
    }

    public String getAreaConhecimento() {
        return areaConhecimento;
    }

    @Override
    public String toString() {
        return "Título: " + titulo + " | Autores: " + autores;
    }
}
