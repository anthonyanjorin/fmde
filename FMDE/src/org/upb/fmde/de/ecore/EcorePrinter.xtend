package org.upb.fmde.de.ecore

import org.eclipse.emf.ecore.ENamedElement
import org.eclipse.emf.ecore.EObject
import org.moflon.core.utilities.eMoflonEMFUtil
import org.upb.fmde.de.categories.diagrams.DotPrinter
import org.upb.fmde.de.categories.graphs.GraphDiagram

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
				«FOR o : graph.vertices.elements»
					class "«graph.label».«o.show»"
				«ENDFOR»
				«FOR e : graph.edges.elements»
					"«graph.label».«graph.src.map(e).show»"-->"«graph.label».«graph.trg.map(e).show»" : "«e.show»"
				«ENDFOR»			
			«ENDFOR»
			«FOR f : d.arrows»
				«FOR v : f.get_f_V.source.elements»
					"«f.source.label».«v.show»" --> "«f.target.label».«f.get_f_V.map(v).show»" : "«f.label»"
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
