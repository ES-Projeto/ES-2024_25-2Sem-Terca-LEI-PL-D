import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVHandlerTest {

    private static final String TEMP_CSV = "test_propriedades.csv";

    @BeforeEach
    public void setupCSV() throws IOException {
        try (FileWriter writer = new FileWriter(TEMP_CSV)) {
            writer.write("OBJECTID;PAR_ID;PAR_NUM;Shape_Length;Shape_Area;geometry;OWNER;Freguesia;Municipio;Ilha\n");
            writer.write("1;101;1001;123.45;678.90;POLYGON ((0 0, 1 0, 1 1, 0 1, 0 0));John Doe;Funchal;Funchal;Madeira\n");
        }
    }

    @Test
    public void testGetPropriedadesSuccessfullyParsesCSV() {
        CSVHandler handler = new CSVHandler(TEMP_CSV);
        List<Propriedade> propriedades = handler.getPropriedades();

        assertEquals(1, propriedades.size());

        Propriedade p = propriedades.get(0);
        assertEquals(1, p.getObjectid());
        assertEquals(101, p.getPar_id());
        assertEquals(1001, p.getPar_num());
        assertEquals(123.45, p.getShapeLength(), 0.01);
        assertEquals(678.90, p.getShapeArea(), 0.01);
        assertEquals("John Doe", p.getOwner());
        assertEquals("Funchal", p.getFreguesia());
        assertEquals("Funchal", p.getMunicipio());
        assertEquals("Madeira", p.getIlha());

        Geometry g = p.getGeometry();
        assertNotNull(g);
        assertTrue(g.isValid());
    }
}