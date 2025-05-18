import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por construir e manipular grafos de propriedades e proprietários,
 * bem como calcular métricas e sugerir trocas com base em critérios de otimização.
 */

public class Grafo {
    private List<Propriedade> propriedades;

    /**
     * Construtor da classe Grafo.
     *
     * @param propriedades Lista de propriedades disponíveis no sistema.
     */
    public Grafo(List<Propriedade> propriedades) {
        this.propriedades = propriedades;
    }

    /**
     * Cria um grafo onde os nós são propriedades e as arestas representam interseções entre as suas geometrias.
     *
     * @return Grafo de propriedades baseado na vizinhança geométrica.
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
     * Imprime a representação textual de um grafo, listando os seus nós e vizinhos.
     *
     * @param grafo Grafo a ser impresso.
     * @param <V> Tipo dos vértices.
     * @param <E> Tipo das arestas.
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

    /**
     * Constrói um grafo onde os nós representam proprietários e as arestas representam relações de vizinhança
     * entre suas propriedades.
     *
     * @param grafoPropriedades Grafo de propriedades.
     * @return Grafo com proprietários conectados por vizinhança.
     */
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

    /**
     * Calcula a área média das propriedades filtradas por freguesia, município ou ilha.
     *
     * @param propriedades Lista de propriedades.
     * @param tipo Tipo de filtro ("freguesia", "municipio" ou "ilha").
     * @param valor Valor do filtro.
     * @return Área média das propriedades filtradas.
     */
    public static double areaMedia(List<Propriedade> propriedades, String tipo, String valor) {
        return propriedades.stream()
                .filter(p -> switch (tipo.toLowerCase()) {
                    case "freguesia" -> p.getFreguesia().equalsIgnoreCase(valor);
                    case "municipio" -> p.getMunicipio().equalsIgnoreCase(valor);
                    case "ilha" -> p.getIlha().equalsIgnoreCase(valor);
                    default -> false;
                })
                .mapToDouble(Propriedade::getShapeArea)
                .average()
                .orElse(0.0);
    }

    /**
     * Calcula a área média considerando a união de propriedades adjacentes do mesmo proprietário.
     *
     * @param propriedades Lista de propriedades.
     * @param tipo Tipo de filtro geográfico.
     * @param valor Valor a ser filtrado.
     * @return Área média após unificação de propriedades adjacentes por proprietário.
     */
    public static double areaMediaUnificada(List<Propriedade> propriedades, String tipo, String valor) {
        List<Propriedade> filtradas = propriedades.stream()
                .filter(p -> switch (tipo.toLowerCase()) {
                    case "freguesia" -> p.getFreguesia().equalsIgnoreCase(valor);
                    case "municipio" -> p.getMunicipio().equalsIgnoreCase(valor);
                    case "ilha" -> p.getIlha().equalsIgnoreCase(valor);
                    default -> false;
                })
                .collect(Collectors.toList());

        return calcularAreaMediaUnificada(filtradas);
    }

    /**
     * Sugere trocas entre propriedades de diferentes proprietários, visando aumentar a área média
     * unificada e com base no potencial de troca.
     *
     * @param propriedades Lista de propriedades.
     * @param tipo Tipo de filtro ("freguesia", "municipio", "ilha").
     * @param valor Valor a ser usado no filtro.
     * @return Lista de sugestões de trocas com ganho e potencial calculado.
     */
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

                    List<Propriedade> grupoOriginal = filtradas.stream()
                            .filter(p -> p.getOwner().equals(donoA) || p.getOwner().equals(donoB))
                            .collect(Collectors.toList());

                    double antes = calcularAreaMediaUnificada(grupoOriginal);

                    a.setOwner(donoB);
                    b.setOwner(donoA);

                    double depois = calcularAreaMediaUnificada(grupoOriginal);

                    a.setOwner(donoA);
                    b.setOwner(donoB);

                    double ganho = depois - antes;

                    if (ganho > 0.01) {
                        double dif = Math.abs(a.getShapeArea() - b.getShapeArea());
                        double potencial = 1.0 / (1.0 + dif);
                        sugestoes.add(new SugestaoTroca(a, b, ganho, potencial));
                    }
                }
            }
        }

        sugestoes.sort(Comparator.comparingDouble(s -> -(s.getGanhoTotal() * s.getPotencial())));
        return sugestoes;
    }

    /**
     * Agrupa e une geometrias de propriedades por proprietário, tratando interseções e vizinhanças.
     *
     * @param propriedades Lista de propriedades.
     * @return Lista de geometrias unificadas por dono.
     */
    private static List<Geometry> unirGeometriasPorDono(Collection<Propriedade> propriedades) {
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

        return geometriasAgrupadas;
    }

    /**
     * Calcula a média da área total das geometrias agrupadas/unificadas por proprietário.
     *
     * @param propriedades Subconjunto de propriedades.
     * @return Área média resultante da união de parcelas adjacentes.
     */
    private static double calcularAreaMediaUnificada(Collection<Propriedade> propriedades) {
        List<Geometry> geometrias = unirGeometriasPorDono(propriedades);
        double soma = geometrias.stream().mapToDouble(Geometry::getArea).sum();
        return geometrias.isEmpty() ? 0.0 : soma / geometrias.size();
    }
}