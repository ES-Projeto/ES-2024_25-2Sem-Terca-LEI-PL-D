import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
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
    void testAreaMediaFreguesia() {
        double media = Grafo.areaMedia(propriedades, "freguesia", "F1");
        assertTrue(media > 0);
    }

    @Test
    void testAreaMediaMunicipio() {
        double media = Grafo.areaMedia(propriedades, "municipio", "M1");
        assertTrue(media > 0);
    }

    @Test
    void testAreaMediaIlha() {
        double media = Grafo.areaMedia(propriedades, "ilha", "I1");
        assertTrue(media > 0);
    }

    @Test
    void testAreaMediaTipoInvalido() {
        double media = Grafo.areaMedia(propriedades, "tipo_invalido", "qualquer_valor");
        assertEquals(0.0, media);
    }

    @Test
    void testAreaMediaUnificadaFreguesia() {
        double media = Grafo.areaMediaUnificada(propriedades, "freguesia", "F1");
        assertTrue(media > 0);
    }

    @Test
    void testAreaMediaUnificadaMunicipio() {
        double media = Grafo.areaMediaUnificada(propriedades, "municipio", "M1");
        assertTrue(media > 0);
    }

    @Test
    void testAreaMediaUnificadaIlha() {
        double media = Grafo.areaMediaUnificada(propriedades, "ilha", "I1");
        assertTrue(media > 0);
    }

    @Test
    void testSugerirTrocasComResultados() {
        List<SugestaoTroca> trocas = Grafo.sugerirTrocas(propriedades, "freguesia", "F1");
        assertNotNull(trocas);
    }

    @Test
    void testSugerirTrocasSemTrocas() {
        for (Propriedade p : propriedades) {
            p.setOwner("Z");
        }
        List<SugestaoTroca> trocas = Grafo.sugerirTrocas(propriedades, "freguesia", "F1");
        assertTrue(trocas.isEmpty());
    }

    @Test
    void testPrintTextDoesNotThrow() {
        Graph<Propriedade, DefaultEdge> graph = grafo.propriedade();
        assertDoesNotThrow(() -> Grafo.printText(graph));
    }
    @Test
    void testSugerirTrocasComGanhoIrrelevante() throws Exception {
        WKTReader reader = new WKTReader();
        Geometry g4 = reader.read("POLYGON((10 10, 12 10, 12 12, 10 12, 10 10))");
        Geometry g5 = reader.read("POLYGON((12 10, 14 10, 14 12, 12 12, 12 10))");

        Propriedade p4 = new Propriedade(4, 4, 4, g4.getArea(), g4.getLength(), g4, "X", "F1", "M1", "I1");
        Propriedade p5 = new Propriedade(5, 5, 5, g5.getArea(), g5.getLength(), g5, "Y", "F1", "M1", "I1");

        propriedades.clear(); // limpar anteriores
        propriedades.add(p4);
        propriedades.add(p5);

        List<SugestaoTroca> trocas = Grafo.sugerirTrocas(propriedades, "freguesia", "F1");

        // Mesmo que haja troca, o ganho tem de ser > 0.01 para ser incluída
        assertTrue(trocas.isEmpty());
    }

    @Test
    void testAreaMediaUnificadaTipoInvalido() {
        double media = Grafo.areaMediaUnificada(propriedades, "tipo_invalido", "F1");
        assertEquals(0.0, media);
    }

    }