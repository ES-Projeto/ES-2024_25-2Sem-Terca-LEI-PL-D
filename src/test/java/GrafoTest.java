import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GrafoTest {

    @Test
    public void testPropriedadeGraphBuilding() throws Exception {
        WKTReader reader = new WKTReader();

        //Geometrias que se intersectam
        Geometry geom1 = reader.read("POLYGON((0 0, 2 0, 2 2, 0 2, 0 0))");
        Geometry geom2 = reader.read("POLYGON((1 1, 3 1, 3 3, 1 3, 1 1))");

        //Geometria que não se intersecta
        Geometry geom3 = reader.read("POLYGON((10 10, 12 10, 12 12, 10 12, 10 10))");

        //Criação de Propriedades
        Propriedade p1 = new Propriedade(1, 101, 1001L, 4.0, 8.0, geom1, "Owner1", "Freg1", "Mun1", "Ilha1");
        Propriedade p2 = new Propriedade(2, 102, 1002L, 4.0, 8.0, geom2, "Owner2", "Freg2", "Mun2", "Ilha2");
        Propriedade p3 = new Propriedade(3, 103, 1003L, 4.0, 8.0, geom3, "Owner3", "Freg3", "Mun3", "Ilha3");

        //Lista de Propriedades
        List<Propriedade> propriedades = Arrays.asList(p1, p2, p3);

        //Criação do Grafo
        Grafo grafoBuild = new Grafo(propriedades);
        Graph<Propriedade, DefaultEdge> grafo = grafoBuild.propriedade();

        // Verifica número de nós
        assertEquals(3, grafo.vertexSet().size());

        // Verifica arestas: p1 e p2 intersectam-se ou seja devem ter uma aresta
        assertTrue(grafo.containsEdge(p1,p2) || grafo.containsEdge(p2,p1));

        //p3 não intersecta com p1 e p2, logo não deve ter arestas
        assertFalse(grafo.containsEdge(p1,p3));
        assertFalse(grafo.containsEdge(p3,p2));
    }
}
