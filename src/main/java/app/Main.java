// package app;
// import java.util.Scanner;

// public class Main {
//     public static void main(String[] args) {
//         Scanner scanner = new Scanner(System.in);
//         Persistencia persistencia = new Persistencia();
//         Biblioteca biblioteca = persistencia.carregar();
//         GerenciadorDeColecoes gerenciador = new GerenciadorDeColecoes();

//         int opcao = -1;
//         while (opcao != 0) {
//             System.out.println("\n--- MENU ---");
//             System.out.println("1 - Adicionar entrada");
//             System.out.println("2 - Listar entradas");
//             System.out.println("3 - Buscar por titulo");
//             System.out.println("4 - Buscar por autor");
//             System.out.println("5 - Salvar");
//             System.out.println("6 - Alternar biblioteca");
//             System.out.println("7 - Criar colecao");
//             System.out.println("8 - Adicionar entrada a colecao");
//             System.out.println("9 - Remover entrada da colecao");
//             System.out.println("10 - Exportar colecao .bib");
//             System.out.println("11 - Exportar colecao .zip");
//             System.out.println("12 - Listar colecoes");
//             System.out.println("0 - Sair");

//             opcao = Integer.parseInt(scanner.nextLine());

//             switch (opcao) {
//                 case 1:
//                     biblioteca.adicionarEntradaViaPrompt(scanner);
//                     break;

//                 case 2:
//                     biblioteca.listarEntradas();
//                     break;

//                 case 3:
//                     System.out.println("Digite o titulo:");
//                     String titulo = scanner.nextLine();
//                     PdfEntry encontrada = biblioteca.buscarPorTitulo(titulo);
//                     if (encontrada != null)
//                         System.out.println(encontrada);
//                     else
//                         System.out.println("Nao encontrada.");
//                     break;

//                 case 4:
//                     System.out.println("Digite o autor:");
//                     String autorBusca = scanner.nextLine();
//                     biblioteca.buscarPorAutor(autorBusca);
//                     break;

//                 case 5:
//                     persistencia.salvar(biblioteca);
//                     System.out.println("Biblioteca salva.");
//                     break;

//                 case 6:
//                     biblioteca = persistencia.carregar();
//                     System.out.println("Biblioteca atualizada.");
//                     break;

//                 case 7:
//                     System.out.println("Nome da colecao:");
//                     String nomeCol = scanner.nextLine();
//                     System.out.println("Autor (deve estar nas entradas):");
//                     String autor = scanner.nextLine();
//                     System.out.println("Limite de entradas:");
//                     int limite = Integer.parseInt(scanner.nextLine());
//                     System.out.println("Tipo: 1-Livro, 2-Nota, 3-Slide");
//                     int tipoCol = Integer.parseInt(scanner.nextLine());

//                     Class<? extends PdfEntry> classe = null; // Inicializada como null
//                     switch (tipoCol) {
//                         case 1:
//                             classe = Livro.class;
//                             break;
//                         case 2:
//                             classe = NotaAula.class;
//                             break;
//                         case 3:
//                             classe = Slide.class;
//                             break;
//                         default:
//                             System.out.println("Tipo invalido.");
//                             break;
//                     }

//                     if (classe != null) { // Verifica se classe foi definida
//                         if (gerenciador.criarColecao(nomeCol, autor, limite, classe))
//                             System.out.println("Colecao criada.");
//                         else
//                             System.out.println("Erro: nome ja em uso.");
//                     }
//                     break;

//                 case 8:
//                     System.out.println("Nome da colecao:");
//                     String nomeAdd = scanner.nextLine();
//                     System.out.println("Titulo da entrada:");
//                     String tituloAdd = scanner.nextLine();
//                     PdfEntry entradaAdd = biblioteca.buscarPorTitulo(tituloAdd);
//                     if (entradaAdd != null) {
//                         boolean ok = gerenciador.adicionarEntrada(nomeAdd, entradaAdd);
//                         System.out.println(ok ? "Entrada adicionada." : "Erro ao adicionar.");
//                     } else {
//                         System.out.println("Entrada nao encontrada.");
//                     }
//                     break;

//                 case 9:
//                     System.out.println("Nome da colecao:");
//                     String nomeRem = scanner.nextLine();
//                     System.out.println("Titulo da entrada:");
//                     String tituloRemover = scanner.nextLine();
//                     PdfEntry entradaRem = biblioteca.buscarPorTitulo(tituloRemover);
//                     if (entradaRem != null) {
//                         boolean ok = gerenciador.removerEntrada(nomeRem, entradaRem);
//                         System.out.println(ok ? "Removida." : "Erro ao remover.");
//                     } else {
//                         System.out.println("Entrada nao encontrada.");
//                     }
//                     break;

//                 case 10:
//                     System.out.println("Nome da colecao:");
//                     String nomeBib = scanner.nextLine();
//                     System.out.println("Caminho do arquivo .bib:");
//                     String pathBib = scanner.nextLine();

//                     try {
//                         Colecao<?> colBib = gerenciador.buscarColecao(nomeBib);
//                         if (colBib != null) {
//                             if (colBib.getTipo().equals(Livro.class)) {
//                                 @SuppressWarnings("unchecked") 
//                                 Colecao<Livro> colecaoLivros = (Colecao<Livro>) colBib;
//                                 colecaoLivros.exportarBibTex(pathBib);
//                                 System.out.println("Arquivo .bib exportado.");
//                             } else {
//                                 System.out.println("Erro: Colecao nao e de livros.");
//                             }
//                         } else {
//                             System.out.println("Colecao nao encontrada.");
//                         }
//                     } catch (Exception e) {
//                         System.out.println("Erro: " + e.getMessage());
//                     }
//                     break;

//                 case 11:
//                     System.out.println("Nome da colecao:");
//                     String nomeZip = scanner.nextLine();
//                     System.out.println("Caminho do arquivo .zip:");
//                     String pathZip = scanner.nextLine();

//                     try {
//                         Colecao<?> colZip = gerenciador.buscarColecao(nomeZip);
//                         if (colZip != null) {
//                             colZip.exportarZip(pathZip);
//                             System.out.println("ZIP gerado.");
//                         } else {
//                             System.out.println("Colecao nao encontrada.");
//                         }
//                     } catch (Exception e) {
//                         System.out.println("Erro: " + e.getMessage());
//                     }
//                     break;

//                 case 12:
//                     gerenciador.listarColecoes();
//                     break;

//                 case 0:
//                     System.out.println("Encerrando...");
//                     break;

//                 default:
//                     System.out.println("Opcao invalida.");
//             }
//         }

//         scanner.close();
//     }
// }