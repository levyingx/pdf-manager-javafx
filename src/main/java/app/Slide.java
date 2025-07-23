package app;
import java.util.List;

public class Slide extends PdfEntry {
    private String disciplina;
    private String instituicao;  

    public Slide(List<String> autores, String titulo, String pathPdf, String areaConhecimento,
                 String disciplina, String instituicao) {
        super(autores, titulo, pathPdf, areaConhecimento);
        this.disciplina = disciplina;
        this.instituicao = instituicao;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public String getInstituicao() {
        return instituicao;
    }

    @Override
    public String toString() {
        return "Slide: " + getTitulo() +
               "\nAutores: " + getAutores() +
               "\nDisciplina: " + disciplina +
               (instituicao.isEmpty() ? "" : "\nInstituicao: " + instituicao) +
               "\nArea: " + getAreaConhecimento();
    }
}
