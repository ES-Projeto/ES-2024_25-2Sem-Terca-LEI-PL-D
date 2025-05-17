import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.locationtech.jts.index.strtree.STRtree;
import java.util.*;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;

public class Grafo {
    private List<Propriedade> propriedades;

    /**
     * Construtor da classe Grafo.
     *
     * @param propriedades lista de propriedades a serem usadas na construção do grafo
     */
    public Grafo(List<Propriedade> propriedades) {
        this.propriedades = propriedades;
    }

    /**
     * Cria um grafo onde cada nó representa uma propriedade e cada aresta representa uma interseção entre geometrias.
     *
     * @return grafo com as propriedades e suas conexões por interseção
     */
    public Graph<Propriedade, DefaultEdge> propriedade() {
        Graph<Propriedade, DefaultEdge> grafo = new SimpleGraph<>(DefaultEdge.class);
        STRtree tree = new STRtree();
        for (Propriedade p : propriedades) {
            grafo.addVertex(p);
            tree.insert(p.getGeometry().getEnvelopeInternal(), p);
        }
        for (Propriedade p : propriedades) {
            List<Propriedade> candidatos = (List<Propriedade>) tree.query(p.getGeometry().getEnvelopeInternal());
            for (Propriedade c : candidatos) {
                if (p.getPar_id() == c.getPar_id()) continue;
                if (p.getPar_id() < c.getPar_id()) {
                    if (p.getGeometry().intersects(c.getGeometry())) {
                        grafo.addEdge(p, c);
                    }
                }
            }
        }
        return grafo;
    }

    /**
     * Imprime o grafo no terminal, listando cada nó e seus vizinhos.
     *
     * @param grafo o grafo a ser impresso
     * @param <V> tipo dos vértices
     * @param <E> tipo das arestas
     */
    public static <V, E> void printText(Graph<V, E> grafo) {
        for (V vertice : grafo.vertexSet()) {
            System.out.println("Nó: " + vertice);
            for (V vizinho : Graphs.neighborListOf(grafo, vertice)) {
                System.out.println("   → vizinho: " + vizinho);
            }
            System.out.println();
        }
    }

    public Graph<String, DefaultEdge> grafoProprietarios(Graph<Propriedade, DefaultEdge> grafoPropriedades) {
    Graph<String, DefaultEdge> grafo = new SimpleGraph<>(DefaultEdge.class);

    for (DefaultEdge edge : grafoPropriedades.edgeSet()) {
        Propriedade p1 = grafoPropriedades.getEdgeSource(edge);
        Propriedade p2 = grafoPropriedades.getEdgeTarget(edge);

        String owner1 = p1.getOwner();
        String owner2 = p2.getOwner();

        if (!owner1.equals(owner2)) {
            grafo.addVertex(owner1);
            grafo.addVertex(owner2);
            grafo.addEdge(owner1, owner2);
        }
    }

    return grafo;
    }

    public static double areaMedia(List<Propriedade> propriedades, String tipo, String valor) {
            return propriedades.stream()
                .filter(p -> {
                    return switch (tipo.toLowerCase()) {
                        case "freguesia" -> p.getFreguesia().equalsIgnoreCase(valor);
                        case "municipio" -> p.getMunicipio().equalsIgnoreCase(valor);
                        case "ilha" -> p.getIlha().equalsIgnoreCase(valor);
                        default -> false;
                    };
                })
                .mapToDouble(Propriedade::getShapeArea)
                .average()
                .orElse(0.0);
    }

    public static double areaMediaUnificada(List<Propriedade> propriedades, String tipo, String valor) {
        // 1. Filtrar propriedades da área geográfica indicada
        List<Propriedade> filtradas = propriedades.stream()
                .filter(p -> switch (tipo.toLowerCase()) {
                    case "freguesia" -> p.getFreguesia().equalsIgnoreCase(valor);
                    case "municipio" -> p.getMunicipio().equalsIgnoreCase(valor);
                    case "ilha" -> p.getIlha().equalsIgnoreCase(valor);
                    default -> false;
                }).collect(Collectors.toList());

        // 2. Agrupar por proprietário
        Map<String, List<Propriedade>> porDono = filtradas.stream()
                .collect(Collectors.groupingBy(Propriedade::getOwner));

        List<Geometry> geometriasAgrupadas = new ArrayList<>();

        for (List<Propriedade> props : porDono.values()) {
            List<Geometry> restantes = new ArrayList<>(props.stream().map(Propriedade::getGeometry).toList());

            // Agrupar geometrias adjacentes
            while (!restantes.isEmpty()) {
                Geometry base = restantes.remove(0);
                List<Geometry> grupo = new ArrayList<>();
                grupo.add(base);

                boolean alterado;
                do {
                    alterado = false;
                    Iterator<Geometry> it = restantes.iterator();
                    while (it.hasNext()) {
                        Geometry g = it.next();
                        if (grupo.stream().anyMatch(baseG -> baseG.touches(g) || baseG.intersects(g))) {
                            grupo.add(g);
                            it.remove();
                            alterado = true;
                        }
                    }
                } while (alterado);

                // União do grupo
                Geometry unida = CascadedPolygonUnion.union(grupo);
                geometriasAgrupadas.add(unida);
            }
        }

        // 3. Calcular média das áreas unificadas
        double soma = geometriasAgrupadas.stream().mapToDouble(Geometry::getArea).sum();
        int total = geometriasAgrupadas.size();

        return total == 0 ? 0.0 : soma / total;
    }

    public static List<SugestaoTroca> sugerirTrocas(List<Propriedade> propriedades, String tipo, String valor) {
        List<Propriedade> filtradas = propriedades.stream()
                .filter(p -> switch (tipo.toLowerCase()) {
                    case "freguesia" -> p.getFreguesia().equalsIgnoreCase(valor);
                    case "municipio" -> p.getMunicipio().equalsIgnoreCase(valor);
                    case "ilha" -> p.getIlha().equalsIgnoreCase(valor);
                    default -> false;
                })
                .toList();

        List<SugestaoTroca> sugestoes = new ArrayList<>();
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
                        double dif = Math.abs(a.getShapeArea() - b.getShapeArea());
                        // Formula menos restrita em relação ao ganho mas menos realista: double potencial = 1.0 / (1.0 + Mathsqrt(dif));
                        double potencial = 1.0 / (1.0 + dif);
                        sugestoes.add(new SugestaoTroca(a, b, ganho, potencial));
                    }
                }
            }
        }

        sugestoes.sort(Comparator.comparingDouble(s -> -(s.getGanhoTotal() * s.getPotencial())));
        return sugestoes;
    }

    private static double areaMediaUnificadaSubgrupo(List<Propriedade> propriedades) {
        Map<String, List<Propriedade>> porDono = propriedades.stream()
                .collect(Collectors.groupingBy(Propriedade::getOwner));

        List<Geometry> geometriasAgrupadas = new ArrayList<>();

        for (List<Propriedade> props : porDono.values()) {
            List<Geometry> restantes = new ArrayList<>(props.stream().map(Propriedade::getGeometry).toList());

            while (!restantes.isEmpty()) {
                Geometry base = restantes.remove(0);
                List<Geometry> grupo = new ArrayList<>();
                grupo.add(base);

                boolean alterado;
                do {
                    alterado = false;
                    Iterator<Geometry> it = restantes.iterator();
                    while (it.hasNext()) {
                        Geometry g = it.next();
                        if (grupo.stream().anyMatch(baseG -> baseG.touches(g) || baseG.intersects(g))) {
                            grupo.add(g);
                            it.remove();
                            alterado = true;
                        }
                    }
                } while (alterado);

                Geometry unida = CascadedPolygonUnion.union(grupo);
                geometriasAgrupadas.add(unida);
            }
        }

        double soma = geometriasAgrupadas.stream().mapToDouble(Geometry::getArea).sum();
        int total = geometriasAgrupadas.size();

        return total == 0 ? 0.0 : soma / total;
    }


}
