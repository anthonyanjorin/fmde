package org.upb.fmde.de.categories.graphs;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.diagrams.Diagram;
import org.upb.fmde.de.categories.finsets.FinSetDiagram;

public class GraphDiagram extends Diagram<Graph, GraphMorphism> {
	
	public GraphDiagram() {
		super(Graphs.Graphs);
	}

	public FinSetDiagram getSetDiagram() {
		FinSetDiagram d = new FinSetDiagram();
		getArrows().forEach(a -> d.arrows(a.get_f_E(), a.get_f_V()));
		getObjects().forEach(o -> d.objects(o.getEdges(), o.getVertices()));
		getObjects().forEach(o -> d.arrows(o.src(), o.trg()));
		return d;
	}
	
	@Override
	public void prettyPrint(String dir, String fileName) throws IOException{
		saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new GraphPrinter(this));
	}
}
