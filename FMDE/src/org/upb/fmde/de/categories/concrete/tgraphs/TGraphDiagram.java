package org.upb.fmde.de.categories.concrete.tgraphs;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class TGraphDiagram extends Diagram<TGraph, TGraphMorphism> {

	public TGraphDiagram(Graph typeGraph) {
		super(TGraphs.TGraphsFor(typeGraph));
	}
	
	public GraphDiagram getGraphDiagram(){
		GraphDiagram d =  new GraphDiagram();
		getArrows().forEach(a -> d.arrows(a.untyped()));
		getObjects().forEach(o -> d.objects(o.type().src(), o.type().trg()));
		getObjects().forEach(o -> d.arrows(o.type()));
		return d;
	}
		
	@Override
	public void prettyPrint(String dir, String fileName) throws IOException{
		saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new TGraphPrinter(this));
	}
}
