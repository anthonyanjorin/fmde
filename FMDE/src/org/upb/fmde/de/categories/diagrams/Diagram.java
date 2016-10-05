package org.upb.fmde.de.categories.diagrams;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.upb.fmde.de.categories.Category;

public class Diagram<Ob, Arr> {
	private Category<Ob, Arr> cat;
	
	private Collection<Ob> objects;
	private Collection<Arr> arrows;
	
	public Diagram(Category<Ob, Arr> cat) {
		this.cat = cat;
		objects = new LinkedHashSet<>();
		arrows = new LinkedHashSet<>();
	}
	
	@SuppressWarnings("unchecked")
	public Diagram<Ob, Arr> objects(Ob... o){
		for (Ob ob : o) objects.add(ob);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public Diagram<Ob, Arr> arrows(Arr... a){
		for(Arr arr : a) arrows.add(arr);
		return this;
	}
	
	public Diagram<Ob, Arr> saveAsDot(String dir, String fileName) throws IOException {
		saveAsDot(new File(dir + fileName + ".plantuml"), new CatPrinter<>(this));
		return this;
	}
	
	public Diagram<Ob, Arr> saveAsDot(File file, DotPrinter p) throws IOException{
		String dotFile = p.print();
		FileWriter w = new FileWriter(file);
		w.write(dotFile);
		w.close();
		return this;
	}
	
	public void prettyPrint(String dir, String fileName) throws IOException{
		throw new UnsupportedOperationException("This diagram cannot be pretty printed!");
	}

	public Category<Ob, Arr> getCat() {
		return cat;
	}

	public Collection<Ob> getObjects() {
		return objects;
	}

	public Collection<Arr> getArrows() {
		return arrows;
	}
	
	public Arr getArrow(String label){
		return arrows.stream().filter(a -> cat.showArr(a).equals(label)).findAny().get();
	}
	
	public Ob getObject(String label){
		return objects.stream().filter(o -> cat.showOb(o).equals(label)).findAny().get();
	}

	public Diagram<Ob, Arr> arrows(Collection<Arr> arrows) {
		this.arrows.addAll(arrows);
		return this;
	}

	public Diagram<Ob, Arr> objects(Collection<Ob> objects) {
		this.objects.addAll(objects);
		return this;
	}
}
