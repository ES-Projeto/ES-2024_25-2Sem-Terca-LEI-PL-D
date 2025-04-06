import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.locationtech.jts.index.strtree.STRtree;
import java.util.List;

public class Grafo {
    private List<Propriedade> propriedades;

    public Grafo(List<Propriedade> propriedades) {
        this.propriedades = propriedades;
    }

    public Graph<Propriedade, DefaultEdge> propriedade() {
        Graph<Propriedade, DefaultEdge> grafo = new SimpleGraph<>(DefaultEdge.class);
        STRtree tree = new STRtree();
        for (Propriedade p : propriedades) {
            grafo.addVertex(p);
            tree.insert(p.getGeometry().getEnvelopeInternal(), p);
        }
        for (Propriedade p : propriedades) {
            List<Propriedade> candidatos = (List<Propriedade>) tree.query(p.getGeometry().getEnvelopeInternal());
            for (Propriedade c : candidatos) {
                if (p.getPar_id() == c.getPar_id()) continue;
                if (p.getPar_id() < c.getPar_id()) {
                    if (p.getGeometry().intersects(c.getGeometry())) {
                        grafo.addEdge(p, c);
                    }
                }
            }
        }
        return grafo;
    }

    public static <V, E> void printText(Graph<V, E> grafo) {
        for (V vertice : grafo.vertexSet()) {
            System.out.println("Nó: " + vertice);
            for (V vizinho : Graphs.neighborListOf(grafo, vertice)) {
                System.out.println("   → vizinho: " + vizinho);
            }
            System.out.println();
        }
    }

}
