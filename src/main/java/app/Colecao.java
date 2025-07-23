package app;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Colecao<T extends PdfEntry> {
    private String nome;
    private String autor;
    private int limite;
    private Class<T> tipo;
    private List<T> entradas;

    public Colecao(String nome, String autor, int limite, Class<T> tipo) {
        this.nome = nome;
        this.autor = autor;
        this.limite = limite;
        this.tipo = tipo;
        this.entradas = new ArrayList<>();
    }

    public boolean adicionarEntrada(PdfEntry entrada) {
        if (!tipo.isInstance(entrada)) return false;
        if (!entrada.getAutores().contains(autor)) return false;
        if (entradas.size() >= limite) return false;

        entradas.add(tipo.cast(entrada));
        return true;
    }

    public boolean removerEntrada(PdfEntry entrada) {
        return entradas.remove(entrada);
    }

    public boolean estaVazia() {
        return entradas.isEmpty();
    }

    public String getNome() {
        return nome;
    }

    public String getAutor() {
        return autor;
    }

    public int getLimite() {
        return limite;
    }

    public Class<T> getTipo() {
        return tipo;
    }

    public List<T> getEntradas() {
        return entradas;
    }

    public int tamanho() {
        return entradas.size();
    }

    @Override
    public String toString() {
        return "Coleção '" + nome + "' de " + tipo.getSimpleName() +
               " (autor: " + autor + ", " + entradas.size() + "/" + limite + " itens)";
    }

    public void exportarBibTex(String caminhoArquivo) throws IOException {
        if (!tipo.equals(Livro.class)) {
            throw new IllegalArgumentException("Exportação BibTeX só é válida para coleções de livros.");
        }

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (T entrada : entradas) {
                Livro livro = (Livro) entrada;

                writer.write("@book{" + formatarChaveBibtex(livro) + ",\n");
                writer.write("  author    = \"" + String.join(" and ", livro.getAutores()) + "\",\n");
                writer.write("  title     = \"" + livro.getTitulo() + "\",\n");

                if (!livro.getSubtitulo().isEmpty()) {
                    writer.write("  subtitle  = \"" + livro.getSubtitulo() + "\",\n");
                }

                writer.write("  year      = \"" + livro.getAnoPublicacao() + "\",\n");

                if (!livro.getEditora().isEmpty()) {
                    writer.write("  publisher = \"" + livro.getEditora() + "\",\n");
                }

                writer.write("}\n\n");
            }
        }
    }

    private String formatarChaveBibtex(Livro livro) {
        String autor = livro.getAutores().get(0).toLowerCase().replaceAll("\\s+", "");
        return autor + livro.getAnoPublicacao();
    }

    public void exportarZip(String caminhoZip) throws IOException {
        // Garante a extensão .zip
        if (!caminhoZip.toLowerCase().endsWith(".zip")) {
            caminhoZip += ".zip";
        }

        int arquivosAdicionados = 0;
        int arquivosNaoEncontrados = 0;
        List<String> arquivosFaltantes = new ArrayList<>();

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(caminhoZip))) {
            for (T entrada : entradas) {
                String pathPdf = entrada.getPathPdf();
                
                // Normaliza a extensão .pdf
                if (!pathPdf.toLowerCase().endsWith(".pdf")) {
                    pathPdf += ".pdf";
                }
                
                File pdfFile = new File(pathPdf);
                if (!pdfFile.exists() || !pdfFile.isFile()) {
                    arquivosNaoEncontrados++;
                    arquivosFaltantes.add(pathPdf);
                    continue;
                }

                try (FileInputStream fis = new FileInputStream(pdfFile)) {
                    // Usa apenas o nome do arquivo no ZIP
                    ZipEntry zipEntry = new ZipEntry(pdfFile.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    arquivosAdicionados++;
                }
            }
        }

        // Gera relatório detalhado
        System.out.println("\n=== Resultado da Exportacao ===");
        System.out.println("Arquivo ZIP criado em: " + new File(caminhoZip).getAbsolutePath());
        System.out.println("Total de itens na colecao: " + entradas.size());
        System.out.println("PDFs adicionados com sucesso: " + arquivosAdicionados);
        
        if (arquivosNaoEncontrados > 0) {
            System.out.println("\n[ATENCAO] " + arquivosNaoEncontrados + " arquivos nao encontrados:");
            arquivosFaltantes.forEach(System.out::println);
        }

        if (arquivosAdicionados == 0) {
            System.out.println("\n[ERRO] Nenhum arquivo valido foi encontrado. ZIP vazio nao foi criado.");
            new File(caminhoZip).delete(); // Remove o ZIP vazio
        }
    }
}
