import java.util.List;

public class Main {
    public static void main(String[] args) {
        String csvFileName = "Madeira-Moodle-1.1.csv";
        CSVHandler teste = new CSVHandler(csvFileName);
        List<Propriedade> propriedades = teste.getPropriedades();
        Grafo dois = new Grafo(propriedades);
        dois.print(dois.propriedade());

    }
}