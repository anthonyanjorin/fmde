package org.upb.fmde.de.ecore

import org.upb.fmde.de.categories.tgraphs.TGraphDiagram
import org.upb.fmde.de.categories.graphs.GraphDiagram

class TEcorePrinter extends EcorePrinter {
	
	new (TGraphDiagram td){
		super(new GraphDiagram)
		fillGraphDiagram(td)
	}
	
	def fillGraphDiagram(TGraphDiagram td){
		td.objects.forEach[o | d.objects(o.typeMorphism.source)]
		td.arrows.forEach[a | d.arrows(a.untypedMorphism)]
	}
}
