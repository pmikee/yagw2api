package de.justi.yagw2api.sample.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.tree.JGraphRadialTreeLayout;

public class GraphWindow extends JFrame {
	private static final long serialVersionUID = -8583440059587749181L;

	private final JPanel contentPanel;

	public GraphWindow() {
		this.contentPanel = new JPanel(true);
		this.contentPanel.setLayout(new BorderLayout());
		this.setContentPane(this.contentPanel);

		final ListenableUndirectedWeightedGraph<String, DefaultEdge> graph = new ListenableUndirectedWeightedGraph<String, DefaultEdge>(DefaultEdge.class);
		graph.addVertex("TEST");
		final JGraphModelAdapter<String, DefaultEdge> graphAdapter = new JGraphModelAdapter<String, DefaultEdge>(graph);
		final JGraph graphComponent = new JGraph(graphAdapter);

		final JScrollPane graphScrollPane = new JScrollPane(graphComponent);

		this.contentPanel.add(graphScrollPane, BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(640, 480));
		this.setPreferredSize(new Dimension(1024, 768));
		this.pack();

		JGraphFacade jgf = new JGraphFacade(graphComponent);
		JGraphLayout layout = new JGraphRadialTreeLayout();
		layout.run(jgf);

	}
}
