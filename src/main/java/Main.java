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

        // Cálculo da área média das propriedades de uma determinada freguesia~
        String freguesia = "Arco da Calheta";
        double media = Grafo.areaMedia(propriedades, "freguesia", freguesia);
        System.out.printf("Área média das propriedades em " + freguesia + ": %.2f m²%n", media);
    }
}
