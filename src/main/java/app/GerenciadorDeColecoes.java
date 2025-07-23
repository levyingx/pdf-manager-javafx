package app;
import java.util.*;

public class GerenciadorDeColecoes {
    private Map<String, Colecao<? extends PdfEntry>> colecoes;

    public GerenciadorDeColecoes() {
        this.colecoes = new HashMap<>();
    }

    public boolean criarColecao(String nome, String autor, int limite, Class<? extends PdfEntry> tipo) {
        if (colecoes.containsKey(nome)) return false;
        Colecao<? extends PdfEntry> nova = new Colecao<>(nome, autor, limite, tipo);
        colecoes.put(nome, nova);
        return true;
    }

    public boolean adicionarEntrada(String nomeColecao, PdfEntry entrada) {
        Colecao<? extends PdfEntry> colecao = colecoes.get(nomeColecao);
        if (colecao == null) return false;
        return colecao.adicionarEntrada(entrada);
    }

    public boolean removerEntrada(String nomeColecao, PdfEntry entrada) {
        Colecao<? extends PdfEntry> colecao = colecoes.get(nomeColecao);
        if (colecao == null) return false;

        boolean removido = colecao.removerEntrada(entrada);
        if (removido && colecao.estaVazia()) {
            colecoes.remove(nomeColecao);
        }
        return removido;
    }

    public void listarColecoes() {
        if (colecoes.isEmpty()) {
            System.out.println("Nenhuma coleção cadastrada.");
            return;
        }
        for (Colecao<?> c : colecoes.values()) {
            System.out.println(c);
        }
    }

    public Colecao<? extends PdfEntry> buscarColecao(String nome) {
        return colecoes.get(nome);
    }

    public boolean removerColecao(String nome) {
        return colecoes.remove(nome) != null;
    }

    public Collection<Colecao<? extends PdfEntry>> getTodas() {
        return colecoes.values();
    }
}
