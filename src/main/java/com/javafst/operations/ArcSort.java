package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.State;

import java.util.Comparator;

/**
 * ArcSort operation.
 */
public class ArcSort {

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
  public static void apply(final Fst fst, final Comparator<Arc> cmp) {
    for (final State s : fst.states()) {
      s.arcSort(cmp);
    }
  }
}
