package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.State;
import com.javafst.semiring.Semiring;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Connect operation.
 */
public class Connect {
  /**
   * Calculates the co-accessible states of an fst.
   */
  private static void calcCoAccessible(final Fst fst, final State state,
      final ArrayList<ArrayList<State>> paths, final HashSet<State> coaccessible) {
    // hold the co-accessible added in this loop
    final ArrayList<State> newCoAccessibles = new ArrayList<State>();
    for (ArrayList<State> path : paths) {
      final int index = path.lastIndexOf(state);
      if (index != -1) {
        if (state.getFinalWeight() != fst.getSemiring().zero()
            || coaccessible.contains(state)) {
          for (int j = index; j > -1; j--) {
            if (!coaccessible.contains(path.get(j))) {
              newCoAccessibles.add(path.get(j));
              coaccessible.add(path.get(j));
            }
          }
        }
      }
    }

    // run again for the new co-accessibles
    for (final State s : newCoAccessibles) {
      calcCoAccessible(fst, s, paths, coaccessible);
    }
  }

  /**
   * Copies a path.
   */
  private static void duplicatePath(final int lastPathIndex, final State fromState,
      final State toState, final ArrayList<ArrayList<State>> paths) {
    final ArrayList<State> lastPath = paths.get(lastPathIndex);
    // copy the last path to a new one, from start to current state
    final int fromIndex = lastPath.indexOf(fromState);
    int toIndex = lastPath.indexOf(toState);
    if (toIndex == -1) {
      toIndex = lastPath.size() - 1;
    }
    final ArrayList<State> newPath = new ArrayList<State>(lastPath.subList(
        fromIndex, toIndex));
    paths.add(newPath);
  }

  /**
   * The depth first search recursion.
   */
  private static State depthFirstSearchNext(final Fst fst, final State start,
      final ArrayList<ArrayList<State>> paths, final ArrayList<Arc>[] exploredArcs,
      final HashSet<State> accessible) {
    int lastPathIndex = paths.size() - 1;

    final ArrayList<Arc> currentExploredArcs = exploredArcs[start.getId()];
    paths.get(lastPathIndex).add(start);
    if (start.getNumArcs() != 0) {
      int arcCount = 0;
      for (final Arc arc : start.arcs()) {
        if ((currentExploredArcs == null)
            || !currentExploredArcs.contains(arc)) {
          lastPathIndex = paths.size() - 1;
          if (arcCount++ > 0) {
            duplicatePath(lastPathIndex, fst.getStart(), start,
                paths);
            lastPathIndex = paths.size() - 1;
            paths.get(lastPathIndex).add(start);
          }
          final State next = arc.getNextState();
          addExploredArc(start.getId(), arc, exploredArcs);
          // detect self loops
          if (next.getId() != start.getId()) {
            depthFirstSearchNext(fst, next, paths, exploredArcs, accessible);
          }
        }
      }
    }
    lastPathIndex = paths.size() - 1;
    accessible.add(start);
    return start;
  }

  /**
   * Adds an arc top the explored arcs list.
   */
  private static void addExploredArc(final int stateId, final Arc arc,
      final ArrayList<Arc>[] exploredArcs) {
    if (exploredArcs[stateId] == null) {
      exploredArcs[stateId] = new ArrayList<Arc>();
    }
    exploredArcs[stateId].add(arc);
  }

  /**
   * Initialization of a depth first search recursion.
   */
  private static void depthFirstSearch(final Fst fst, final HashSet<State> accessible,
      final ArrayList<ArrayList<State>> paths, final ArrayList<Arc>[] exploredArcs,
      final HashSet<State> coaccessible) {
    final State currentState = fst.getStart();
    State nextState = currentState;
    do {
      if (!accessible.contains(currentState)) {
        nextState = depthFirstSearchNext(fst, currentState, paths, exploredArcs,
            accessible);
      }
    } while (currentState.getId() != nextState.getId());
    for (final State state : fst.states()) {
      if (state.getFinalWeight() != fst.getSemiring().zero()) {
        calcCoAccessible(fst, state, paths, coaccessible);
      }
    }
  }

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
    @SuppressWarnings("unchecked")
    final ArrayList<Arc>[] exploredArcs = new ArrayList[fst.getNumStates()];
    final ArrayList<ArrayList<State>> paths = new ArrayList<ArrayList<State>>();
    paths.add(new ArrayList<State>());
    depthFirstSearch(fst, accessible, paths, exploredArcs, coaccessible);

    final HashSet<State> toDelete = new HashSet<State>();
    for (final State state : fst.states()) {
      if (!(accessible.contains(state) || coaccessible.contains(state))) {
        toDelete.add(state);
      }
    }
    fst.deleteStates(toDelete);
  }
}
