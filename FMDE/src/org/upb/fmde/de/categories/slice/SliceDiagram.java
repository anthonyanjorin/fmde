package org.upb.fmde.de.categories.slice;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.LabelledArrow;
import org.upb.fmde.de.categories.diagrams.Diagram;

public class SliceDiagram<Ob, Arr extends LabelledArrow<Ob>> extends Diagram<Arr, Triangle<Ob, Arr>> {

	public SliceDiagram(Category<Arr, Triangle<Ob, Arr>> cat) {
		super(cat);
	}

}
