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
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
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
		
		//find all Ti TODO find all tis
		allT_TiArrows.add(q_t.right);
		
		
//		for(Glue glue: constructEpimorphicGlues(q_t)) {
//			allT_TiArrows.add(glue.s);
//		}
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
		Corner<GraphMorphism> untyped = Graphs.restrict(new Corner<>(Graphs, upperLeft.first.untyped(), upperLeft.second.untyped()));
		
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
		Glue unglued = new Glue("S");
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
	
	private class Glue implements Comparable<Glue> {
		
		private TGraph S;
		
		GraphMorphism type;
		
		TGraphMorphism p;
		
		TGraphMorphism s;
		
		List<Object> gluedPVertices = new ArrayList<Object>();
		List<Object> gluedRVertices = new ArrayList<Object>();
		
		Map<Object,Object> gluedObjects = new HashMap<Object, Object>();
		
		private void constructMorphismsFromMapping(TGraph P, TGraph R) {
			//construct p from gluedObjects
			
			TotalFunction f_E = new TotalFunction(P.type().src().edges(), "f_E", S.type().src().edges());
			TotalFunction f_V = new TotalFunction(P.type().src().vertices(), "f_V", S.type().src().vertices());
			
			for(Object vertice: P.type().src().vertices().elts()) {
				//map all vertices
				Object mapTarget = vertice;
				Object mappedType = P.type()._V().map(vertice);
				
				if(gluedObjects.containsKey(vertice)) { //vertice is glued -> map to glue
					mapTarget = gluedObjects.get(vertice);
				}
				
				S.type().src().vertices().elts().add(mapTarget);
				f_V.addMapping(vertice, mapTarget);
				
				//map vertice type
				S.type()._V().addMapping(mapTarget, mappedType);
			}
			for(Object edge: P.type().src().edges().elts()) {
				
				Object mappedType = P.type()._E().map(edge);
				Object src = P.type().src().src().map(edge);
				Object trg = P.type().src().trg().map(edge);
				
				if(gluedObjects.containsKey(P.type().src().src().map(edge))) {
					src = gluedObjects.get(P.type().src().src().map(edge));
					//edge src has been glued
					if(gluedObjects.containsKey(P.type().src().trg().map(edge))) {
						trg = gluedObjects.get(P.type().src().trg().map(edge));
						//edge src and target have been glued
						Object origSrc = P.type().src().src().map(edge);
						Object origTrg = P.type().src().trg().map(edge);
						
						if(gluedObjects.get(origSrc).equals(src) && gluedObjects.get(origTrg).equals(trg)) {
							//find existing edge and map edge to existing edge
							for(Object edgeInR: R.type().src().edges().elts()) {
								Object rSrc = R.type().src().src().map(edgeInR);
								Object rTrg = R.type().src().trg().map(edgeInR);;
								if(rSrc.equals(src) && rTrg.equals(trg)) { //found edge
									S.type().src().edges().elts().add(edgeInR);
									f_E.addMapping(edge, edgeInR);
									break;
								}
							}
							break;
						}
						//else add edge
					}
				}
				else {
					if(gluedObjects.containsKey(P.type().src().trg().map(edge))) {
						//edge target has been glued, but not src
						trg = gluedObjects.get(P.type().src().trg().map(edge));
					}
				}
				S.type().src().edges().elts().add(edge);
				f_E.addMapping(edge, edge);
				//map edge src and edge trg
				S.type().src().src().addMapping(edge, src);
				S.type().src().trg().addMapping(edge, trg);
				//map edge type
				S.type()._E().addMapping(edge, mappedType);
			}
			
			GraphMorphism pS = new GraphMorphism("p", P.type().src(), S.type().src(), f_E, f_V);
			p = new TGraphMorphism("p", pS, P, S);
			
			//construct s from gluedObjects
			f_E = new TotalFunction(R.type().src().edges(), "f_E", S.type().src().edges());
			f_V = new TotalFunction(R.type().src().vertices(), "f_V", S.type().src().vertices());
			
			for(Object vertice: R.type().src().vertices().elts()) {
				//map all vertices
				S.type().src().vertices().elts().add(vertice);
				f_V.addMapping(vertice, vertice);
				//map vertice type
				Object mappedType = R.type()._V().map(vertice);
				S.type()._V().addMapping(vertice, mappedType);
			}
			for(Object edge: R.type().src().edges().elts()) {
				//map edge
				S.type().src().edges().elts().add(edge);
				f_E.addMapping(edge, edge);
				
				//map edge src and edge trg
				Object src = R.type().src().src().map(edge);
				Object trg = R.type().src().trg().map(edge);
				
				S.type().src().src().addMapping(edge, src);
				S.type().src().trg().addMapping(edge, trg);
				//map edge type
				Object mappedType = R.type()._E().map(edge);
				S.type()._E().addMapping(edge, mappedType);
			}
			
			GraphMorphism rS = new GraphMorphism("s", R.type().src(), S.type().src(), f_E, f_V);
			s = new TGraphMorphism("s", rS, R, S);
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
		
		private void initTGraph(String name) {
			FinSet vertices = new FinSet("vertices");
			FinSet edges = new FinSet("edges");
			TotalFunction src = new TotalFunction(edges, "src", vertices);
			TotalFunction trg = new TotalFunction(edges, "trg", vertices);
			Graph untyped = new Graph(name, edges, vertices, src, trg);
			TotalFunction f_E = new TotalFunction(edges, "f_E", typeGraph.edges());
			TotalFunction f_V = new TotalFunction(vertices, "f_V", typeGraph.vertices());
			type = new GraphMorphism("type", untyped, typeGraph, f_E, f_V);
			S = new TGraph(name, type);
		}
		
		public Glue(String name) {
			initTGraph(name);
		}
		
		public Glue(Glue old) {
			this.initTGraph(old.S.label());
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
			if (this.gluedObjects.keySet().size() != g.gluedObjects.keySet().size()) {
				return false;
			}
			for (Object pVertice: this.gluedObjects.keySet()) {
				if (!this.gluedObjects.get(pVertice).equals(g.gluedObjects.get(pVertice))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public int compareTo(Glue arg0) {
			if (this.equals(arg0)) {
				return 0;
			}
			return 1;
		}
	}
}
