package org.upb.fmde.de.categories.concrete.tgraphs;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.diagrams.Diagram;
import org.upb.fmde.de.categories.slice.Triangle;

public class TGraphDiagram extends Diagram<GraphMorphism, Triangle<Graph, GraphMorphism>> {

	public TGraphDiagram(Graph typeGraph) {
		super(TGraphs.TGraphsFor(typeGraph));
	}
	
	public GraphDiagram getGraphDiagram(){
		GraphDiagram d =  new GraphDiagram();
		getArrows().forEach(a -> d.arrows(a.getF()));
		getObjects().forEach(o -> d.objects(o.src(), o.trg()));
		getObjects().forEach(o -> d.arrows(o));
		return d;
	}
		
	@Override
	public void prettyPrint(String dir, String fileName) throws IOException{
		saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new TGraphPrinter(this));
	}
}
