package org.upb.fmde.de.categories.concrete.tgraphs;

import static org.upb.fmde.de.categories.concrete.graphs.Graphs.Graphs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

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
import org.upb.fmde.de.tests.TestUtil;

public class TGraphs implements LabelledCategory<TGraph, TGraphMorphism>, 
								CategoryWithInitOb<TGraph, TGraphMorphism>, 
								CategoryWithPushouts<TGraph, TGraphMorphism>,
								CategoryWithPushoutComplements<TGraph, TGraphMorphism> {
	private final Graph typeGraph;
	private final TGraph EMPTY_TYPED_GRAPH;
	private final CoLimit<TGraph, TGraphMorphism> INITIAL_OBJECT;
	
	private static final String diagrams = "diagrams/application-conditions/";
	
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
		allT_TiArrows.add(q_t.left);
		
		try {
			TGraphDiagram d = new TGraphDiagram(this.typeGraph);				
			TGraph P = upperLeftCorner.left.src();
			TGraph C = upperLeftCorner.left.trg();
			TGraph S = q_t.left.src();
			TGraph T = q_t.right.trg();
			T.label("T");
			d.objects(P, C, S, T).arrows(q_t.left, q_t.right, upperLeftCorner.left, upperLeftCorner.right);
			TestUtil.prettyPrintTEcore(d, "P_C_S_T_" + q_t.hashCode(), diagrams + "P_C_S_T/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Add all other Tis.
		for(Glue glue: constructEpimorphicGlues(q_t)) {
			allT_TiArrows.add(glue.s);
			
			try {
				TGraphDiagram d = new TGraphDiagram(this.typeGraph);
				TGraph S = q_t.left.src();
				S.type().src().label("S");
				TGraph T = q_t.right.trg();
				T.type().src().label("T");
				TGraph Ti = glue.S;
				Ti.type().src().label("Ti");
				d.objects(S, T, Ti).arrows(glue.s);
				TestUtil.prettyPrintTEcore(d, "T_Ti_" + T.hashCode() + "_" + Ti.hashCode(), diagrams + "T_Ti/");
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		List<Glue> all_p_s = constructEpimorphicGlues(P, R);
		
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
		TGraphMorphism z = Z.get().first;
		TGraphMorphism r_star = Z.get().second;
		
		//Calculate Y
		CoLimit<CoSpan<TGraphMorphism>, TGraphMorphism> Y = pushout(new Span<TGraphMorphism>(this, l, z));
		TGraphMorphism y = Y.obj.right;
		TGraphMorphism l_star = Y.obj.left;
		
		List<TGraphMorphism> allYis = new ArrayList<TGraphMorphism>();
		
		//Calculate all Zi
		for (TGraphMorphism xi : all_t_ti) {
			Optional<Corner<TGraphMorphism>> Zi = pushoutComplement(new Corner<TGraphMorphism>(this, r_star, xi));
			if(!Zi.isPresent()) {
				continue;
			}
			TGraphMorphism zi = Zi.get().first;
			
			//Calculate Di
			CoLimit<CoSpan<TGraphMorphism>, TGraphMorphism> Di = pushout(new Span<TGraphMorphism>(this, l_star, zi));
			TGraphMorphism yi = Di.obj.right;
			
			allYis.add(yi);
			
			try {
				TGraphDiagram d = new TGraphDiagram(this.typeGraph);
				
				TGraph Y_TGraph = y.trg();
				Y_TGraph.type().src().label("Y");
				TGraph Z_TGraph = z.trg();
				Z_TGraph.type().src().label("Z");
				TGraph X_TGraph = s.trg(); // X = S
				X_TGraph.type().src().label("X=S");
				TGraph Di_TGraph = yi.trg();
				Di_TGraph.type().src().label("Di");
				TGraph Zi_TGraph = zi.trg();
				Zi_TGraph.type().src().label("Zi");
				TGraph Ci_TGraph = xi.trg(); // C_i = T_i
				Ci_TGraph.type().src().label("Ci=Ti");
				
				d.objects(Y_TGraph, Z_TGraph, X_TGraph, Di_TGraph, Zi_TGraph, Ci_TGraph)
					.arrows(l_star, r_star, zi, yi, xi);
				TestUtil.prettyPrintTEcore(d, "Y_Z_X_Di_Zi_Ci_" + X_TGraph.hashCode() + "_" + Di_TGraph.hashCode(), diagrams + "Y_Z_X_Di_Zi_Ci/");
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	
	private List<Glue> findGlues(Map<Object, List<Object>> typeMappingR, TGraph P, Glue g) {
		List<Glue> foundGlues = new ArrayList<Glue>();
		
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
				
				for (Glue glue : findGlues(typeMappingR, P, x)) {
					if (!foundGlues.contains(glue)) {
						foundGlues.add(glue);
					}
				}
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
	
	public List<Glue> constructEpimorphicGlues(TGraph P, TGraph R) {
		Map<Object, List<Object>> typeMappingR = constructTypeMapping(R);
		
		List<Glue> allGlues = new ArrayList<Glue>();
		Glue unglued = new Glue("S");
		unglued.constructMorphismsFromMapping(P, R);
		allGlues.add(unglued);
		for (Glue glue : findGlues(typeMappingR, P, unglued)) {
			if (!allGlues.contains(glue)) {
				glue.constructMorphismsFromMapping(P, R);
				allGlues.add(glue);
			}
		}
		
		return allGlues;
	}

	public List<Glue> constructEpimorphicGlues(CoSpan<TGraphMorphism> q_t) {
		TGraphMorphism t = q_t.left;
		TGraphMorphism q = q_t.right;
		
		TGraph C = q.src();
		TGraph S = t.src();
		
		Map<Object, List<Object>> typeMappingS = constructTypeMapping(S);
		
		List<Glue> allGlues = new ArrayList<Glue>();
		Glue pushoutGlue = new Glue(q_t);
		for (Glue glue : findGlues(typeMappingS, C, pushoutGlue)) {
			if (!allGlues.contains(glue) && !glue.equals(pushoutGlue))  {
				glue.constructMorphismsFromMapping(C, S);
				allGlues.add(glue);
			}
		}
		
		return allGlues;
		
		//p => q;ti
		//s => t;ti
	}
	
	private class Glue {
		
		private TGraph S;
		
		GraphMorphism type;
		
		TGraphMorphism p;
		
		TGraphMorphism s;
		
		List<Object> gluedPVertices = new ArrayList<Object>();
		List<Object> gluedRVertices = new ArrayList<Object>();
		
		Map<Object,Object> gluedObjects = new HashMap<Object, Object>();
		
		private void constructMorphismsFromMapping(TGraph P, TGraph R) {
			//construct p from gluedObjects
			TotalFunction f_E_p = new TotalFunction(P.type().src().edges(), "f_E", S.type().src().edges());
			TotalFunction f_V_p = new TotalFunction(P.type().src().vertices(), "f_V", S.type().src().vertices());
			
			for(Object vertice: P.type().src().vertices().elts()) {
				//map all vertices
				Object mapTarget = vertice;
				Object mappedType = P.type()._V().map(vertice);
				
				if(gluedObjects.containsKey(vertice)) { //vertice is glued -> map to glue
					mapTarget = gluedObjects.get(vertice);
				}
				
				S.type().src().vertices().elts().add(mapTarget);
				f_V_p.addMapping(vertice, mapTarget);
				
				//map vertice type
				S.type()._V().addMapping(mapTarget, mappedType);
			}
			for(Object edge: P.type().src().edges().elts()) {
				
				Object mappedType = P.type()._E().map(edge);
				Object src = P.type().src().src().map(edge);
				Object trg = P.type().src().trg().map(edge);
				
				if (gluedObjects.containsKey(src)) {
					src = gluedObjects.get(src);
					//edge src has been glued
					if (gluedObjects.containsKey(trg)) {
						//edge src and target have been glued
						trg = gluedObjects.get(trg);
						
						boolean foundEdgeInR = false;
						for (Object edgeInR: R.type().src().edges().elts()) {
							Object rSrc = R.type().src().src().map(edgeInR);
							Object rTrg = R.type().src().trg().map(edgeInR);
							if (rSrc.equals(src) && rTrg.equals(trg) && mappedType == R.type()._E().map(edgeInR)) { //found edge
								foundEdgeInR = true;
								f_E_p.addMapping(edge, edgeInR);
								break;
							}
						}
						if (foundEdgeInR) {
							continue;
						}
					}
				}
				else {
					if(gluedObjects.containsKey(P.type().src().trg().map(edge))) {
						//edge target has been glued, but not src
						trg = gluedObjects.get(P.type().src().trg().map(edge));
					}
				}
				S.type().src().edges().elts().add(edge);
				f_E_p.addMapping(edge, edge);
				//map edge src and edge trg
				S.type().src().src().addMapping(edge, src);
				S.type().src().trg().addMapping(edge, trg);
				//map edge type
				S.type()._E().addMapping(edge, mappedType);
			}
			
			//construct s from gluedObjects
			TotalFunction f_E_s = new TotalFunction(R.type().src().edges(), "f_E", S.type().src().edges());
			TotalFunction f_V_s = new TotalFunction(R.type().src().vertices(), "f_V", S.type().src().vertices());
			
			for(Object vertice: R.type().src().vertices().elts()) {
				//map all vertices
				if (!this.gluedObjects.containsValue(vertice)) {
					S.type().src().vertices().elts().add(vertice);
					//map vertice type
					Object mappedType = R.type()._V().map(vertice);
					S.type()._V().addMapping(vertice, mappedType);
				}
				f_V_s.addMapping(vertice, vertice);
			}
			for(Object edge: R.type().src().edges().elts()) {
				//map edge
				S.type().src().edges().elts().add(edge);
				f_E_s.addMapping(edge, edge);
				
				//map edge src and edge trg
				Object src = R.type().src().src().map(edge);
				Object trg = R.type().src().trg().map(edge);
				
				S.type().src().src().addMapping(edge, src);
				S.type().src().trg().addMapping(edge, trg);
				
				//map edge type
				Object mappedType = R.type()._E().map(edge);
				S.type()._E().addMapping(edge, mappedType);
			}
			
			GraphMorphism pS = new GraphMorphism("p", P.type().src(), S.type().src(), f_E_p, f_V_p);
			p = new TGraphMorphism("p", pS, P, S);
			
			GraphMorphism rS = new GraphMorphism("s", R.type().src(), S.type().src(), f_E_s, f_V_s);
			s = new TGraphMorphism("s", rS, R, S);
			
			TGraphDiagram d = new TGraphDiagram(P.type().src());
			d.objects(P, R, S).arrows(p, s);
			try {
				TestUtil.prettyPrintTEcore(d, "P_R_S_" + S.hashCode(), diagrams + "P_R_S/");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		
		public Glue(CoSpan<TGraphMorphism> q_t) {
			TGraphMorphism q = q_t.left;
			TGraphMorphism t = q_t.right;
			
			this.initTGraph("T");
			
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
			if (old == null) {
				System.out.println("old is null");
			}
			if (old.S == null) {
				System.out.println("old.S is null");
			}
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
			if (this.gluedObjects.entrySet().size() != g.gluedObjects.entrySet().size()) {
				return false;
			}
			for (Entry<Object, Object> verticeMapping: this.gluedObjects.entrySet()) {
				Object pVertice = verticeMapping.getKey();
				if (verticeMapping.getValue() != g.gluedObjects.get(pVertice)) {
					return false;
				}
			}
			for (Entry<Object, Object> verticeMapping: g.gluedObjects.entrySet()) {
				Object pVertice = verticeMapping.getKey();
				if (verticeMapping.getValue() != this.gluedObjects.get(pVertice)) {
					return false;
				}
			}
			return true;
		}
	}
}
