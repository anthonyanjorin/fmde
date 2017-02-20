package org.upb.fmde.de.categories.concrete.egraphs;

import org.upb.fmde.de.categories.diagrams.Diagram;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class EGraphDiagram extends Diagram<EGraph, EGraphMorphism> {

    private Collection<Object> attributes;

    public EGraphDiagram() {
        super(EGraphs.eGraphs);
    }

    public Collection<Object> getAttributes() {
        return attributes;
    }

    @Override
    public void prettyPrint(String dir, String fileName) throws IOException {

        // from all EGraphs collect all attributes in a set (to remove duplicates)
        attributes = getObjects().stream()
            .flatMap(graph -> graph.Vd.elts().stream())
            .collect(Collectors.toSet());

        saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new EGraphPrinter(this));
    }
}
