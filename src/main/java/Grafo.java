import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.locationtech.jts.index.strtree.STRtree;
import java.util.List;

public class Grafo {
    private List<Propriedade> propriedades;

    /**
     * Construtor da classe Grafo.
     *
     * @param propriedades lista de propriedades a serem usadas na construção do grafo
     */
    public Grafo(List<Propriedade> propriedades) {
        this.propriedades = propriedades;
    }

    /**
     * Cria um grafo onde cada nó representa uma propriedade e cada aresta representa uma interseção entre geometrias.
     *
     * @return grafo com as propriedades e suas conexões por interseção
     */
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

    /**
     * Imprime o grafo no terminal, listando cada nó e seus vizinhos.
     *
     * @param grafo o grafo a ser impresso
     * @param <V> tipo dos vértices
     * @param <E> tipo das arestas
     */
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
