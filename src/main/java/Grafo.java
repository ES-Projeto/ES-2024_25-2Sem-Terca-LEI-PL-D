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

    public Graph<String, DefaultEdge> grafoProprietarios(Graph<Propriedade, DefaultEdge> grafoPropriedades) {
    Graph<String, DefaultEdge> grafo = new SimpleGraph<>(DefaultEdge.class);

    for (DefaultEdge edge : grafoPropriedades.edgeSet()) {
        Propriedade p1 = grafoPropriedades.getEdgeSource(edge);
        Propriedade p2 = grafoPropriedades.getEdgeTarget(edge);

        String owner1 = p1.getOwner();
        String owner2 = p2.getOwner();

        if (!owner1.equals(owner2)) {
            grafo.addVertex(owner1);
            grafo.addVertex(owner2);
            grafo.addEdge(owner1, owner2);
        }
    }

    return grafo;
    }

    public static double areaMedia(List<Propriedade> propriedades, String tipo, String valor) {
            return propriedades.stream()
                .filter(p -> {
                    return switch (tipo.toLowerCase()) {
                        case "freguesia" -> p.getFreguesia().equalsIgnoreCase(valor);
                        case "municipio" -> p.getMunicipio().equalsIgnoreCase(valor);
                        case "ilha" -> p.getIlha().equalsIgnoreCase(valor);
                        default -> false;
                    };
                })
                .mapToDouble(Propriedade::getShapeArea)
                .average()
                .orElse(0.0);
    }


}
