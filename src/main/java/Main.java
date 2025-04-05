import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String csvFileName = "Madeira-Moodle-1.1.csv";
        CSVHandler teste = new CSVHandler(csvFileName);
        List<Propriedade> propriedades = teste.getPropriedades();
        for (Propriedade propriedade : propriedades) {
            System.out.println(propriedade);
        }
    }
}