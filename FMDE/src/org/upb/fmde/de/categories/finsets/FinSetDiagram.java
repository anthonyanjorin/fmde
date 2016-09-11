package org.upb.fmde.de.categories.finsets;

import java.io.File;
import java.io.IOException;

import org.upb.fmde.de.categories.Diagram;

public class FinSetDiagram extends Diagram<FinSet, TotalFunction> {

	public FinSetDiagram() {
		super(FinSets.FinSets);
	}
		
	@Override
	public void prettyPrint(String dir, String fileName) throws IOException{
		saveAsDot(new File(dir + fileName + ".pretty.plantuml"), new FinSetPrinter(this));
	}
}
