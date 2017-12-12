package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.State;
import com.javafst.semiring.Semiring;

import java.util.HashMap;

/**
 * Remove epsilon operation.
 * 
 */
public class RmEpsilon {

  /**
   * Put a new state in the epsilon closure.
   */
  private static void put(final State fromState, final State toState, final float weight,
      final HashMap<State, Float>[] cl) {
    HashMap<State, Float> tmp = cl[fromState.getId()];
    if (tmp == null) {
      tmp = new HashMap<State, Float>();
      cl[fromState.getId()] = tmp;
    }
    tmp.put(toState, weight);
  }

  /**
   * Add a state in the epsilon closure.
   */
  private static void add(final State fromState, final State toState, final float weight,
      final HashMap<State, Float>[] cl, final Semiring semiring) {
    final Float old = getPathWeight(fromState, toState, cl);
    if (old == null) {
      put(fromState, toState, weight, cl);
    } else {
      put(fromState, toState, semiring.plus(weight, old), cl);
    }
  }

  /**
   * Calculate the epsilon closure.
   */
  private static void calcClosure(final Fst fst, final State state,
      final HashMap<State, Float>[] cl, final Semiring semiring) {
    final State s = state;
    float pathWeight;
    for (final Arc a : s.arcs()) {
      if ((a.getIlabel() == 0) && (a.getOlabel() == 0)) {
        if (cl[a.getNextState().getId()] == null) {
          calcClosure(fst, a.getNextState(), cl, semiring);
        }
        if (cl[a.getNextState().getId()] != null) {
          for (State pathFinalState : cl[a.getNextState().getId()]
              .keySet()) {
            pathWeight = semiring.times(
                getPathWeight(a.getNextState(), pathFinalState,
                    cl), a.getWeight());
            add(state, pathFinalState, pathWeight, cl, semiring);
          }
        }
        add(state, a.getNextState(), a.getWeight(), cl, semiring);
      }
    }
  }

  /**
   * Get an epsilon path's cost in epsilon closure.
   */
  private static Float getPathWeight(final State in, final State out,
      final HashMap<State, Float>[] cl) {
    if (cl[in.getId()] != null) {
      return cl[in.getId()].get(out);
    }
    return null;
  }

  /**
   * Removes epsilon transitions from an fst.
   * 
   * <p>It return a new epsilon-free fst and does not modify the original fst
   * 
   * @param fst     The fst to remove epsilon transitions from.
   * @return        The epsilon-free fst.
   */
  public static Fst get(final Fst fst) {
    if (fst == null) {
      return null;
    }

    if (fst.getSemiring() == null) {
      return null;
    }

    final Semiring semiring = fst.getSemiring();

    final Fst res = new Fst(semiring);

    @SuppressWarnings("unchecked")
    final HashMap<State, Float>[] cl = new HashMap[fst.getNumStates()];
    final State[] oldToNewStateMap = new State[fst.getNumStates()];
    final State[] newToOldStateMap = new State[fst.getNumStates()];

    for (final State s : fst.states()) {
      // Add non-epsilon arcs
      final State newState = new State(s.getFinalWeight());
      res.addState(newState);
      oldToNewStateMap[s.getId()] = newState;
      newToOldStateMap[newState.getId()] = s;
      if (newState.getId() == fst.getStart().getId()) {
        res.setStart(newState);
      }
    }

    for (final State s : fst.states()) {
      // Add non-epsilon arcs
      final State newState = oldToNewStateMap[s.getId()];
      for (final Arc a : s.arcs()) {
        if ((a.getIlabel() != 0) || (a.getOlabel() != 0)) {
          newState.addArc(new Arc(a.getIlabel(), a.getOlabel(), a
              .getWeight(), oldToNewStateMap[a.getNextState()
                                             .getId()]));
        }
      }

      // Compute e-Closure
      if (cl[s.getId()] == null) {
        calcClosure(fst, s, cl, semiring);
      }
    }

    // augment fst with arcs generated from epsilon moves.
    for (int i = 0; i < res.getNumStates(); i++) {
      final State s = res.getState(i);
      final State oldState = newToOldStateMap[s.getId()];
      if (cl[oldState.getId()] != null) {
        for (State pathFinalState : cl[oldState.getId()].keySet()) {
          final State s1 = pathFinalState;
          if (s1.getFinalWeight() != semiring.zero()) {
            s.setFinalWeight(semiring.plus(s.getFinalWeight(),
                semiring.times(getPathWeight(oldState, s1, cl),
                    s1.getFinalWeight())));
          }
          for (final Arc a : s1.arcs()) {
            if ((a.getIlabel() != 0) || (a.getOlabel() != 0)) {
              final Arc newArc = new Arc(a.getIlabel(), a.getOlabel(),
                  semiring.times(a.getWeight(),
                      getPathWeight(oldState, s1, cl)),
                  oldToNewStateMap[a.getNextState().getId()]);
              s.addArc(newArc);
            }
          }
        }
      }
    }
    res.setIsyms(fst.getIsyms());
    res.setOsyms(fst.getOsyms());
    Connect.apply(res);
    return res;
  }
}
