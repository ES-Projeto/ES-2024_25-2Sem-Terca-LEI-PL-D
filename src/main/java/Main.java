import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String csvFileName = "Madeira-Moodle-1.1.csv";
        CSVHandler teste = new CSVHandler(csvFileName);
        List<Propriedade> propriedades = teste.getPropriedades();
        Grafo dois = new Grafo(propriedades);
        Graph<Propriedade, DefaultEdge> grafopropriedade = dois.propriedade();
        dois.printText(grafopropriedade);
    }
}