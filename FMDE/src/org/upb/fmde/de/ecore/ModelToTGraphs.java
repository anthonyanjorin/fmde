package org.upb.fmde.de.ecore;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.ecore.ModelToGraphs.EcoreEdge;

public class ModelToTGraphs {
	private TGraph model;
	private TGraph metamodel;
	private TGraph metametamodel;
	
	public ModelToTGraphs(EObject root, String label) {
		Graph m = new ModelToGraphs(root, label).getResult();
		Graph mm = new MetaModelToGraphs(root.eClass().getEPackage(), "TG_" + label).getResult();
		Graph mmm = new MetaMetaModelToGraphs(root.eClass().eClass().getEPackage(), "Ecore").getResult();
		
		model = determineTypeForModel(m, mm);
		metamodel = determineTypeForMetamodel(mm, mmm);
		metametamodel = determineTypeForMetamodel(mmm, mmm);
	}
	
	public ModelToTGraphs(EObject root, String label, TGraph metamodel, TGraph metametamodel){
		this.metamodel = metamodel;
		this.metametamodel = metametamodel;
		
		Graph m = new ModelToGraphs(root, label).getResult();
		model = determineTypeForModel(m, metamodel.type().src());
	}

	private TGraph determineTypeForMetamodel(Graph mm, Graph mmm) {
		TotalFunction f_E = new TotalFunction(mm.edges(), "type_E_" + mm.label(), mmm.edges());
		TotalFunction f_V = new TotalFunction(mm.vertices(), "type_V_" + mm.label(), mmm.vertices());
		
		for (Object node : mm.vertices().elts()) {
			EClass tv = ((EObject) node).eClass();
			 
			mmm.vertices().elts()
				.stream()
				.filter(mv -> mv.equals(tv))
				.forEach(o -> f_V.addMapping(node, o));				
		}
		
		for (Object edge : mm.edges().elts()) {			
			mmm.edges().elts()
				.stream()
				.filter(me -> me.equals(EcorePackage.eINSTANCE.getEReference()))
				.forEach(e -> f_E.addMapping(edge, e));
		}
		
		GraphMorphism type = new GraphMorphism("type_" + mm.label(), mm, mmm, f_E, f_V);
		return new TGraph(mm.label(), type);
	}

	private TGraph determineTypeForModel(Graph m, Graph mm) {
		// TODO (08) Create and return m -type-> mm
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public TGraph[] getResult() {
		return new TGraph[] {model, metamodel, metametamodel};
	}

	
}
