package org.upb.fmde.de.categories.diagrams

class CatPrinter<Ob, Arr> implements DotPrinter {
	
	private Diagram<Ob, Arr> d;
	
	new(Diagram<Ob, Arr> d){
		this.d = d
	}
	
	
	override print() {
		return '''
		@startuml
		digraph Diagram {
		  «FOR o : d.objects»
		  "«d.cat.showOb(o)»";
		  «ENDFOR»
		  «FOR a : d.arrows»
		  "«d.cat.showOb(d.cat.source(a))»"->"«d.cat.showOb(d.cat.target(a))»" [label=" «d.cat.showArr(a)»"];
		  «ENDFOR»
		}
		@enduml
		'''
	}
}
