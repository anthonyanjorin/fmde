package org.upb.fmde.de.categories.tgraphs

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
				«FOR o : graph.type.source.vertices.elements»
					class "«d.cat.showOb(graph)».«o»"
				«ENDFOR»
				«FOR e : graph.type.source.edges.elements»
					"«d.cat.showOb(graph)».«graph.type.source.src.map(e)»"-->"«d.cat.showOb(graph)».«graph.type.source.trg.map(e)»" : "«e»"
				«ENDFOR»			
			«ENDFOR»
			
			«FOR f : d.arrows»
				«FOR v : f.untypedMorphism.get_f_V.source.elements»
					"«d.cat.showOb(f.source)».«v»" --> "«d.cat.showOb(f.target)».«f.untypedMorphism.get_f_V.map(v)»" : "«f.label»"
				«ENDFOR»
			«ENDFOR»
			@enduml
		'''
	}
}
