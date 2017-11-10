package com.javafst.operations;

import java.util.Comparator;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.State;

/**
 * ArcSort operation.
 */
public class ArcSort {
  /**
   * Default Constructor
   */
  private ArcSort() {
  }

  /**
   * Applies the ArcSort on the provided fst. Sorting can be applied either on
   * input or output label based on the provided comparator.
   * 
   * ArcSort can be applied to both {@link com.javafst.Fst} and
   * {@link com.javafst.ImmutableFst}
   * 
   * @param fst the fst to sort it's arcs
   * @param cmp the provided Comparator
   */
  public static void apply(Fst fst, Comparator<Arc> cmp) {
    int numStates = fst.getNumStates();
    for (int i = 0; i < numStates; i++) {
      State s = fst.getState(i);
      s.arcSort(cmp);
    }
  }
}
