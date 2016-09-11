package org.upb.fmde.de.categories.tgraphs

import org.upb.fmde.de.categories.DotPrinter

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
				«FOR o : graph.typeMorphism.source.vertices.elements»
					class "«d.cat.showOb(graph)».«o»"
				«ENDFOR»
				«FOR e : graph.typeMorphism.source.edges.elements»
					"«d.cat.showOb(graph)».«graph.typeMorphism.source.src.map(e)»"-->"«d.cat.showOb(graph)».«graph.typeMorphism.source.trg.map(e)»" : "«e»"
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
