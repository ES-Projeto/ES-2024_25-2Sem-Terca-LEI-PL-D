import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrafoTest {

    private List<Propriedade> propriedades;
    private Propriedade prop1, prop2, prop3;
    private Grafo grafo;

    @BeforeEach
    void setUp() throws Exception {
        propriedades = new ArrayList<>();
        WKTReader reader = new WKTReader();
        Geometry g1 = reader.read("POLYGON((0 0, 2 0, 2 2, 0 2, 0 0))");
        Geometry g2 = reader.read("POLYGON((2 0, 4 0, 4 2, 2 2, 2 0))");
        Geometry g3 = reader.read("POLYGON((5 5, 7 5, 7 7, 5 7, 5 5))");

        prop1 = new Propriedade(1, 1, 1, g1.getArea(), g1.getLength(), g1, "A", "F1", "M1", "I1");
        prop2 = new Propriedade(2, 2, 2, g2.getArea(), g2.getLength(), g2, "B", "F1", "M1", "I1");
        prop3 = new Propriedade(3, 3, 3, g3.getArea(), g3.getLength(), g3, "A", "F1", "M1", "I1");

        propriedades.add(prop1);
        propriedades.add(prop2);
        propriedades.add(prop3);

        grafo = new Grafo(propriedades);
    }

    @Test
    void testPropriedadeGraphCreation() {
        Graph<Propriedade, DefaultEdge> graph = grafo.propriedade();
        assertEquals(3, graph.vertexSet().size());
        assertEquals(1, graph.edgeSet().size()); // prop1 e prop2 intersectam-se
    }

    @Test
    void testGrafoProprietarios() {
        Graph<Propriedade, DefaultEdge> propertyGraph = grafo.propriedade();
        Graph<String, DefaultEdge> ownerGraph = grafo.grafoProprietarios(propertyGraph);
        assertTrue(ownerGraph.containsVertex("A"));
        assertTrue(ownerGraph.containsVertex("B"));
        assertEquals(1, ownerGraph.edgeSet().size());
    }

    @Test
    void testAreaMedia() {
        double media = Grafo.areaMedia(propriedades, "freguesia", "F1");
        assertTrue(media > 0);
    }

    @Test
    void testAreaMediaUnificada() {
        double media = Grafo.areaMediaUnificada(propriedades, "freguesia", "F1");
        assertTrue(media > 0);
    }

    @Test
    void testSugerirTrocas() {
        List<SugestaoTroca> trocas = Grafo.sugerirTrocas(propriedades, "freguesia", "F1");
        assertNotNull(trocas);
    }
}