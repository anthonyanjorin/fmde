package org.upb.fmde.de.categories.concrete.graphs

import org.upb.fmde.de.categories.diagrams.DotPrinter

class GraphPrinter implements DotPrinter {
	
	GraphDiagram d
	
	new (GraphDiagram d){
		this.d = d
	}
	
	override print() {
		return '''
			@startuml
			skinparam shadowing false
			hide members
			hide circle
			«FOR graph : d.objects»
				«FOR o : graph.vertices.elts»
					class "«graph.label».«o»"
				«ENDFOR»
				«FOR e : graph.edges.elts»
					"«graph.label».«graph.src.map(e)»"-->"«graph.label».«graph.trg.map(e)»" : "«e»"
				«ENDFOR»			
			«ENDFOR»
			
			«FOR f : d.arrows»
				«FOR v : f._V.src.elts»
					"«f.src.label».«v»" --> "«f.trg.label».«f._V.map(v)»" : "«f.label»"
				«ENDFOR»
			«ENDFOR»
			@enduml
		'''
	}
}
