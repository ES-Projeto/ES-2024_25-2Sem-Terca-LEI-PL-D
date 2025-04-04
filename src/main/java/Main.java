public class Main {
    public static void main(String[] args) {
        String csvFileName = "Madeira-Moodle-1.1.csv";
        CSVHandler teste = new CSVHandler(csvFileName);
        teste.teste(); //Leitura de ficheiros CSV
    }
}