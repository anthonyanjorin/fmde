package org.upb.fmde.de.categories.concrete.pfinsets

import org.upb.fmde.de.categories.diagrams.DotPrinter

class PFinSetPrinter implements DotPrinter {

	PFinSetDiagram d

	new(PFinSetDiagram d) {
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
				«IF (f.map(v) != null)»
					"«f.src.label».«v»" --> "«f.trg.label».«f.map(v)»" : "«f.label»"
				«ENDIF»
				«ENDFOR»
			«ENDFOR»
			@enduml
		'''
	}
}
