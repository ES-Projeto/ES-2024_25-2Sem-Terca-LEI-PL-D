import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testGetPropriedadesComParseException() throws IOException {
        try (FileWriter writer = new FileWriter(TEMP_CSV)) {
            writer.write("OBJECTID;PAR_ID;PAR_NUM;Shape_Length;Shape_Area;geometry;OWNER;Freguesia;Municipio;Ilha\n");
            writer.write("1;abc;1001;123.45;678.90;POLYGON ((0 0, 1 0, 1 1, 0 1, 0 0));John Doe;Funchal;Funchal;Madeira\n");
        }

        CSVHandler handler = new CSVHandler(TEMP_CSV);
        assertThrows(RuntimeException.class, handler::getPropriedades);
    }

    @Test
    public void testGetPrecosFreguesia() throws IOException {
        try (FileWriter writer = new FileWriter(TEMP_CSV)) {
            writer.write("OBJECTID;PAR_ID;PAR_NUM;Shape_Length;Shape_Area;geometry;OWNER;Freguesia;Municipio;Ilha\n");
            writer.write("1;101;1001;123.45;678.90;POLYGON ((0 0, 1 0, 1 1, 0 1, 0 0));John Doe;Funchal;Funchal;Madeira\n");
            writer.write("2;102;1002;200.00;900.00;POLYGON ((0 0, 2 0, 2 2, 0 2, 0 0));Jane Smith;Machico;Machico;Madeira\n");
            writer.write("3;103;1003;250.00;1000.00;POLYGON ((0 0, 3 0, 3 3, 0 3, 0 0));Joe Bloggs;Funchal;Funchal;Madeira\n");
        }

        CSVHandler handler = new CSVHandler(TEMP_CSV);
        Map<String, Double> precos = handler.getPrecosFreguesia();

        assertEquals(2, precos.size());
        assertTrue(precos.containsKey("Funchal"));
        assertTrue(precos.containsKey("Machico"));

        double precoFunchal = precos.get("Funchal");
        double precoMachico = precos.get("Machico");

        assertTrue(precoFunchal >= 200 && precoFunchal <= 2000);
        assertTrue(precoMachico >= 200 && precoMachico <= 2000);
    }

    @Test
    public void testCSVSemDados() throws IOException {
        try (FileWriter writer = new FileWriter(TEMP_CSV)) {
            writer.write("OBJECTID;PAR_ID;PAR_NUM;Shape_Length;Shape_Area;geometry;OWNER;Freguesia;Municipio;Ilha\n");
        }

        CSVHandler handler = new CSVHandler(TEMP_CSV);

        List<Propriedade> propriedades = handler.getPropriedades();
        assertTrue(propriedades.isEmpty());

        Map<String, Double> precos = handler.getPrecosFreguesia();
        assertTrue(precos.isEmpty());
    }
}