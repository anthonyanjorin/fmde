package org.upb.fmde.de.categories.concrete.pgraphs

import org.upb.fmde.de.categories.diagrams.DotPrinter

class PGraphPrinter implements DotPrinter {
	
	PGraphDiagram d
	
	new (PGraphDiagram d){
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
				«IF (f._V.map(v) != null)»
					"«f.src.label».«v»" --> "«f.trg.label».«f._V.map(v)»" : "«f.label»"
				«ENDIF»
				«ENDFOR»
			«ENDFOR»
			@enduml
		'''
	}
}
