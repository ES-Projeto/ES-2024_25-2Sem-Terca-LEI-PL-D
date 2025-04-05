import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
    private String csvFileName;

    public CSVHandler(String csvFileName) {
        this.csvFileName = csvFileName;
    }



    public List<Propriedade> getPropriedades(){
        List<Propriedade> propriedades = new ArrayList<>();
            try(CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';').parse(new FileReader(csvFileName))){
                WKTReader reader = new WKTReader();
                for(CSVRecord record : parser){
                    try{
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
                        propriedades.add(new Propriedade(objectId, par_id, par_num, shapeArea, shapeLenght, geometry , owner, freguesia, municipio, ilha));
                    }catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }catch(IOException e){
                System.out.println("failure");
            }
            return propriedades;
    }



}