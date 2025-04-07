import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class CSVHandlerTest {

    @Test
    public void testGetPropriedades() throws IOException {
        String csvFileName = "test.csv";
        try (FileWriter writer = new FileWriter(csvFileName)) {
            writer.write("OBJECTID;PAR_ID;PAR_NUM;Shape_Length;Shape_Area;geometry;OWNER;Freguesia;Municipio;Ilha\n");
            writer.write("1;1;1234567890;10.0;20.0;POINT(1 1);Owner1;Freguesia1;Municipio1;Ilha1\n");
            writer.write("2;2;9876543210;15.0;30.0;POINT(2 2);Owner2;Freguesia2;Municipio2;Ilha2\n");
        }

        try {
            CSVHandler csvHandler = new CSVHandler(csvFileName);
            List<Propriedade> propriedades = csvHandler.getPropriedades();

            assertFalse(propriedades.isEmpty());
            assertEquals(2, propriedades.size());

            Propriedade prop1 = propriedades.get(0);
            assertEquals(1, prop1.getObjectid());
            assertEquals(1, prop1.getPar_id());
            assertEquals(1234567890L, prop1.getPar_num());
            assertEquals(10.0, prop1.getShapeLength());
            assertEquals(20.0, prop1.getShapeArea());
            assertEquals("Owner1", prop1.getOwner());
            assertEquals("Freguesia1", prop1.getFreguesia());
            assertEquals("Municipio1", prop1.getMunicipio());
            assertEquals("Ilha1", prop1.getIlha());

        } catch (Exception e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }

        java.io.File file = new java.io.File(csvFileName);
        boolean deleted = file.delete();
        assertTrue(deleted, "Temporary file should be deleted");
    }
}
