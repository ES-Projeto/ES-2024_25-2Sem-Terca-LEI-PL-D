import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import static org.junit.jupiter.api.Assertions.*;

public class PropriedadeTest {

    @Test
    public void testGettersAndToString() throws Exception {
        WKTReader reader = new WKTReader();
        Geometry geometry = reader.read("POINT(1 1)");

        Propriedade p = new Propriedade(
                10, 20 ,123456L,
                100.5,200.5,geometry,
                "João","Freguesia X",
                "Município Y","Ilha Z");

        // Testando os getters
        assertEquals(10, p.getObjectid());
        assertEquals(20, p.getPar_id());
        assertEquals(123456L, p.getPar_num());
        assertEquals(100.5, p.getShapeArea());
        assertEquals(200.5, p.getShapeLength());
        assertEquals("João", p.getOwner());
        assertEquals("Freguesia X", p.getFreguesia());
        assertEquals("Município Y", p.getMunicipio());
        assertEquals("Ilha Z", p.getIlha());
        assertEquals("POINT (1 1)", p.getGeometry().toText());

        // Testando o toString
        String output = p.toString();
        assertTrue(output.contains("Objectid=10"));
        assertTrue(output.contains("par_id=20"));
        assertTrue(output.contains("par_num=123456"));
        assertTrue(output.contains("shapeArea=100.5"));
        assertTrue(output.contains("shapeLength=200.5"));
        assertTrue(output.contains("geometry=POINT (1 1)"));
        assertTrue(output.contains("owner='João'"));
        assertTrue(output.contains("freguesia='Freguesia X'"));
        assertTrue(output.contains("municipio='Município Y'"));
        assertTrue(output.contains("ilha='Ilha Z'"));
    }
}
