package org.upb.fmde.de.categories.concrete.pgraphs;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.pfinsets.PFinSetDiagram;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class PGraphDiagram extends Diagram<Graph, PGraphMorphism> {
	
	public PGraphDiagram() {
		super(PGraphs.PGraphs);
	}

	public PFinSetDiagram getSetDiagram() {
		PFinSetDiagram d = new PFinSetDiagram();
		getArrows().forEach(a -> d.arrows(a._E(), a._V()));
		getObjects().forEach(o -> d.objects(o.edges(), o.vertices()));
		getObjects().forEach(o -> d.arrows(o.src(), o.trg()));
		return d;
	}
	
	@Override
	public void prettyPrint(String dir, String fileName) throws IOException{
		saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new PGraphPrinter(this));
	}
}
