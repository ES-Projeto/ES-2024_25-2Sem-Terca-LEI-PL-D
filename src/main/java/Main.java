import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String csvFileName = "Madeira-Moodle-1.1.csv";

        // Carregamento do ficheiro CSV e criação da lista de propriedades
        CSVHandler teste = new CSVHandler(csvFileName);
        List<Propriedade> propriedades = teste.getPropriedades();
        ValorTerrenoCalculator.inicializarPrecosBase(propriedades); // Inicializar valores base
        Grafo dois = new Grafo(propriedades);

        // Criação do grafo de propriedades (baseado em interseções)
        Graph<Propriedade, DefaultEdge> grafopropriedade = dois.propriedade();

        // Criação do grafo de proprietários
        Graph<String, DefaultEdge> grafoProprietarios = dois.grafoProprietarios(grafopropriedade);

        // Filtra apenas os primeiros 50 proprietários para visualização
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

        // Visualização do grafo de proprietários filtrado
        GrafoVisual.visualize(subGraph);

        // Cálculo da área média das propriedades de uma determinada freguesia
        String freguesia = "Ponta do Sol";
        double media = Grafo.areaMedia(propriedades, "freguesia", freguesia);
        System.out.printf("Área média das propriedades em " + freguesia + ": %.2f m²%n", media);

        double mediaUnificada = Grafo.areaMediaUnificada(propriedades, "freguesia", freguesia);
        System.out.printf("Área média das propriedades em %s (com união): %.2f m²%n", freguesia, mediaUnificada);

        // Sugestões de troca versão original
        List<SugestaoTroca> trocas = Grafo.sugerirTrocas(propriedades, "freguesia", freguesia);
        System.out.println("\n--- Sugestões de troca (versão original) ---");
        if (trocas.isEmpty()) {
            System.out.println("⚠️ Nenhuma sugestão de troca encontrada.");
        } else {
            trocas.stream().limit(5).forEach(System.out::println);
        }

        // Sugestões de troca versão com valor de terreno
        List<SugestaoTrocaV2> trocasV2 = Grafo.sugerirTrocasV2(propriedades, "freguesia", freguesia);
        System.out.println("\n--- Sugestões de troca (versão com valor de terreno) ---");
        if (trocasV2.isEmpty()) {
            System.out.println("⚠️ Nenhuma sugestão de troca encontrada.");
        } else {
            trocasV2.stream().limit(5).forEach(System.out::println);
        }
    }
}
