package org.upb.fmde.de.categories.concrete.graphs;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.concrete.finsets.FinSetDiagram;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class GraphDiagram extends Diagram<Graph, GraphMorphism> {
	
	public GraphDiagram() {
		super(Graphs.Graphs);
	}

	public FinSetDiagram getSetDiagram() {
		FinSetDiagram d = new FinSetDiagram();
		getArrows().forEach(a -> d.arrows(a._E(), a._V()));
		getObjects().forEach(o -> d.objects(o.edges(), o.vertices()));
		getObjects().forEach(o -> d.arrows(o.src(), o.trg()));
		return d;
	}
	
	@Override
	public void prettyPrint(String dir, String fileName) throws IOException{
		saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new GraphPrinter(this));
	}
}
