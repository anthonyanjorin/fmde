package org.upb.fmde.de.categories.concrete.finsets

import org.upb.fmde.de.categories.diagrams.DotPrinter

class FinSetPrinter implements DotPrinter {
	
	FinSetDiagram d
	
	new (FinSetDiagram d){
		this.d = d
	}
	
	override print() {
		return '''
			@startuml
			skinparam shadowing false
			hide members
			hide circle
			«FOR set : d.objects»
				«FOR o : set.elts»
					class "«set.label».«o»"
				«ENDFOR»
			«ENDFOR»
			
			«FOR f : d.arrows»
				«FOR v : f.src.elts»
					"«f.src.label».«v»" --> "«f.trg.label».«f.map(v)»" : "«f.label»"
				«ENDFOR»
			«ENDFOR»
			@enduml
		'''
	}
}
