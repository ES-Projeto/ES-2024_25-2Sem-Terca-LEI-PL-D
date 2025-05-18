import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


import java.util.*;
import java.util.stream.Collectors;

public class Grafo {

    private List<Propriedade> propriedades;

    public Grafo(List<Propriedade> propriedades) {
        this.propriedades = propriedades;
    }

    // Método que cria grafo de propriedades baseado em interseções (simplificado)
    public Graph<Propriedade, DefaultEdge> propriedade() {
        Graph<Propriedade, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (Propriedade p : propriedades) {
            graph.addVertex(p);
        }
        // Lógica para adicionar arestas (exemplo simplificado)
        for (Propriedade p1 : propriedades) {
            for (Propriedade p2 : propriedades) {
                if (!p1.equals(p2) && p1.getGeometry().intersects(p2.getGeometry())) {
                    graph.addEdge(p1, p2);
                }
            }
        }
        return graph;
    }

    // Método que cria grafo de proprietários baseado no grafo de propriedades
    public Graph<String, DefaultEdge> grafoProprietarios(Graph<Propriedade, DefaultEdge> grafopropriedade) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (Propriedade p : grafopropriedade.vertexSet()) {
            graph.addVertex(p.getOwner());
        }
        for (DefaultEdge e : grafopropriedade.edgeSet()) {
            Propriedade src = grafopropriedade.getEdgeSource(e);
            Propriedade tgt = grafopropriedade.getEdgeTarget(e);
            String ownerSrc = src.getOwner();
            String ownerTgt = tgt.getOwner();
            if (!ownerSrc.equals(ownerTgt)) {
                graph.addEdge(ownerSrc, ownerTgt);
            }
        }
        return graph;
    }

    // Cálculo da área média simples para um tipo e valor (exemplo para freguesia)
    public static double areaMedia(List<Propriedade> propriedades, String tipo, String valor) {
        List<Propriedade> filtradas = propriedades.stream()
                .filter(p -> {
                    switch (tipo.toLowerCase()) {
                        case "freguesia":
                            return p.getFreguesia().equalsIgnoreCase(valor);
                        case "municipio":
                            return p.getMunicipio().equalsIgnoreCase(valor);
                        case "ilha":
                            return p.getIlha().equalsIgnoreCase(valor);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());

        if (filtradas.isEmpty()) return 0;

        return filtradas.stream().mapToDouble(Propriedade::getShapeArea).average().orElse(0);
    }

    // Área média unificada (exemplo simplificado)
    public static double areaMediaUnificada(List<Propriedade> propriedades, String tipo, String valor) {
        // Aqui supomos unir geometria e calcular área total (simplificação)
        List<Propriedade> filtradas = propriedades.stream()
                .filter(p -> {
                    switch (tipo.toLowerCase()) {
                        case "freguesia":
                            return p.getFreguesia().equalsIgnoreCase(valor);
                        case "municipio":
                            return p.getMunicipio().equalsIgnoreCase(valor);
                        case "ilha":
                            return p.getIlha().equalsIgnoreCase(valor);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());

        if (filtradas.isEmpty()) return 0;

        double totalArea = filtradas.stream().mapToDouble(Propriedade::getShapeArea).sum();
        return totalArea / filtradas.size();
    }

    // Método para calcular a área média unificada de um subgrupo de propriedades (útil para sugerir trocas)
    public static double areaMediaUnificadaSubgrupo(List<Propriedade> grupo) {
        if (grupo.isEmpty()) return 0;
        double totalArea = grupo.stream().mapToDouble(Propriedade::getShapeArea).sum();
        return totalArea / grupo.size();
    }

    // Método para sugerir trocas usando SugestaoTrocaV2 (adaptado ao teu código)
    public static List<SugestaoTrocaV2> sugerirTrocasV2(List<Propriedade> propriedades, String tipo, String valor) {
        List<Propriedade> filtradas = propriedades.stream()
                .filter(p -> {
                    switch (tipo.toLowerCase()) {
                        case "freguesia":
                            return p.getFreguesia().equalsIgnoreCase(valor);
                        case "municipio":
                            return p.getMunicipio().equalsIgnoreCase(valor);
                        case "ilha":
                            return p.getIlha().equalsIgnoreCase(valor);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());

        List<SugestaoTrocaV2> sugestoes = new ArrayList<>();
        int tentativas = 0;
        int limite = 500;

        for (Propriedade a : filtradas) {
            for (Propriedade b : filtradas) {
                if (tentativas++ > limite) break;
                if (a.getPar_id() == b.getPar_id()) continue;
                if (!a.getOwner().equals(b.getOwner())) {

                    String donoA = a.getOwner();
                    String donoB = b.getOwner();

                    // Subconjunto só com os dois donos
                    List<Propriedade> grupoOriginal = filtradas.stream()
                            .filter(p -> p.getOwner().equals(donoA) || p.getOwner().equals(donoB))
                            .collect(Collectors.toList());

                    double antes = areaMediaUnificadaSubgrupo(grupoOriginal);

                    // Simula a troca
                    a.setOwner(donoB);
                    b.setOwner(donoA);

                    double depois = areaMediaUnificadaSubgrupo(grupoOriginal);

                    // Reverte
                    a.setOwner(donoA);
                    b.setOwner(donoB);

                    double ganho = depois - antes;

                    if (ganho > 0.01) {
                        double valorTotalA = a.getShapeArea() * ValorTerrenoCalculator.getPrecoBase(a.getFreguesia());
                        double valorTotalB = b.getShapeArea() * ValorTerrenoCalculator.getPrecoBase(b.getFreguesia());

                        double deltaValor = Math.abs(valorTotalA - valorTotalB);
                        double qualidade = ganho / (1 + deltaValor);

                        sugestoes.add(new SugestaoTrocaV2(a, b, ganho, qualidade, valorTotalA, valorTotalB));
                    }
                }
            }
        }

        sugestoes.sort(Comparator.comparingDouble(SugestaoTrocaV2::getQualidade).reversed());
        return sugestoes;
    }
}
