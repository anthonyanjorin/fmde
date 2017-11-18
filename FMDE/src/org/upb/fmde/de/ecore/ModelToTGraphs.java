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
	private GraphMorphism model;
	private GraphMorphism metamodel;
	private GraphMorphism metametamodel;
	
	public ModelToTGraphs(EObject root, String label) {
		Graph m = new ModelToGraphs(root, label).getResult();
		Graph mm = new MetaModelToGraphs(root.eClass().getEPackage(), "TG_" + label).getResult();
		Graph mmm = new MetaMetaModelToGraphs(root.eClass().eClass().getEPackage(), "Ecore").getResult();
		
		model = determineTypeForModel(m, mm);
		metamodel = determineTypeForMetamodel(mm, mmm);
		metametamodel = determineTypeForMetamodel(mmm, mmm);
	}
	
	public ModelToTGraphs(EObject root, String label, GraphMorphism metamodel, GraphMorphism metametamodel){
		this.metamodel = metamodel;
		this.metametamodel = metametamodel;
		
		Graph m = new ModelToGraphs(root, label).getResult();
		model = determineTypeForModel(m, metamodel.src());
	}

	private GraphMorphism determineTypeForMetamodel(Graph mm, Graph mmm) {
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

	private GraphMorphism determineTypeForModel(Graph m, Graph mm) {
		TotalFunction f_E = new TotalFunction(m.edges(), "type_E_" + m.label(), mm.edges());
		TotalFunction f_V = new TotalFunction(m.vertices(), "type_V_" + m.label(), mm.vertices());

		for (Object node : m.vertices().elts()) {
			EClass tv = ((EObject) node).eClass();

			mm.vertices().elts().stream()
					.filter(mv -> mv.equals(tv))
					.forEach(o -> f_V.addMapping(node, o));
		}

		for (Object edge : m.edges().elts()) {
			EReference te = (EReference) ((EcoreEdge) edge).sf;

			mm.edges().elts().stream()
					.filter(me -> me.equals(te))
					.forEach(e -> f_E.addMapping(edge, e));
		}

		GraphMorphism type = new GraphMorphism("type_" + m.label(), m, mm, f_E, f_V);
		return new TGraph(m.label(), type);
	}

	public GraphMorphism[] getResult() {
		return new GraphMorphism[] {model, metamodel, metametamodel};
	}

	
}
