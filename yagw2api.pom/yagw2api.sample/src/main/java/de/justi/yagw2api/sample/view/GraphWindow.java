package de.justi.yagw2api.sample.view;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.tree.JGraphRadialTreeLayout;

public class GraphWindow extends AbstractWindow {
	private static final long serialVersionUID = -8583440059587749181L;


	public GraphWindow() {
		super();
		final ListenableUndirectedWeightedGraph<String, DefaultEdge> graph = new ListenableUndirectedWeightedGraph<String, DefaultEdge>(DefaultEdge.class);
		graph.addVertex("TEST");
		graph.addVertex("TEST1");
		graph.addVertex("TEST2");
		graph.addVertex("TEST3");
		graph.addEdge("TEST", "TEST1");
		graph.addEdge("TEST", "TEST2");
		graph.addEdge("TEST", "TEST3");
		graph.addEdge("TEST3", "TEST2");
		final JGraphModelAdapter<String, DefaultEdge> graphAdapter = new JGraphModelAdapter<String, DefaultEdge>(graph);
		final JGraph graphComponent = new JGraph(graphAdapter);

		final JScrollPane graphScrollPane = new JScrollPane(graphComponent);

		this.getContentPanel().add(graphScrollPane, BorderLayout.CENTER);

		JGraphFacade jgf = new JGraphFacade(graphComponent);
		JGraphLayout layout = new JGraphRadialTreeLayout();
		layout.run(jgf);

	}
}
