import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Classe principal responsável pela execução da aplicação.
 * Esta classe:
 * <ul>
 *   <li>Lê dados de um ficheiro CSV.</li>
 *   <li>Constrói o grafo de propriedades com base em interseções geométricas.</li>
 *   <li>Gera o grafo de proprietários e visualiza um subgrafo com os primeiros 50 proprietários.</li>
 *   <li>Calcula e imprime a área média das propriedades (simples e unificada) para uma freguesia indicada.</li>
 *   <li>Gera e exibe sugestões de troca de propriedades entre proprietários para otimizar a área média.</li>
 * </ul>
 */

public class Main {

    /**
     * Ponto de entrada da aplicação.
     *
     * @param args argumentos de linha de comando (não utilizados)
     * @throws IOException se ocorrer erro ao ler o ficheiro CSV
     */
    public static void main(String[] args) throws IOException {
        String csvFileName = "Madeira-Moodle-1.1.csv";

        // Carregamento das Propriedades a partir do CSV
        CSVHandler teste = new CSVHandler(csvFileName);
        List<Propriedade> propriedades = teste.getPropriedades();
        HashMap<String,Double> precos = teste.getPrecosFreguesia();
        Grafo dois = new Grafo(propriedades,precos);

        //Contrução do grafo de propriedades (com base em interseçoes geométricas)
        Graph<Propriedade, DefaultEdge> grafopropriedade = dois.propriedade();

        //Construçao do grafo de propriedades
        Graph<String, DefaultEdge> grafoProprietarios = dois.grafoProprietarios(grafopropriedade);

        //Subgrafo com apenas os primeiros 50 proprietários para visualização
        Graph<String, DefaultEdge> subGraph = new SimpleGraph<>(DefaultEdge.class);
        int count = 0;
        for (String owner : grafoProprietarios.vertexSet()) {
            subGraph.addVertex(owner);
            count++;
            if (count >= 50) break;
        }

        for (DefaultEdge edge : grafoProprietarios.edgeSet()) {
            String src = grafoProprietarios.getEdgeSource(edge);
            String tgt = grafoProprietarios.getEdgeTarget(edge);

            if (subGraph.containsVertex(src) && subGraph.containsVertex(tgt)) {
                subGraph.addEdge(src, tgt);
            }
        }

        //Visualização do grafo filtrado
        GrafoVisual.visualize(subGraph);

        //Cálculo e exibição da área média simples e unificada
        String freguesia = "Ponta do Sol";
        double media = Grafo.areaMedia(propriedades, "freguesia", freguesia);
        System.out.printf("Área média das propriedades em " + freguesia + ": %.2f m²%n", media);

        double mediaUnificada = Grafo.areaMediaUnificada(propriedades, "freguesia", freguesia);
        System.out.printf("Área média das propriedades em %s (com união): %.2f m²%n", freguesia, mediaUnificada);

        //Geração e exibição das melhores sugestões de troca
        List<SugestaoTroca> trocas = Grafo.sugerirTrocas(propriedades, "freguesia", freguesia);
        System.out.println("Número total de sugestões geradas: " + trocas.size());

        if (trocas.isEmpty()) {
            System.out.println("Nenhuma sugestão de troca encontrada.");
        } else {
            System.out.println("Melhores sugestões de troca:");
            trocas.stream().limit(5).forEach(System.out::println);
        }


        List<SugestaoTroca> trocasAvancadas = Grafo.sugerirTrocasAvancado(propriedades, "freguesia", freguesia);
        System.out.println("Número total de sugestões geradas: " + trocas.size());

        if (trocasAvancadas.isEmpty()) {
            System.out.println("Nenhuma sugestão de troca encontrada.");
        } else {
            System.out.println("Melhores sugestões de troca AVANÇADAS:");
            trocasAvancadas.stream().limit(5).forEach(System.out::println);
        }

    }
}
