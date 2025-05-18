import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import static org.junit.jupiter.api.Assertions.*;

public class PropriedadeTest {

    private Propriedade propriedade;
    private Geometry geometry;

    @BeforeEach
    void setup() throws Exception {
        WKTReader reader = new WKTReader();
        geometry = reader.read("POLYGON((0 0, 2 0, 2 2, 0 2, 0 0))");
        propriedade = new Propriedade(1, 123, 456L, 4.0, 8.0, geometry, "Maria", "Funchal", "Funchal", "Madeira");
    }

    @Test
    void testGetters() {
        assertEquals(1, propriedade.getObjectid());
        assertEquals(123, propriedade.getPar_id());
        assertEquals(456L, propriedade.getPar_num());
        assertEquals(4.0, propriedade.getShapeArea(), 0.01);
        assertEquals(8.0, propriedade.getShapeLength(), 0.01);
        assertEquals("Maria", propriedade.getOwner());
        assertEquals("Funchal", propriedade.getFreguesia());
        assertEquals("Funchal", propriedade.getMunicipio());
        assertEquals("Madeira", propriedade.getIlha());
        assertEquals(geometry, propriedade.getGeometry());
    }

    @Test
    void testIsLoteavelAndQualidadeAcessoAreWithinValidRange() {
        Boolean isLoteavel = propriedade.getIsLoteavel();
        int qualidade = propriedade.getQualidadeAcesso();

        assertNotNull(isLoteavel);
        assertTrue(qualidade >= 1 && qualidade <= 10);
    }

    @Test
    void testSetOwner() {
        propriedade.setOwner("João");
        assertEquals("João", propriedade.getOwner());
    }

    @Test
    void testToStringContainsKeyInfo() {
        String output = propriedade.toString();
        assertTrue(output.contains("Objectid=1"));
        assertTrue(output.contains("owner='Maria'")); // valor original no construtor
        assertTrue(output.contains("ilha='Madeira'"));
    }
}