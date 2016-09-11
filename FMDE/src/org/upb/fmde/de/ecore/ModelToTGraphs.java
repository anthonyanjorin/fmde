package org.upb.fmde.de.ecore;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.upb.fmde.de.categories.finsets.TotalFunction;
import org.upb.fmde.de.categories.graphs.Graph;
import org.upb.fmde.de.categories.graphs.GraphMorphism;
import org.upb.fmde.de.categories.tgraphs.TGraph;
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

	private TGraph determineTypeForMetamodel(Graph mm, Graph mmm) {
		TotalFunction f_E = new TotalFunction(mm.getEdges(), "type_E_" + mm.label(), mmm.getEdges());
		TotalFunction f_V = new TotalFunction(mm.getVertices(), "type_V_" + mm.label(), mmm.getVertices());
		
		for (Object node : mm.getVertices().getElements()) {
			EClass tv = ((EObject) node).eClass();
			 
			mmm.getVertices().getElements()
				.stream()
				.filter(mv -> mv.equals(tv))
				.forEach(o -> f_V.addMapping(node, o));				
		}
		
		for (Object edge : mm.getEdges().getElements()) {			
			mmm.getEdges().getElements()
				.stream()
				.filter(me -> me.equals(EcorePackage.eINSTANCE.getEReference()))
				.forEach(e -> f_E.addMapping(edge, e));
		}
		
		GraphMorphism type = new GraphMorphism("type_" + mm.label(), mm, mmm, f_E, f_V);
		return new TGraph(mm.label(), type);
	}

	private TGraph determineTypeForModel(Graph m, Graph mm) {
		TotalFunction f_E = new TotalFunction(m.getEdges(), "type_E_" + m.label(), mm.getEdges());
		TotalFunction f_V = new TotalFunction(m.getVertices(), "type_V_" + m.label(), mm.getVertices());

		for (Object node : m.getVertices().getElements()) {
			EClass tv = ((EObject) node).eClass();

			mm.getVertices().getElements().stream()
					.filter(mv -> mv.equals(tv))
					.forEach(o -> f_V.addMapping(node, o));
		}

		for (Object edge : m.getEdges().getElements()) {
			EReference te = (EReference) ((EcoreEdge) edge).sf;

			mm.getEdges().getElements().stream()
					.filter(me -> me.equals(te))
					.forEach(e -> f_E.addMapping(edge, e));
		}

		GraphMorphism type = new GraphMorphism("type_" + m.label(), m, mm, f_E, f_V);
		return new TGraph(m.label(), type);
	}

	public TGraph[] getResult() {
		return new TGraph[] {model, metamodel, metametamodel};
	}

	
}
