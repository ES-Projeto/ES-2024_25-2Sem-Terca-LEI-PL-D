import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class CSVHandler {
    private String csvFileName;

    public CSVHandler(String csvFileName) {
        this.csvFileName = csvFileName;
    }


    // Teste de leitura de ficheiros CSV
    public void teste(){
    try (CSVReader reader = new CSVReader(new FileReader(csvFileName))){
        String[] nextLine;
        while((nextLine = reader.readNext()) != null){
            System.out.println(Arrays.toString(nextLine));
        }
    } catch (IOException | CsvValidationException e){
        System.out.println("failure");
    }
   }



}