package org.upb.fmde.de.categories.graphs

import org.upb.fmde.de.categories.DotPrinter

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
				«FOR o : graph.vertices.elements»
					class "«graph.label».«o»"
				«ENDFOR»
				«FOR e : graph.edges.elements»
					"«graph.label».«graph.src.map(e)»"-->"«graph.label».«graph.trg.map(e)»" : "«e»"
				«ENDFOR»			
			«ENDFOR»
			
			«FOR f : d.arrows»
				«FOR v : f.get_f_V.source.elements»
					"«f.source.label».«v»" --> "«f.target.label».«f.get_f_V.map(v)»" : "«f.label»"
				«ENDFOR»
			«ENDFOR»
			@enduml
		'''
	}
}
