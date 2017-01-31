package org.upb.fmde.de.tests;

import org.junit.Test;
import org.upb.fmde.de.categories.concrete.egraphs.EGraph;
import org.upb.fmde.de.categories.concrete.egraphs.EGraphDiagram;
import org.upb.fmde.de.categories.concrete.egraphs.EGraphMorphism;
import org.upb.fmde.de.categories.concrete.egraphs.EGraphPatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.upb.fmde.de.categories.concrete.egraphs.EGraphs.eGraphs;

public class TestsProject {
    private static final String diagramsDir = "diagrams/project/";

    @Test
    public void testEGraphComposition() {

        EGraph hostGraph = createHostGraph();
        EGraph typeGraph = createTypeGraph();
        EGraphMorphism type = createMorphism();

        assertTrue(eGraphs.compose(eGraphs.id(hostGraph), type).isTheSameAs(type));
        assertTrue(eGraphs.compose(type, eGraphs.id(typeGraph)).isTheSameAs(type));
    }

    @Test
    public void testEGraphPatternMatcher() {
        EGraph hostGraph = createHostGraph();
        EGraph patternGraph = createPatternGraph();

        EGraphPatternMatcher matcher = new EGraphPatternMatcher(patternGraph, hostGraph);
        List<EGraphMorphism> matches = matcher.determineMatches(false);
        assertEquals(matches.size(), 2);
    }

    @Test
    public void createDiagram() throws IOException {

        EGraph hostGraph = createHostGraph();
        EGraph typeGraph = createTypeGraph();
        EGraphMorphism typeMorphism = createMorphism();

        EGraphDiagram diagram = new EGraphDiagram();
        diagram
            .objects(hostGraph, typeGraph)
            .arrows(typeMorphism)
            .prettyPrint(diagramsDir, "diagram");

    }

    @Test
    public void createDiagram2() throws IOException {

        EGraph hostGraph = createHostGraph();
        EGraph patternGraph = createPatternGraph();

        EGraphDiagram diagram = new EGraphDiagram();
        diagram
            .objects(hostGraph, patternGraph)
            .prettyPrint(diagramsDir, "diagram2");
    }

    private EGraphMorphism createMorphism() {
        EGraph hostGraph = createHostGraph();
        EGraph typeGraph = createTypeGraph();

        TotalFunction tVg = new TotalFunction(hostGraph.Vg, "tVg", typeGraph.Vg);
        TotalFunction tVd = new TotalFunction(hostGraph.Vd, "tVd", typeGraph.Vd);
        TotalFunction tEg = new TotalFunction(hostGraph.Eg, "tEg", typeGraph.Eg);
        TotalFunction tEna = new TotalFunction(hostGraph.Ena, "tEna", typeGraph.Ena);
        TotalFunction tEea = new TotalFunction(hostGraph.Eea, "tEea", typeGraph.Eea);

        tVg.addMapping("p1", "Player");
        tVg.addMapping("p2", "Player");
        tVg.addMapping("p3", "Player");
        tVg.addMapping("plat1", "Platform");
        tVg.addMapping("plat2", "Platform");
        tVg.addMapping("plat3", "Platform");
        tVg.addMapping("plat4", "Platform");
        tVd.addMapping("red", "Color");
        tVd.addMapping("blue", "Color");

        tEg.addMapping("bridge1", "Bridge");
        tEg.addMapping("bridge2", "Bridge");
        tEg.addMapping("gate1", "Gate");
        tEg.addMapping("gate2", "Gate");
        tEg.addMapping("gate2", "Gate");
        tEg.addMapping("on1", "On");
        tEg.addMapping("on2", "On");
        tEg.addMapping("on3", "On");

        tEna.addMapping("p1.color", "Player.color");
        tEna.addMapping("p2.color", "Player.color");
        tEna.addMapping("p3.color", "Player.color");
        tEea.addMapping("bridge1.color", "Bridge.color");
        tEea.addMapping("bridge2.color", "Bridge.color");
        tEea.addMapping("gate1.color", "Gate.color");
        tEea.addMapping("gate2.color", "Gate.color");

        return new EGraphMorphism("type", hostGraph, typeGraph, tVg, tVd, tEg, tEna, tEea);
    }

    private EGraph createPatternGraph() {
        FinSet vg = new FinSet("graph nodes", "srcPlatform", "trgPlatform", "player");
        FinSet vd = new FinSet("data nodes", "blue");

        FinSet eg = new FinSet("graph edges", "connection", "on");
        FinSet ena = new FinSet("node attribute edges", "player.color");
        FinSet eea = new FinSet("edge attribute edges", "connection.color");

        TotalFunction sourceG = new TotalFunction(eg, "sourceG", vg);
        sourceG.addMapping("connection", "srcPlatform");
        sourceG.addMapping("on", "player");

        TotalFunction targetG = new TotalFunction(eg, "targetG", vg);
        targetG.addMapping("connection", "trgPlatform");
        targetG.addMapping("on", "srcPlatform");

        TotalFunction sourceNA = new TotalFunction(ena, "sourceNA", vg);
        sourceNA.addMapping("player.color", "player");

        TotalFunction targetNA = new TotalFunction(ena, "targetNA", vd);
        targetNA.addMapping("player.color", "blue");

        TotalFunction sourceEA = new TotalFunction(eea, "sourceEA", eg);
        sourceEA.addMapping("connection.color", "connection");

        TotalFunction targetEA = new TotalFunction(eea, "targetEA", vd);
        targetEA.addMapping("connection.color", "blue");

        return new EGraph("PatternGraph", vg, vd, eg, ena, eea, sourceG, targetG, sourceNA, targetNA, sourceEA, targetEA);
    }

    private EGraph createHostGraph() {
        FinSet vg = new FinSet("graph nodes", "plat1", "plat2", "plat3", "plat4", "p1", "p2", "p3");
        FinSet vd = new FinSet("data nodes", "red", "blue");

        FinSet eg = new FinSet("graph edges", "bridge1", "bridge2", "gate1", "gate2", "on1", "on2", "on3");
        FinSet ena = new FinSet("node attribute edges", "p1.color", "p2.color", "p3.color");
        FinSet eea = new FinSet("edge attribute edges", "bridge1.color", "bridge2.color", "gate1.color", "gate2.color");

        TotalFunction sourceG = new TotalFunction(eg, "sourceG", vg);
        sourceG.addMapping("bridge1", "plat1");
        sourceG.addMapping("bridge2", "plat2");
        sourceG.addMapping("gate1", "plat1");
        sourceG.addMapping("gate2", "plat3");
        sourceG.addMapping("on1", "p1");
        sourceG.addMapping("on2", "p2");
        sourceG.addMapping("on3", "p3");

        TotalFunction targetG = new TotalFunction(eg, "targetG", vg);
        targetG.addMapping("bridge1", "plat4");
        targetG.addMapping("bridge2", "plat4");
        targetG.addMapping("gate1", "plat2");
        targetG.addMapping("gate2", "plat4");
        targetG.addMapping("on1", "plat1");
        targetG.addMapping("on2", "plat2");
        targetG.addMapping("on3", "plat4");

        TotalFunction sourceNA = new TotalFunction(ena, "sourceNA", vg);
        sourceNA.addMapping("p1.color", "p1");
        sourceNA.addMapping("p2.color", "p2");
        sourceNA.addMapping("p3.color", "p3");

        TotalFunction targetNA = new TotalFunction(ena, "targetNA", vd);
        targetNA.addMapping("p1.color", "blue");
        targetNA.addMapping("p2.color", "blue");
        targetNA.addMapping("p3.color", "red");

        TotalFunction sourceEA = new TotalFunction(eea, "sourceEA", eg);
        sourceEA.addMapping("bridge1.color", "bridge1");
        sourceEA.addMapping("bridge2.color", "bridge2");
        sourceEA.addMapping("gate1.color", "gate1");
        sourceEA.addMapping("gate2.color", "gate2");

        TotalFunction targetEA = new TotalFunction(eea, "targetEA", vd);
        targetEA.addMapping("bridge1.color", "blue");
        targetEA.addMapping("bridge2.color", "red");
        targetEA.addMapping("gate1.color", "blue");
        targetEA.addMapping("gate2.color", "red");

        return new EGraph("HostGraph", vg, vd, eg, ena, eea, sourceG, targetG, sourceNA, targetNA, sourceEA, targetEA);
    }

    private EGraph createTypeGraph() {
        FinSet vg = new FinSet("graph nodes", "Platform", "Player");
        FinSet vd = new FinSet("data nodes", "Color");

        FinSet eg = new FinSet("graph edges", "Bridge", "Gate", "On");
        FinSet ena = new FinSet("node attribute edges", "Player.color");
        FinSet eea = new FinSet("edge attribute edges", "Bridge.color", "Gate.color");

        TotalFunction sourceG = new TotalFunction(eg, "sourceG", vg);
        sourceG.addMapping("Bridge", "Platform");
        sourceG.addMapping("Gate", "Platform");
        sourceG.addMapping("On", "Player");

        TotalFunction targetG = new TotalFunction(eg, "targetG", vg);
        targetG.addMapping("Bridge", "Platform");
        targetG.addMapping("Gate", "Platform");
        targetG.addMapping("On", "Platform");

        TotalFunction sourceNA = new TotalFunction(ena, "sourceNA", vg);
        sourceNA.addMapping("Player.color", "Player");

        TotalFunction targetNA = new TotalFunction(ena, "targetNA", vd);
        targetNA.addMapping("Player.color", "Color");

        TotalFunction sourceEA = new TotalFunction(eea, "sourceEA", eg);
        sourceEA.addMapping("Bridge.color", "Bridge");
        sourceEA.addMapping("Gate.color", "Gate");

        TotalFunction targetEA = new TotalFunction(eea, "targetEA", vd);
        targetEA.addMapping("Bridge.color", "Color");
        targetEA.addMapping("Gate.color", "Color");

        return new EGraph("TypeGraph", vg, vd, eg, ena, eea, sourceG, targetG, sourceNA, targetNA, sourceEA, targetEA);
    }
}
