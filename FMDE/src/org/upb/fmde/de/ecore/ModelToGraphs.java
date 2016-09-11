package org.upb.fmde.de.ecore;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.upb.fmde.de.categories.finsets.FinSet;
import org.upb.fmde.de.categories.finsets.TotalFunction;
import org.upb.fmde.de.categories.graphs.Graph;

public class ModelToGraphs {
	private Graph result;
	private FinSet vertices;
	private FinSet edges;
	private TotalFunction source;
	private TotalFunction target;

	public ModelToGraphs(EObject root, String label) {
		vertices = new FinSet("V_" + label);
		edges = new FinSet("E_" + label);
		source = new TotalFunction(edges, "s_" + label, vertices);
		target = new TotalFunction(edges, "t_" + label, vertices);

		traverseModel(root);
		result = new Graph(label, edges, vertices, source, target);
	}

	private void traverseModel(EObject root) {
		visitNodeAndEdges(root);
		root.eAllContents().forEachRemaining(eob -> visitNodeAndEdges(eob));
	}

	private void visitNodeAndEdges(EObject eob) {
		vertices.getElements().add(eob);
		for (EStructuralFeature sf : eMoflonEMFUtil.getAllReferences(eob)) {
			if (sf.isMany()) {
				((Collection<?>) eob.eGet(sf)).forEach(o -> addEdge(sf, eob, o));
			} else {
				Object o = eob.eGet(sf);
				addEdge(sf, eob, o);
			}
		}
	}

	private void addEdge(EStructuralFeature sf, EObject from, Object to) {
		EcoreEdge edge = new EcoreEdge(sf, from, to);
		edges.getElements().add(edge);
		source.addMapping(edge, from);
		target.addMapping(edge, to);
	}

	public Graph getResult() {
		return result;
	}
	
	public class EcoreEdge {  	
		public EStructuralFeature sf;
		public Object source;
		public Object target;
		
		EcoreEdge(EStructuralFeature sf, Object eob, Object o) {
			this.sf = sf;
			this.source = eob;
			this.target = o;
		}
		
		@Override
		public String toString() {
			return sf.getName();
		}		
	}
}
