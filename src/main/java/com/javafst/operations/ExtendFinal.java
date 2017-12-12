package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.State;
import com.javafst.semiring.Semiring;

import java.util.ArrayList;

/**
 * Extend an Fst to a single final state and undo operations.
 */
public class ExtendFinal {

  /**
   * Extends an Fst to a single final state.
   * 
   * <p>It adds a new final state with a 0.0 (Semiring's 1) final weight and
   * connects the current final states to it using epsilon transitions with
   * weight equal to the original final state's weight.
   * 
   * @param fst the Fst to extend
   */
  public static void apply(final Fst fst) {
    Semiring semiring = fst.getSemiring();
    ArrayList<State> finalStates = new ArrayList<State>();

    int numStates = fst.getNumStates();
    for (int i = 0; i < numStates; i++) {
      State s = fst.getState(i);
      if (s.getFinalWeight() != semiring.zero()) {
        finalStates.add(s);
      }
    }

    // Add a new single final
    State newFinal = new State(semiring.one());
    fst.addState(newFinal);
    for (State s : finalStates) {
      // add epsilon transition from the old final to the new one
      s.addArc(new Arc(0, 0, s.getFinalWeight(), newFinal));
      // set old state's weight to zero
      s.setFinalWeight(semiring.zero());
    }
  }

  /**
   * Undo of the extend operation.
   * @param fst fst to work with
   */
  public static void undo(final Fst fst) {
    State f = null;
    int numStates = fst.getNumStates();
    for (int i = 0; i < numStates; i++) {
      State s = fst.getState(i);
      if (s.getFinalWeight() != fst.getSemiring().zero()) {
        f = s;
        break;
      }
    }

    if (f == null) {
      System.err.println("Final state not found.");
      return;
    }
    for (int i = 0; i < numStates; i++) {
      State s = fst.getState(i);
      for (int j = 0; j < s.getNumArcs(); j++) {
        Arc a = s.getArc(j);
        if (a.getIlabel() == 0 && a.getOlabel() == 0
            && a.getNextState().getId() == f.getId()) {
          s.setFinalWeight(a.getWeight());
        }
      }
    }
    fst.deleteState(f);
  }

}
