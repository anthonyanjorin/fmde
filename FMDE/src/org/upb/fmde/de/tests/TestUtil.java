package org.upb.fmde.de.tests;

import java.io.File;
import java.util.Optional;

public class TestUtil {

	public static void clear(String diagrams) {
		Optional.ofNullable(new File(diagrams)).ifPresent(d -> {
			for (File f : d.listFiles()) 
				f.delete();
		});
	}
}