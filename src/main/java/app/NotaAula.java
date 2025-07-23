package app;
import java.util.List;

public class NotaAula extends PdfEntry {
    private String subtitulo;
    private String disciplina;
    private String instituicao;  
    private int paginas;         

    public NotaAula(List<String> autores, String titulo, String subtitulo, String pathPdf, String areaConhecimento,
                    String disciplina, String instituicao, int paginas) {
        super(autores, titulo, pathPdf, areaConhecimento);
        this.subtitulo = subtitulo;
        this.disciplina = disciplina;
        this.instituicao = instituicao;
        this.paginas = paginas;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public int getPaginas() {
        return paginas;
    }

    @Override
    public String toString() {
        return "Nota de Aula: " + getTitulo() + " - " + subtitulo +
               "\nAutores: " + getAutores() +
               "\nDisciplina: " + disciplina +
               (instituicao.isEmpty() ? "" : "\nInstituição: " + instituicao) +
               (paginas > 0 ? "\nPaginas: " + paginas : "") +
               "\nArea: " + getAreaConhecimento();
    }
}
