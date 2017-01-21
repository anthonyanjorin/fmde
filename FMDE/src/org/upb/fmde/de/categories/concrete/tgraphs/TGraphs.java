package org.upb.fmde.de.categories.concrete.tgraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.upb.fmde.de.categories.LabelledCategory;
import org.upb.fmde.de.categories.colimits.CategoryWithInitOb;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushoutComplements;
import org.upb.fmde.de.categories.colimits.pushouts.CategoryWithPushouts;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.colimits.pushouts.Corner;
import org.upb.fmde.de.categories.colimits.pushouts.Span;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.graphconditions.GraphCondition;
import org.upb.fmde.de.graphconditions.SatisfiableGraphCondition;

public class TGraphs implements LabelledCategory<TGraph, TGraphMorphism>, 
								CategoryWithInitOb<TGraph, TGraphMorphism>, 
								CategoryWithPushouts<TGraph, TGraphMorphism>,
								CategoryWithPushoutComplements<TGraph, TGraphMorphism> {
	private final Graph typeGraph;
	private final TGraph EMPTY_TYPED_GRAPH;
	private final CoLimit<TGraph, TGraphMorphism> INITIAL_OBJECT;
	
	public static TGraphs TGraphsFor(Graph typeGraph) {
		return new TGraphs(typeGraph);
	}
	
	public TGraphs(Graph typeGraph) {
		this.typeGraph = typeGraph;
		EMPTY_TYPED_GRAPH = new TGraph("EMPTY_TYPED_GRAPH", Graphs.initialObject().up.apply(typeGraph));
		INITIAL_OBJECT = new CoLimit<>(
				EMPTY_TYPED_GRAPH, 
				G -> new TGraphMorphism("initial_" + G.label(), Graphs.initialObject().up.apply(G.type().src()), EMPTY_TYPED_GRAPH, G)
			);
	}
	
	@Override
	public TGraphMorphism compose(TGraphMorphism f, TGraphMorphism g) {
		GraphMorphism f_g = Graphs.compose(f.untyped(), g.untyped());
		return new TGraphMorphism(f_g.label(), f_g, f.src(), g.trg());
	}

	@Override
	public TGraphMorphism id(TGraph f) {
		GraphMorphism id = Graphs.id(f.type().src());
		return new TGraphMorphism(id.label(), id, f, f);
	}
	
	@Override
	public String showOb(TGraph o) {
		return LabelledCategory.super.showOb(o) + ":" + o.type().trg().label();
	}

	@Override
	public CoLimit<TGraph, TGraphMorphism> initialObject() {
		return INITIAL_OBJECT;
	}

	@Override
	public CoLimit<TGraphMorphism, TGraphMorphism> coequaliser(TGraphMorphism f, TGraphMorphism g) {
		CoLimit<GraphMorphism, GraphMorphism> coeq_graphs = Graphs.coequaliser(f.untyped(), g.untyped());
		GraphMorphism type = coeq_graphs.up.apply(f.trg().type());
		TGraph obj = new TGraph("==", type);
		TGraphMorphism coeq_obj = new TGraphMorphism("==", coeq_graphs.obj, f.trg(), obj);
		
		return new CoLimit<TGraphMorphism, TGraphMorphism>(
				coeq_obj, 
				tm -> new TGraphMorphism("u", coeq_graphs.up.apply(tm.untyped()), obj, tm.trg())
				);
	}

	@Override
	public CoLimit<CoSpan<TGraphMorphism>, TGraphMorphism> coproduct(TGraph a, TGraph b) {
		CoLimit<CoSpan<GraphMorphism>, GraphMorphism> coprod_graphs = Graphs.coproduct(a.type().src(), b.type().src());
		GraphMorphism type = coprod_graphs.up.apply(new CoSpan<>(Graphs, b.type(), a.type()));
		TGraph obj = new TGraph(a + "+" + b, type);
		
		return new CoLimit<>(
				new CoSpan<>(this, new TGraphMorphism("left", coprod_graphs.obj.left, b, obj), 
						     		new TGraphMorphism("right", coprod_graphs.obj.right, a, obj)), 
				cos -> new TGraphMorphism("u", 
						coprod_graphs.up.apply(new CoSpan<>(Graphs, cos.left.untyped(), cos.right.untyped())),
						obj,
						cos.left.trg())
				);
	}

	@Override
	public Optional<Corner<TGraphMorphism>> pushoutComplement(Corner<TGraphMorphism> upperLeft) {
		TGraphMorphism l = upperLeft.first;
		TGraphMorphism m = upperLeft.second;
		TGraph K = l.src();
		TGraph G = m.trg();
		
		return Graphs.pushoutComplement(new Corner<>(Graphs, l.untyped(), m.untyped()))
				.map(pc_G -> determinedTypedPushoutComplement(pc_G, K, G));
	}

	private Corner<TGraphMorphism> determinedTypedPushoutComplement(Corner<GraphMorphism> pc_G, TGraph K, TGraph G) {
		Graph D = pc_G.first.trg();
		
		TotalFunction type_E = new TotalFunction(D.edges(), "type_E", typeGraph.edges());
		D.edges().elts().forEach(e -> type_E.addMapping(e, G.type()._E().map(e)));
		
		TotalFunction type_V = new TotalFunction(D.vertices(), "type_V", typeGraph.vertices());
		D.vertices().elts().forEach(v -> type_V.addMapping(v, G.type()._V().map(v)));
		
		GraphMorphism type_D = new GraphMorphism("type_D", D, typeGraph, type_E, type_V);
		TGraph D_ = new TGraph("D", type_D);
		
		TGraphMorphism d = new TGraphMorphism("d", pc_G.first, K, D_);
		TGraphMorphism l_ = new TGraphMorphism("l'", pc_G.second, D_, G);
		
		return new Corner<>(this, d, l_);
	}
	
	private List<TGraphMorphism> constructConditions(Span<TGraphMorphism> upperLeftCorner) {
		//construct Pushout T
		
		CoLimit<CoSpan<TGraphMorphism>, TGraphMorphism> PO = pushout(upperLeftCorner);
		CoSpan<TGraphMorphism> q_t = PO.obj;
		
		List<TGraphMorphism> allT_TiArrows = new ArrayList<TGraphMorphism>();
		//find all Ti
		for(Glue glue: constructEpimorphicGlues(q_t)) {
			allT_TiArrows.add(glue.s);
		}
		//return t;ti
		return allT_TiArrows;
	}
	
	public List<GraphCondition<TGraph, TGraphMorphism>> calculateLeftSideApplicationConditions(Span<TGraphMorphism> L_K_R, 
			TGraphMorphism a) {
		List<GraphCondition<TGraph, TGraphMorphism>> allConditions = new ArrayList<GraphCondition<TGraph,TGraphMorphism>>();
		
		TGraph P = a.src();
		TGraph R = L_K_R.right.trg();
		
		//construct all possible S
		Set<Glue> all_p_s = constructEpimorphicGlues(P, R);
		
		for (Glue glue : all_p_s) {
			Span<TGraphMorphism> upperLeftCorner = new Span<TGraphMorphism>(this, a, glue.p);
			List<TGraphMorphism> all_t_ti = constructConditions(upperLeftCorner);
			
			//construct conditions
			Optional<GraphCondition<TGraph, TGraphMorphism>> applCond = constructLeftSideApplCond(L_K_R, glue.s, all_t_ti);
			if(applCond.isPresent()) {
				allConditions.add(applCond.get());
			}
		}
		
		return allConditions;
	}
	
	private Optional<GraphCondition<TGraph, TGraphMorphism>> constructLeftSideApplCond(
			Span<TGraphMorphism> L_K_R, 
			TGraphMorphism s, 
			List<TGraphMorphism> all_t_ti) {
		
		TGraphMorphism r = L_K_R.right;
		TGraphMorphism l = L_K_R.left;
		
		
		//Calculate Z
		Optional<Corner<TGraphMorphism>> Z = pushoutComplement(new Corner<TGraphMorphism>(this, r, s));
		if(!Z.isPresent()) {
			return Optional.empty();
		}
		TGraphMorphism z = Z.get().first; //TODO corner?
		TGraphMorphism r_star = Z.get().second;
		
		//Calculate Y
		CoLimit<CoSpan<TGraphMorphism>, TGraphMorphism> Y = pushout(new Span<TGraphMorphism>(this, l, z));
		TGraphMorphism y = Y.obj.right;
		TGraphMorphism l_star = Y.obj.left; //TODO check
		
		
		List<TGraphMorphism> allYis = new ArrayList<TGraphMorphism>();
		
		//Calculate all Zi
		for (TGraphMorphism xi : all_t_ti) {
			Optional<Corner<TGraphMorphism>> Zi = pushoutComplement(new Corner<TGraphMorphism>(this, r_star, xi));
			if(!Zi.isPresent()) {
				continue;
			}
			
			TGraphMorphism zi = Z.get().first; //TODO corner?
			//Calculate Di
			CoLimit<CoSpan<TGraphMorphism>, TGraphMorphism> Di = pushout(new Span<TGraphMorphism>(this, l_star, zi));
			TGraphMorphism yi = Di.obj.right; //TODO check
			
			allYis.add(yi);
		}
		
		if(allYis.isEmpty()) {
			return Optional.empty();
		}
		
		GraphCondition<TGraph, TGraphMorphism> gc = new SatisfiableGraphCondition<TGraph, TGraphMorphism>(this, y, allYis);
		
		return Optional.of(gc);
		
	}

	@Override
	public Corner<TGraphMorphism> restrict(Corner<TGraphMorphism> upperLeft) {
		Corner<GraphMorphism> untyped = Graphs.restrict(new Corner<>(Graphs.Graphs, upperLeft.first.untyped(), upperLeft.second.untyped()));
		
		TGraph G = upperLeft.second.trg();
		Graph G_ = untyped.first.trg();
		
		TotalFunction type_G_E = new TotalFunction(G_.edges(), "type_G_E", typeGraph.edges());
		G_.edges().elts().forEach(e -> type_G_E.addMapping(e, G.type()._E().map(e)));
		
		TotalFunction type_G_V = new TotalFunction(G_.vertices(), "type_G_V", typeGraph.vertices());
		G_.vertices().elts().forEach(v -> type_G_V.addMapping(v, G.type()._V().map(v)));
		
		GraphMorphism type_G = new GraphMorphism("type_G", G_, typeGraph, type_G_E, type_G_V);
		TGraph _G_ = new TGraph("G_", type_G);
		
		return new Corner<>(this, new TGraphMorphism("_m_", untyped.first, upperLeft.first.trg(), _G_),
								  new TGraphMorphism("_g_", untyped.second, _G_, G));
	}
	
	private Set<Glue> findGlues(Map<Object, List<Object>> typeMappingR, TGraph P, Glue g) {
		Set<Glue> foundGlues = new TreeSet<Glue>();
		
		for(Object vertice: P.type().src().vertices().elts() ) {
			if(g.gluedPVertices.contains(vertice)) {
				continue;
			}
			//get potential gluing candidates
			List<Object> candidates = typeMappingR.get(P.type()._V().map(vertice));
			
			for(Object candidate: candidates) {
				if(g.gluedRVertices.contains(candidate)) {
					continue;
				}
				
				Glue x = new Glue(g);
				x.addGlue(vertice, candidate);
				foundGlues.add(x);
				
				foundGlues.addAll(findGlues(typeMappingR, P, x));
			}
		}
		
		return foundGlues;
	}
	
	private Map<Object, List<Object>> constructTypeMapping(TGraph X){
		Map<Object,List<Object>> typeMapping = new HashMap<Object, List<Object>>();
		for(Object verticeType: X.type().trg().vertices().elts()) {
			List<Object> matches = new ArrayList<Object>();
			for(Object vertice: X.type().src().vertices().elts()) {
				if(X.type()._V().map(vertice).equals(verticeType)) {
					matches.add(vertice);
				}
			}
			typeMapping.put(verticeType, matches);
		}
		return typeMapping;
	}
	
	public Set<Glue> constructEpimorphicGlues(TGraph P, TGraph R) {
		Map<Object, List<Object>> typeMappingR = constructTypeMapping(R);
		
		Set<Glue> allGlues = new TreeSet<Glue>();
		Glue unglued = new Glue();
		allGlues.add(unglued);
		allGlues.addAll(findGlues(typeMappingR, P, unglued));
		
		for(Glue g: allGlues) {
			g.constructMorphismsFromMapping(P, R);
		}
		
		return allGlues;
	}

	public Set<Glue> constructEpimorphicGlues(CoSpan<TGraphMorphism> q_t) {
		TGraphMorphism q = q_t.left;
		TGraphMorphism t = q_t.right;
		
		TGraph C = q.src();
		TGraph S = t.src();
		
		Map<Object, List<Object>> typeMappingS = constructTypeMapping(S);
		
		Set<Glue> allGlues = new TreeSet<Glue>();
		Glue pushoutGlue = new Glue(q_t);
		allGlues.add(pushoutGlue);
		allGlues.addAll(findGlues(typeMappingS, C, pushoutGlue));
		
		for(Glue g: allGlues) {
			g.constructMorphismsFromMapping(C, S);;
		}
		
		return allGlues;
		
		//p => q;ti
		//s => t;ti
	}
	
	private static class Glue{
		
		TGraphMorphism p;
		
		TGraphMorphism s;
		
		List<Object> gluedPVertices = new ArrayList<Object>();
		List<Object> gluedRVertices = new ArrayList<Object>();
		
		Map<Object,Object> gluedObjects = new HashMap<Object, Object>();
		
		private void constructMorphismsFromMapping(TGraph P, TGraph R) {
			//construct p and s from gluedObjects
			
			//map vertice
			
			//map edge
			//TODO
		}
		
		public Glue(CoSpan<TGraphMorphism> q_t) {
			TGraphMorphism q = q_t.left;
			TGraphMorphism t = q_t.right;
			
			for(Object verticeInC: q.untyped()._V().src().elts()) {
				for(Object verticeInS: t.untyped()._V().src().elts()) {
					if(q.untyped()._V().map(verticeInC).equals(t.untyped()._V().map(verticeInS))) {
						addGlue(verticeInC, verticeInS);
						break;
					}
				}
			}
		}
		
		public Glue(){}
		
		public Glue(Glue old) {
			this.gluedPVertices = new ArrayList<Object>();
			this.gluedPVertices.addAll(old.gluedPVertices);
			
			this.gluedRVertices = new ArrayList<Object>();
			this.gluedRVertices.addAll(old.gluedRVertices);
			
			for(Object pVertice: old.gluedObjects.keySet()) {
				this.gluedObjects.put(pVertice, old.gluedObjects.get(pVertice));
			}
		}
		
		public void addGlue(Object pVertice, Object rVertice) {
			this.gluedPVertices.add(pVertice);
			this.gluedRVertices.add(rVertice);
			this.gluedObjects.put(pVertice, rVertice);
		}
		
		public boolean equals(Object o) {
			if (!(o instanceof Glue)) {
				return false;
			}
			
			Glue g = (Glue) o;
			
			if (gluedObjects.entrySet().size() != g.gluedObjects.entrySet().size()) {
				return false;
			}
			
			for (Object pVertice: gluedObjects.entrySet()) {
				if (!gluedObjects.get(pVertice).equals(g.gluedObjects.get(pVertice))) {
					return false;
				}
			}
			return true;
		}
		
	}
}
