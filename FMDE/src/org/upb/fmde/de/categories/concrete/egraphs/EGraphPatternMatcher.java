package org.upb.fmde.de.categories.concrete.egraphs;

import org.upb.fmde.de.categories.PatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.FinSetPatternMatcher;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class EGraphPatternMatcher extends PatternMatcher<EGraph, EGraphMorphism> {

    public EGraphPatternMatcher(EGraph pattern, EGraph host) {
        super(pattern, host);
    }

    @Override
    public List<EGraphMorphism> determineMatches(boolean mono) {
        return determineMatches(mono, (from, to) -> true, (from, to) -> true);
    }

    public List<EGraphMorphism> determineMatches(boolean mono, BiPredicate<Object, Object> edgeFilter, BiPredicate<Object, Object> nodeFilter) {
        this.mono = mono;
        List<EGraphMorphism> matches = new ArrayList<>();

        FinSetPatternMatcher pm_Eg = new FinSetPatternMatcher(pattern.Eg, host.Eg);
        for (TotalFunction m_Eg : pm_Eg.determineMatches(mono, edgeFilter)) {

            FinSetPatternMatcher pm_Ena = new FinSetPatternMatcher(pattern.Ena, host.Ena);
            for (TotalFunction m_Ena : pm_Ena.determineMatches(mono, (from, to) -> true)) {

                FinSetPatternMatcher pm_Eea = new FinSetPatternMatcher(pattern.Eea, host.Eea);
                for (TotalFunction m_Eea : pm_Eea.determineMatches(mono, (from, to) -> true)) {

                    FinSetPatternMatcher pm_Vg = new FinSetPatternMatcher(pattern.Vg, host.Vg);
                    for (TotalFunction m_Vg : pm_Vg.determineMatches(mono, (from, to) -> true)) {

                        FinSetPatternMatcher pm_Vd = new FinSetPatternMatcher(pattern.Vd, host.Vd);
                        for (TotalFunction m_Vd : pm_Vd.determineMatches(mono, Object::equals)) {

                            try {
                                EGraphMorphism morphism = new EGraphMorphism("m",
                                    pattern, host, m_Vg, m_Vd, m_Eg, m_Ena, m_Eea);
                                matches.add(morphism);
                            } catch (Exception e) {

                            }

                        }

                    }

                }

            }
        }

        return matches;
    }
}
