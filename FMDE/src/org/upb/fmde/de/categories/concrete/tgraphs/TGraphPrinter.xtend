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
				«FOR o : graph.src.vertices.elts»
					class "«d.cat.showOb(graph)».«o»"
				«ENDFOR»
				«FOR e : graph.src.edges.elts»
					"«d.cat.showOb(graph)».«graph.src.src.map(e)»"-->"«d.cat.showOb(graph)».«graph.src.trg.map(e)»" : "«e»"
				«ENDFOR»			
			«ENDFOR»
			
			«FOR f : d.arrows»
				«FOR v : f.getM()._V.src.elts»
					"«d.cat.showOb(f.src)».«v»" --> "«d.cat.showOb(f.trg)».«f.getM()._V.map(v)»" : "«f.label»"
				«ENDFOR»
			«ENDFOR»
			@enduml
		'''
	}
}
