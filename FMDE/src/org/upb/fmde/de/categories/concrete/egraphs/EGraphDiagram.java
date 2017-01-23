package org.upb.fmde.de.categories.concrete.egraphs;

import org.upb.fmde.de.categories.diagrams.Diagram;

import java.io.File;
import java.io.IOException;

public class EGraphDiagram extends Diagram<EGraph, EGraphMorphism> {

    public EGraphDiagram() {
        super(EGraphs.eGraphs);
    }

    @Override
    public void prettyPrint(String dir, String fileName) throws IOException {
        saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new EGraphPrinter(this));
    }
}
