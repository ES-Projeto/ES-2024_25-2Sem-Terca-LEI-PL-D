import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GrafoVisualTest {

    @Test
    void testVisualizeWithEmptyGraph() {
        var emptyGraph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
        assertDoesNotThrow(() -> GrafoVisual.visualize(emptyGraph));
    }

    @Test
    void testVisualizeWithValidGraph() {
        var graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        assertDoesNotThrow(() -> GrafoVisual.visualize(graph));
    }
}