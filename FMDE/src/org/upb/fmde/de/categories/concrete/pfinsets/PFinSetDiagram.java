package org.upb.fmde.de.categories.concrete.pfinsets;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class PFinSetDiagram extends Diagram<FinSet, PartialFunction> {

	public PFinSetDiagram() {
		super(PFinSets.PFinSets);
	}
		
	@Override
	public void prettyPrint(String dir, String fileName) throws IOException{
		saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new PFinSetPrinter(this));
	}
}