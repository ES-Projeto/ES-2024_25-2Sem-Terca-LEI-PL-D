import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ValorTerrenoCalculator {

    private static final Map<String, Double> precoBasePorFreguesia = new HashMap<>();

    // Deve ser chamado uma vez após o carregamento das propriedades
    public static void inicializarPrecosBase(List<Propriedade> propriedades) {
        Random rand = new Random(42); // Seed fixa
        for (Propriedade p : propriedades) {
            String freguesia = p.getFreguesia();
            if (!precoBasePorFreguesia.containsKey(freguesia)) {
                precoBasePorFreguesia.put(freguesia, 5 + rand.nextDouble() * 45); // 5-50 €/m²
            }
        }
    }

    public static double calcularValor(Propriedade p) {
        double base = precoBasePorFreguesia.getOrDefault(p.getFreguesia(), 20.0);
        double area = p.getShapeArea();
        double fatorLoteavel = p.isLoteavel() ? 1.2 : 1.0;
        double fatorAcesso = 1 + 0.05 * (p.getAcesso() - 5);

        return area * base * fatorLoteavel * fatorAcesso;
    }

    public static double getPrecoBase(String freguesia) {
        return precoBasePorFreguesia.getOrDefault(freguesia, 20.0);
    }
}
