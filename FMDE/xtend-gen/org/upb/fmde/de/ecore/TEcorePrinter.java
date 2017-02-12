package org.upb.fmde.de.ecore;

import java.util.Collection;
import java.util.function.Consumer;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.slicecat.Triangle;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;
import org.upb.fmde.de.ecore.EcorePrinter;

@SuppressWarnings("all")
public class TEcorePrinter extends EcorePrinter {
  public TEcorePrinter(final TGraphDiagram td) {
    super(new GraphDiagram());
    this.fillGraphDiagram(td);
  }
  
  public void fillGraphDiagram(final TGraphDiagram td) {
    Collection<GraphMorphism> _objects = td.getObjects();
    final Consumer<GraphMorphism> _function = (GraphMorphism o) -> {
      Graph _src = o.src();
      this.d.objects(_src);
    };
    _objects.forEach(_function);
    Collection<Triangle<Graph, GraphMorphism>> _arrows = td.getArrows();
    final Consumer<Triangle<Graph, GraphMorphism>> _function_1 = (Triangle<Graph, GraphMorphism> a) -> {
      GraphMorphism _m = a.getM();
      this.d.arrows(_m);
    };
    _arrows.forEach(_function_1);
  }
}
