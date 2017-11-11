package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.State;

import java.util.Comparator;

/**
 * ArcSort operation.
 */
public class ArcSort {

  private ArcSort() { }

  /**
   * Applies the ArcSort on the provided fst. Sorting can be applied either on
   * input or output label based on the provided comparator.
   * 
   * <p>ArcSort can be applied to both {@link com.javafst.Fst} and
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
