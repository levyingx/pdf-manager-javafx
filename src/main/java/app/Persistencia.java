package app;
import java.io.*;
import java.util.List;

public class Persistencia {
    private static final String ARQUIVO = "biblioteca.dat";

    public void salvar(Biblioteca biblioteca) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            out.writeObject(biblioteca.getEntradas());
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

   @SuppressWarnings("unchecked") 
public Biblioteca carregar() {
    Biblioteca b = new Biblioteca();
    File f = new File(ARQUIVO);
    if (!f.exists()) return b;

    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
        List<PdfEntry> lista = (List<PdfEntry>) in.readObject();
        for (PdfEntry e : lista) {
            if (e != null) { // Validação opcional para evitar nulls
                b.adicionarEntrada(e);
            }
        }
    } catch (Exception e) {
        System.out.println("Erro ao carregar: " + e.getMessage());
    }
    return b;
}
}
