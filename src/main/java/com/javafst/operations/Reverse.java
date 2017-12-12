package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.State;
import com.javafst.semiring.Semiring;

/**
 * Reverse operation.
 */
public class Reverse {

  /**
   * Reverses an fst.
   * 
   * @param fst    The fst to reverse.
   * @return       The reversed fst.
   */
  public static Fst get(final Fst fst) {
    if (fst.getSemiring() == null) {
      return null;
    }
    ExtendFinal.apply(fst);
    final Semiring semiring = fst.getSemiring();
    final Fst res = new Fst(fst.getNumStates());
    res.setSemiring(semiring);
    res.setIsyms(fst.getOsyms());
    res.setOsyms(fst.getIsyms());
    final State[] stateMap = new State[fst.getNumStates()];
    final int numStates = fst.getNumStates();
    for (int i = 0; i < numStates; i++) {
      final State is = fst.getState(i);
      final State s = new State(semiring.zero());
      res.addState(s);
      stateMap[is.getId()] = s;
      if (is.getFinalWeight() != semiring.zero()) {
        res.setStart(s);
      }
    }

    stateMap[fst.getStart().getId()].setFinalWeight(semiring.one());

    for (int i = 0; i < numStates; i++) {
      final State olds = fst.getState(i);
      final State news = stateMap[olds.getId()];
      for (int j = 0; j < olds.getNumArcs(); j++) {
        final Arc olda = olds.getArc(j);
        final State next = stateMap[olda.getNextState().getId()];
        final Arc newa = new Arc(olda.getIlabel(), olda.getOlabel(),
            semiring.reverse(olda.getWeight()), news);
        next.addArc(newa);
      }
    }

    ExtendFinal.undo(fst);
    return res;
  }
}
