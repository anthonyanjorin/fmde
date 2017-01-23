package org.upb.fmde.de.categories.concrete.egraphs

import org.upb.fmde.de.categories.diagrams.DotPrinter

class EGraphPrinter implements DotPrinter {

	EGraphDiagram d

	new (EGraphDiagram d){
		this.d = d
	}

	override print() { // TODO: EGraphMorphisms; separaten Sub-Graph für Attribute, da wir diese ja nur einmal haben wollen
		return '''
			digraph G
			{
				«FOR graph : d.objects»
					subgraph cluster_«graph.label»
					{
						«FOR e : graph.Eg.elts»
							«graph.sourceG.map(e)» -> «e» -> «graph.targetG.map(e)»
						«ENDFOR»
						«FOR e : graph.Ena.elts»
		                    «graph.sourceNA.map(e)» -> «graph.targetNA.map(e)» [label="«e»", color=lightgray]
		                «ENDFOR»
		                «FOR e : graph.Eea.elts»
		                    «graph.sourceEA.map(e)» -> «graph.targetEA.map(e)» [label="«e»", color=lightgray]
		                «ENDFOR»
		                «FOR e : graph.Eg.elts»
							«e» [shape=none]
						«ENDFOR»
		                «FOR o : graph.Vd.elts»
		                     «o» [shape=polygon,sides=4,color=lightgray,style=filled]
		                «ENDFOR»
		                label = "«graph.label»";
	                }
				«ENDFOR»
	
				«FOR f : d.arrows»
					«FOR v : f.fVg.src.elts»
						"«f.src.label».«v»" -> "«f.trg.label».«f.fVg.map(v)»" [label = "«f.label»"]
					«ENDFOR»
				«ENDFOR»
			}
		'''
	}
}
