package org.upb.fmde.de.tests.single_po;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.pfinsets.PFinSetDiagram;
import org.upb.fmde.de.categories.concrete.pfinsets.PFinSets;
import org.upb.fmde.de.categories.concrete.pfinsets.PartialFunction;
import org.upb.fmde.de.categories.diagrams.Diagram;
import org.upb.fmde.de.tests.TestUtil;

public class PartialFunctionTest {
	private static final String diagrams = "diagrams/single_po/partial_function_tests/";
	
	@BeforeClass
	public static void clear() {
		TestUtil.clear(diagrams);
	}

	@Test
	public void partialFunctionTests() throws IOException {
		Diagram<FinSet, PartialFunction> d = createDiagram1();

		d.saveAsDot(diagrams, "partialFunctionExample")
		 .prettyPrint(diagrams, "partialFunctionExample");

		
		
		Diagram<FinSet, PartialFunction> d1 = createDiagram1();
		
		PartialFunction composedFunction = PFinSets.PFinSets.compose(d1.getArrow("f"), d1.getArrow("g"));
		d1.arrows(composedFunction);
		
		d1.saveAsDot(diagrams, "composedPartialFunctionExample")
		 .prettyPrint(diagrams, "composedPartialFunctionExample");
		
		assertTrue(composedFunction.map("x_1")==null);
		assertTrue(composedFunction.map("x_2")== "z_2");	
		
	}
		
	
	private PFinSetDiagram createDiagram1() {
		FinSet X = new FinSet("X", "x_1", "x_2", "x_3");
		FinSet Y = new FinSet("Y", "y_1", "y_2", "y_3", "y_4");
		FinSet Z = new FinSet("Z",  "z_1", "z_2", "z_3");
		
		PartialFunction f = new PartialFunction(X, "f", Y)
				.addMapping(X.get("x_1"), Y.get("y_3"))
				.addMapping(X.get("x_2"), Y.get("y_1"))
				.addMapping(X.get("x_3"), Y.get("y_2"));
		
		PartialFunction g = new PartialFunction(Y, "g", Z)
				.addMapping(Y.get("y_1"), Z.get("z_2"))
				.addMapping(Y.get("y_2"), Z.get("z_3"));
						
		PFinSetDiagram d1 = new PFinSetDiagram();
		d1.objects(X, Y, Z).arrows(g, f);

		return d1;
	}
}
