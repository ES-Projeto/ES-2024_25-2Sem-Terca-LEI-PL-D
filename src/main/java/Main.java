import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String csvFileName = "Madeira-Moodle-1.1.csv";

        // Load CSV and create property graph
        CSVHandler teste = new CSVHandler(csvFileName);
        List<Propriedade> propriedades = teste.getPropriedades();
        Grafo dois = new Grafo(propriedades);

        Graph<Propriedade, DefaultEdge> grafopropriedade = dois.propriedade();
        // GrafoVisual.visualize(grafopropriedade); // Optional

        // Build full owner graph
        Graph<String, DefaultEdge> grafoProprietarios = dois.grafoProprietarios(grafopropriedade);

        // 🔍 Filter: keep only the first 50 owners for visualization
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

        // ✅ Visualize simplified graph
        GrafoVisual.visualize(subGraph);
    }
}
