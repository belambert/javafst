package com.javafst.operations;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.javafst.Fst;
import com.javafst.State;
import com.javafst.semiring.Semiring;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Connect operation.
 * 
 * <p>A state q ∈ Q is said to be non-accessible (non-coaccessible) when there is
 * no path from I to q (respectively from q to F ). Non-accessible and non-coaccessible
 * states are called useless states. They can be removed using a connection (or
 * trimming) algorithm in linear time without affecting the weight T associates
 * to any pair. A transducer with no useless state is said to be trim.
 */
public class Connect {

  /**
   * Trims an Fst, removing states and arcs that are not on successful paths.
   * 
   * @param fst the fst to trim
   */
  public static void apply(final Fst fst) {
    final Semiring semiring = fst.getSemiring();
    if (semiring == null) {
      System.out.println("Fst has no semiring.");
      return;
    }
    final HashSet<State> accessible = new HashSet<State>();
    final HashSet<State> coaccessible = new HashSet<State>();
    getAccessibleStates(fst.getStart(), fst, accessible);
    getCoAccessibleStates(fst, coaccessible);
    // Delete all the non-accessible states
    final HashSet<State> toDelete = new HashSet<State>();
    for (final State state : fst.states()) {
      if (!(accessible.contains(state) && coaccessible.contains(state))) {
        toDelete.add(state);
      }
    }
    fst.deleteStates(toDelete);
  }

  private static HashSet<State> getAccessibleStates(final State state, final Fst fst,
                                                    final HashSet<State> accessible) {
    if (accessible.contains(state)) {
      return accessible;
    }
    accessible.add(state);
    state.arcs().stream().forEach(x -> getAccessibleStates(x.getNextState(), fst, accessible));
    return accessible;
  }
  
  private static Set<State> getCoAccessibleStates(final Fst fst,
                                                  final HashSet<State> coAccessible) {
    Multimap<State, State> map = HashMultimap.create();
    HashSet<State> exploredStates = new HashSet<State>();
    getReverseArcs(fst.getStart(), fst, map, exploredStates);
    fst.finalStates().forEach(x -> getCoAccessibleRecursive(x, map, coAccessible));
    return coAccessible;
  }
  
  /** Build a table of reverse pointing arcs. */
  private static void getReverseArcs(final State state, final Fst fst,
                                     final Multimap<State, State> map,
                                     final HashSet<State> exploredStates) {
    if (exploredStates.contains(state)) {
      return;
    }
    state.arcs().stream().forEach(x -> map.put(x.getNextState(), state));
    exploredStates.add(state);
    state.arcs().stream().forEach(x -> getReverseArcs(x.getNextState(), fst, map, exploredStates));
  }
  
  private static void getCoAccessibleRecursive(final State state,
                                               final Multimap<State, State> map,
                                               final HashSet<State> coAccessible) {
    coAccessible.add(state);
    Collection<State> previousStates = map.get(state);
    previousStates.stream().filter(s -> ! coAccessible.contains(s))
                           .forEach(s -> getCoAccessibleRecursive(s, map, coAccessible));
  }
}
