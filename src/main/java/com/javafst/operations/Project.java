package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.ImmutableFst;
import com.javafst.State;

/**
 * Project operation. 
 */
public class Project {

  private Project() {
  }

  /**
   * Projects an fst onto its domain or range by either copying each arc's
   * input label to its output label or vice versa.
   * 
   * @param fst fst to modify
   * @param pType type of the projection
   */
  public static void apply(Fst fst, ProjectType pType) {
    if (pType == ProjectType.INPUT) {
      fst.setOsyms(fst.getIsyms());
    } else if (pType == ProjectType.OUTPUT) {
      fst.setIsyms(fst.getOsyms());
    }

    int numStates = fst.getNumStates();
    for (int i = 0; i < numStates; i++) {
      State s = fst.getState(i);
      // Immutable fsts hold an additional (null) arc
      int numArcs = (fst instanceof ImmutableFst) ? s.getNumArcs() - 1 : s
          .getNumArcs();
      for (int j = 0; j < numArcs; j++) {
        Arc a = s.getArc(j);
        if (pType == ProjectType.INPUT) {
          a.setOlabel(a.getIlabel());
        } else if (pType == ProjectType.OUTPUT) {
          a.setIlabel(a.getOlabel());
        }
      }
    }
  }
}
