package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.ImmutableFst;
import com.javafst.State;
import com.javafst.semiring.Semiring;
import com.javafst.utils.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Compose operation.
 * 
 * <p>See: M. Mohri, "Weighted automata algorithms", Handbook of Weighted Automata.
 * Springer, pp. 213-250, 2009.
 */

public class Compose {

  /**
   * Computes the composition of two Fsts. Assuming no epsilon transitions.
   * 
   * <p>Input Fsts are not modified.
   * 
   * @param fst1 the first Fst
   * @param fst2 the second Fst
   * @param semiring the semiring to use in the operation
   * @param sorted sort result
   * @return the composed Fst
   */
  public static Fst compose(final Fst fst1, final Fst fst2, final Semiring semiring, final boolean sorted) {
    if (!Arrays.equals(fst1.getOsyms(), fst2.getIsyms())) {
      // symbol tables do not match
      return null;
    }

    final Fst res = new Fst(semiring);

    final HashMap<Pair<State, State>, State> stateMap = new HashMap<Pair<State, State>, State>();
    final Queue<Pair<State, State>> queue = new LinkedList<Pair<State, State>>();

    State s1 = fst1.getStart();
    State s2 = fst2.getStart();

    if ((s1 == null) || (s2 == null)) {
      System.err.println("Cannot find initial state.");
      return null;
    }

    Pair<State, State> p = new Pair<State, State>(s1, s2);
    State s = new State(semiring.times(s1.getFinalWeight(),
        s2.getFinalWeight()));

    res.addState(s);
    res.setStart(s);
    stateMap.put(p, s);
    queue.add(p);

    while (!queue.isEmpty()) {
      p = queue.remove();
      s1 = p.getLeft();
      s2 = p.getRight();
      s = stateMap.get(p);
      for (final Arc a1 : s1.arcs()) {
        for (final Arc a2 : s2.arcs()) {
          if (sorted && a1.getOlabel() < a2.getIlabel()) {
            break;
          }
          if (a1.getOlabel() == a2.getIlabel()) {
            final State nextState1 = a1.getNextState();
            final State nextState2 = a2.getNextState();
            final Pair<State, State> nextPair = new Pair<State, State>(
                nextState1, nextState2);
            State nextState = stateMap.get(nextPair);
            if (nextState == null) {
              nextState = new State(semiring.times(
                  nextState1.getFinalWeight(),
                  nextState2.getFinalWeight()));
              res.addState(nextState);
              stateMap.put(nextPair, nextState);
              queue.add(nextPair);
            }
            Arc a = new Arc(a1.getIlabel(), a2.getOlabel(),
                semiring.times(a1.getWeight(), a2.getWeight()),
                nextState);
            s.addArc(a);
          }
        }
      }
    }

    res.setIsyms(fst1.getIsyms());
    res.setOsyms(fst2.getOsyms());

    return res;
  }

  /**
   * Computes the composition of two Fsts. The two Fsts are augmented in order
   * to avoid multiple epsilon paths in the resulting Fst.
   * 
   * @param fst1 the first Fst
   * @param fst2 the second Fst
   * @param semiring the semiring to use in the operation
   * @return the composed Fst
   */
  public static Fst get(final Fst fst1, final Fst fst2, final Semiring semiring) {
    if ((fst1 == null) || (fst2 == null)) {
      return null;
    }

    if (!Arrays.equals(fst1.getOsyms(), fst2.getIsyms())) {
      // symbol tables do not match
      return null;
    }

    final Fst filter = getFilter(fst1.getOsyms(), semiring);
    augment(1, fst1, semiring);
    augment(0, fst2, semiring);

    final Fst tmp = Compose.compose(fst1, filter, semiring, false);
    final Fst res = Compose.compose(tmp, fst2, semiring, false);

    // Why is/was this commented out?
    // It causes the Compose test to fail.
    //Connect.apply(res);

    return res;
  }

  /**
   * Get a filter to use for avoiding multiple epsilon paths in the resulting
   * Fst.
   * 
   * <p>See: M. Mohri, "Weighted automata algorithms", Handbook of Weighted
   * Automata. Springer, pp. 213-250, 2009.
   * 
   * @param syms the gilter's input/output symbols
   * @param semiring the semiring to use in the operation
   * @return the filter
   */
  public static Fst getFilter(final String[] syms, final Semiring semiring) {
    final Fst filter = new Fst(semiring);

    final int e1index = syms.length;
    final int e2index = syms.length + 1;

    filter.setIsyms(syms);
    filter.setOsyms(syms);

    // State 0
    final State s0 = new State(syms.length + 3);
    s0.setFinalWeight(semiring.one());
    final State s1 = new State(syms.length);
    s1.setFinalWeight(semiring.one());
    final State s2 = new State(syms.length);
    s2.setFinalWeight(semiring.one());
    filter.addState(s0);
    s0.addArc(new Arc(e2index, e1index, semiring.one(), s0));
    s0.addArc(new Arc(e1index, e1index, semiring.one(), s1));
    s0.addArc(new Arc(e2index, e2index, semiring.one(), s2));
    for (int i = 1; i < syms.length; i++) {
      s0.addArc(new Arc(i, i, semiring.one(), s0));
    }
    filter.setStart(s0);

    // State 1
    filter.addState(s1);
    s1.addArc(new Arc(e1index, e1index, semiring.one(), s1));
    for (int i = 1; i < syms.length; i++) {
      s1.addArc(new Arc(i, i, semiring.one(), s0));
    }

    // State 2
    filter.addState(s2);
    s2.addArc(new Arc(e2index, e2index, semiring.one(), s2));
    for (int i = 1; i < syms.length; i++) {
      s2.addArc(new Arc(i, i, semiring.one(), s0));
    }

    return filter;
  }

  /**
   * Augments the labels of an Fst in order to use it for composition avoiding
   * multiple epsilon paths in the resulting Fst.
   * 
   * <p>Augment can be applied to both {@link com.javafst.Fst} and
   * {@link com.javafst.ImmutableFst}, as immutable fsts hold an
   * additional null arc for that operation.
   * 
   * @param label constant denoting if the augment should take place on input
   *            or output labels For value equal to 0 augment will take place
   *            for input labels For value equal to 1 augment will take place
   *            for output labels
   * @param fst the fst to augment
   * @param semiring the semiring to use in the operation
   */
  public static void augment(final int label, final Fst fst, final Semiring semiring) {
    // label: 0->augment on ilabel
    // 1->augment on olabel

    final String[] isyms = fst.getIsyms();
    final String[] osyms = fst.getOsyms();

    final int e1inputIndex = isyms.length;
    final int e2inputIndex = isyms.length + 1;

    final int e1outputIndex = osyms.length;
    final int e2outputIndex = osyms.length + 1;

    for (final State s : fst.states()) {
      // Immutable fsts hold an additional (null) arc for augmention
      final int numArcs = (fst instanceof ImmutableFst) ? s.getNumArcs() - 1
          : s.getNumArcs();
      for (int j = 0; j < numArcs; j++) {
        final Arc a = s.getArc(j);
        if ((label == 1) && (a.getOlabel() == 0)) {
          a.setOlabel(e2outputIndex);
        } else if ((label == 0) && (a.getIlabel() == 0)) {
          a.setIlabel(e1inputIndex);
        }
      }
      if (label == 0) {
        if (fst instanceof ImmutableFst) {
          s.setArc(numArcs, new Arc(e2inputIndex, 0, semiring.one(),
              s));
        } else {
          s.addArc(new Arc(e2inputIndex, 0, semiring.one(), s));
        }
      } else if (label == 1) {
        if (fst instanceof ImmutableFst) {
          s.setArc(numArcs, new Arc(0, e1outputIndex, semiring.one(),
              s));
        } else {
          s.addArc(new Arc(0, e1outputIndex, semiring.one(), s));
        }
      }
    }
  }
}
