package org.upb.fmde.de.categories.concrete.tgraphs

import org.upb.fmde.de.categories.diagrams.DotPrinter

class TGraphPrinter implements DotPrinter {
	
	TGraphDiagram d
	
	new (TGraphDiagram d){
		this.d = d
	}
	
	override print() {
		return '''
			@startuml
			skinparam shadowing false
			hide members
			hide circle
			«FOR graph : d.objects»
				«FOR o : graph.type.src.vertices.elts»
					class "«d.cat.showOb(graph)».«o» : «graph.type()._V().map(o)»"
				«ENDFOR»
				«FOR e : graph.type.src.edges.elts»
					"«d.cat.showOb(graph)».«graph.type.src.src.map(e)» : «graph.type()._V().map(graph.type.src.src.map(e))»"-->"«d.cat.showOb(graph)».«graph.type.src.trg.map(e)» : «graph.type()._V().map(graph.type.src.trg.map(e))»" : "«e»"
				«ENDFOR»			
			«ENDFOR»
			
			«FOR f : d.arrows»
				«FOR v : f.untyped._V.src.elts»
					"«d.cat.showOb(f.src)».«v» : «f.src().type()._V().map(v)»" --> "«d.cat.showOb(f.trg)».«f.untyped._V.map(v)» : «f.trg().type()._V().map(f.untyped._V.map(v))»" : "«f.label»"
				«ENDFOR»
			«ENDFOR»
			@enduml
		'''
	}
}