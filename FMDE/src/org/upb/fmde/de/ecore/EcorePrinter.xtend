package org.upb.fmde.de.ecore

import org.eclipse.emf.ecore.ENamedElement
import org.eclipse.emf.ecore.EObject
import org.moflon.core.utilities.eMoflonEMFUtil
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram
import org.upb.fmde.de.categories.diagrams.DotPrinter

class EcorePrinter implements DotPrinter {
	
	protected GraphDiagram d
	
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
					class "«graph.label».«o.show»"
				«ENDFOR»
				«FOR e : graph.edges.elts»
					"«graph.label».«graph.src.map(e).show»"-->"«graph.label».«graph.trg.map(e).show»" : "«e.show»"
				«ENDFOR»			
			«ENDFOR»
			«FOR f : d.arrows»
				«FOR v : f._V.src.elts»
					"«f.src.label».«v.show»" --> "«f.trg.label».«f._V.map(v).show»" : "«f.label»"
				«ENDFOR»
			«ENDFOR»
			@enduml
		'''
	}
	
	def show(Object o) {
		if(o instanceof ENamedElement)
			return (o as ENamedElement).name
		else if(o instanceof EObject)
			return eMoflonEMFUtil.getIdentifier(o as EObject)
		else
			return o.toString();
	}
}
