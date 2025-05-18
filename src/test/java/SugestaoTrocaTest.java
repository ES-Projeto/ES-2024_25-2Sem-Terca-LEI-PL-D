import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import static org.junit.jupiter.api.Assertions.*;

public class SugestaoTrocaTest {

    private Propriedade propA;
    private Propriedade propB;
    private SugestaoTroca sugestao;

    @BeforeEach
    void setup() throws Exception {
        WKTReader reader = new WKTReader();
        Geometry g1 = reader.read("POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))");
        Geometry g2 = reader.read("POLYGON((2 0, 3 0, 3 1, 2 1, 2 0))");

        propA = new Propriedade(1, 1001, 1111L, g1.getArea(), g1.getLength(), g1, "Ana", "F1", "M1", "I1");
        propB = new Propriedade(2, 1002, 2222L, g2.getArea(), g2.getLength(), g2, "Bruno", "F1", "M1", "I1");

        sugestao = new SugestaoTroca(propA, propB, 1.25, 0.95);
    }

    @Test
    void testGetters() {
        assertEquals(propA, sugestao.getPropA());
        assertEquals(propB, sugestao.getPropB());
        assertEquals(1.25, sugestao.getGanhoTotal(), 0.01);
        assertEquals(0.95, sugestao.getPotencial(), 0.01);
    }

    @Test
    void testSetOwners() {
        sugestao.setOwnerA("Carlos");
        sugestao.setOwnerB("Diana");

        assertEquals("Carlos", propA.getOwner());
        assertEquals("Diana", propB.getOwner());
    }

    @Test
    void testToStringContainsInfo() {
        String output = sugestao.toString();
        assertTrue(output.contains("Proprietário Ana"));
        assertTrue(output.contains("Proprietário Bruno"));
        assertTrue(output.contains("Ganho médio total"));
        assertTrue(output.contains("Potencial de troca"));
    }
}