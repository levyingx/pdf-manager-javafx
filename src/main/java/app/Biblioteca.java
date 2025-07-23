package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Biblioteca {
    protected List<PdfEntry> entradas;

    public Biblioteca() {
        entradas = new ArrayList<>();
    }

    public void adicionarEntrada(PdfEntry e) {
        entradas.add(e);
    }

    public void listarEntradas() {
        if (entradas.isEmpty()) {
            System.out.println("Nenhuma entrada cadastrada.");
            return;
        }
        for (PdfEntry e : entradas) {
            System.out.println(e + "\n");
        }
    }

    public List<PdfEntry> buscarPorTitulo(String titulo) {
        List<PdfEntry> encontrados = new ArrayList<>();
        for (PdfEntry e : entradas) {
            if (e.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                encontrados.add(e);
            }
        }
        return encontrados;
    }

    public List<PdfEntry> buscarPorAutor(String autor) {
        List<PdfEntry> encontrados = new ArrayList<>();
        for (PdfEntry e : entradas) {
            for (String a : e.getAutores()) {
                if (a.toLowerCase().contains(autor.toLowerCase())) {
                    encontrados.add(e);
                    break; 
                }
            }
        }
        return encontrados;
    }

    public void adicionarEntradaViaPrompt(Scanner scanner) {
        System.out.println("Escolha o tipo: 1-Livro, 2-Nota, 3-Slide");
        int tipo = Integer.parseInt(scanner.nextLine());

        System.out.println("Digite os nomes dos autores (separados por virgula):");
        String[] autoresStr = scanner.nextLine().split(",");
        List<String> autores = new ArrayList<>();
        for (String s : autoresStr)
            autores.add(s.trim());

        System.out.println("Digite o titulo:");
        String titulo = scanner.nextLine();

        System.out.println("Digite o path do PDF:");
        String path = scanner.nextLine();

        System.out.println("Digite a area de conhecimento:");
        String area = scanner.nextLine();

        switch (tipo) {
            case 1: // Livro
                System.out.println("Digite o subtitulo:");
                String subtitulo = scanner.nextLine();
                System.out.println("Digite o ano de publicacao:");
                int ano = Integer.parseInt(scanner.nextLine());
                System.out.println("Digite a editora (ou deixe em branco):");
                String editora = scanner.nextLine();
                System.out.println("Digite o numero de paginas (ou 0):");
                int paginas = Integer.parseInt(scanner.nextLine());
                adicionarEntrada(new Livro(autores, titulo, subtitulo, path, area, ano, editora, paginas));
                break;

            case 2: // Nota de aula
                System.out.println("Digite o subtitulo:");
                String subtNota = scanner.nextLine();
                System.out.println("Digite a disciplina:");
                String disciplinaNota = scanner.nextLine();
                System.out.println("Digite a instituicao (ou deixe em branco):");
                String instNota = scanner.nextLine();
                System.out.println("Digite o numero de paginas (ou 0):");
                int pagNota = Integer.parseInt(scanner.nextLine());
                adicionarEntrada(
                        new NotaAula(autores, titulo, subtNota, path, area, disciplinaNota, instNota, pagNota));
                break;

            case 3: // Slide
                System.out.println("Digite a disciplina:");
                String disciplinaSlide = scanner.nextLine();
                System.out.println("Digite a instituicao (ou deixe em branco):");
                String instSlide = scanner.nextLine();
                adicionarEntrada(new Slide(autores, titulo, path, area, disciplinaSlide, instSlide));
                break;

            default:
                System.out.println("Tipo invalido.");
        }
    }

    public List<PdfEntry> getEntradas() {
        return entradas;
    }
}
