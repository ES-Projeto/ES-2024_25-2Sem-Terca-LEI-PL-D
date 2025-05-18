import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitária responsável pela visualização gráfica de um grafo genérico usando a biblioteca JGraphX.
 * Os vértices e arestas do grafo são convertidos em elementos visuais, exibidos numa interface Swing.
 */
public class GrafoVisual {

    /**
     * Visualiza um grafo passado como argumento numa janela gráfica.
     * Esta função utiliza {@code mxGraph} para desenhar os vértices e as conexões (arestas) com um layout orgânico.
     *
     * @param jGraphTGraph Grafo do JGraphT a ser visualizado. Os nós e arestas são desenhados automaticamente.
     * @param <V> Tipo dos vértices.
     * @param <E> Tipo das arestas.
     */
    public static <V, E> void visualize(Graph<V, E> jGraphTGraph) {
        if (jGraphTGraph == null || jGraphTGraph.vertexSet().isEmpty()) {
            System.out.println(" Grafo vazio. Nenhum nó para visualizar.");
            JOptionPane.showMessageDialog(null, "O grafo está vazio e não pode ser visualizado.", "Grafo vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println(" Visualizando grafo:");
        System.out.println("   Nós: " + jGraphTGraph.vertexSet().size());
        System.out.println("   Arestas: " + jGraphTGraph.edgeSet().size());

        mxGraph mxGraph = new mxGraph();
        Object parent = mxGraph.getDefaultParent();
        Map<V, Object> vertexMap = new HashMap<>();

        mxGraph.getModel().beginUpdate();
        try {
            // Add vertices
            for (V vertex : jGraphTGraph.vertexSet()) {
                if (vertex == null || vertex.toString().trim().isEmpty()) continue;
                Object cell = mxGraph.insertVertex(parent, null, vertex.toString(), 0, 0, 120, 50);
                vertexMap.put(vertex, cell);
            }

            // Add edges
            for (E edge : jGraphTGraph.edgeSet()) {
                V source = jGraphTGraph.getEdgeSource(edge);
                V target = jGraphTGraph.getEdgeTarget(edge);
                if (vertexMap.containsKey(source) && vertexMap.containsKey(target)) {
                    mxGraph.insertEdge(parent, null, "", vertexMap.get(source), vertexMap.get(target));
                }
            }

            // Apply layout inside update block
            mxOrganicLayout layout = new mxOrganicLayout(mxGraph);
            layout.execute(parent);
        } finally {
            mxGraph.getModel().endUpdate();
        }

        // Create Swing component
        mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);
        graphComponent.setConnectable(false);
        graphComponent.setPanning(true);
        graphComponent.setToolTips(true);
        graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
        graphComponent.setCenterZoom(true);

        // Setup the frame
        JFrame frame = new JFrame("Grafo de Propriedades / Proprietários");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(graphComponent);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println(" Janela de grafo aberta com sucesso.");
    }
}
