import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String csvFileName = "Madeira-Moodle-1.1.csv";

        CSVHandler teste = new CSVHandler(csvFileName);
        List<Propriedade> propriedades = teste.getPropriedades();
        Grafo dois = new Grafo(propriedades);

        Graph<Propriedade, DefaultEdge> grafopropriedade = dois.propriedade();

        Graph<String, DefaultEdge> grafoProprietarios = dois.grafoProprietarios(grafopropriedade);

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

        GrafoVisual.visualize(subGraph);

        String freguesia = "Ponta do Sol";
        double media = Grafo.areaMedia(propriedades, "freguesia", freguesia);
        System.out.printf("Área média das propriedades em " + freguesia + ": %.2f m²%n", media);

        double mediaUnificada = Grafo.areaMediaUnificada(propriedades, "freguesia", freguesia);
        System.out.printf("Área média das propriedades em %s (com união): %.2f m²%n", freguesia, mediaUnificada);

        List<SugestaoTroca> trocas = Grafo.sugerirTrocas(propriedades, "freguesia", freguesia);
        System.out.println("Número total de sugestões geradas: " + trocas.size());

        if (trocas.isEmpty()) {
            System.out.println("Nenhuma sugestão de troca encontrada.");
        } else {
            System.out.println("Melhores sugestões de troca:");
            trocas.stream().limit(5).forEach(System.out::println);
        }

    }
}
