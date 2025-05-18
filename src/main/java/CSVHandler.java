import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe responsável por ler um ficheiro CSV contendo dados de propriedades geográficas
 * e converter essas informações em objetos {@link Propriedade}.
 */
public class CSVHandler {
    private String csvFileName;

    /**
     * Construtor da classe CSVHandler.
     *
     * @param csvFileName nome (ou caminho) do ficheiro CSV a ser lido
     */
    public CSVHandler(String csvFileName) {
        this.csvFileName = csvFileName;
    }

    /**
     * Lê o ficheiro CSV e retorna uma lista de objetos {@link Propriedade}.
     *
     * @return lista de propriedades lidas do ficheiro CSV
     */
    public List<Propriedade> getPropriedades() {
        List<Propriedade> propriedades = new ArrayList<>();
        try (CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';').parse(new FileReader(csvFileName))) {
            WKTReader reader = new WKTReader();
            for (CSVRecord record : parser) {
                try {
                    int objectId = Integer.parseInt(record.get("OBJECTID"));
                    int par_id = (int) Double.parseDouble(record.get("PAR_ID"));
                    long par_num = (long) Double.parseDouble(record.get("PAR_NUM").replace(",", "."));
                    double shapeLenght = Double.parseDouble(record.get("Shape_Length"));
                    double shapeArea = Double.parseDouble(record.get("Shape_Area"));
                    Geometry geometry = reader.read(record.get("geometry"));
                    String owner = record.get("OWNER");
                    String freguesia = record.get("Freguesia");
                    String municipio = record.get("Municipio");
                    String ilha = record.get("Ilha");
                    propriedades.add(new Propriedade(objectId, par_id, par_num, shapeArea, shapeLenght, geometry, owner, freguesia, municipio, ilha));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println("falha ao carregar propriedades");
        }
        return propriedades;
    }


    /**
     * Obtém um mapa de preços médios por metro quadrado para cada freguesia,
     * lendo os dados de um ficheiro CSV e gerando valores aleatórios entre 200 e 2000 euros.
     * Cada freguesia terá um preço único gerado uma única vez durante a leitura.
     *
     * @return HashMap onde a chave é o nome da freguesia e o valor é o preço médio por metro quadrado arredondado a duas casas decimais.
     */
    public HashMap<String, Double> getPrecosFreguesia() {
        HashMap<String, Double> precos = new HashMap<>();
        java.util.Random random = new java.util.Random();
        try (CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';').parse(new FileReader(csvFileName))) {
            for (CSVRecord record : parser) {
                String freguesia = record.get("Freguesia");
                if (!precos.containsKey(freguesia)) {
                    // Preço por metro quadrado entre 200 e 2000 euros
                    double preco = 200 + (1800 * random.nextDouble());
                    preco = Math.round(preco * 100.0) / 100.0; // arredonda para 2 casas decimais
                    precos.put(freguesia, preco);
                }
            }
        } catch (IOException e) {
            System.out.println("falha ao carregar preços");
        }
        return precos;
    }
}
