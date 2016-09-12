package org.upb.fmde.de.categories.tgraphs;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.Diagram;
import org.upb.fmde.de.categories.graphs.Graph;
import org.upb.fmde.de.categories.graphs.GraphDiagram;

public class TGraphDiagram extends Diagram<TGraph, TGraphMorphism> {

	public TGraphDiagram(Graph typeGraph) {
		super(TGraphs.TGraphsFor(typeGraph));
	}
	
	public GraphDiagram getGraphDiagram(){
		GraphDiagram d =  new GraphDiagram();
		getArrows().forEach(a -> d.arrows(a.getUntypedMorphism()));
		getObjects().forEach(o -> d.objects(o.type().getSource(), o.type().getTarget()));
		getObjects().forEach(o -> d.arrows(o.type()));
		return d;
	}
		
	@Override
	public void prettyPrint(String dir, String fileName) throws IOException{
		saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new TGraphPrinter(this));
	}
}
