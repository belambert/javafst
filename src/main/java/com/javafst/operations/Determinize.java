package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.State;
import com.javafst.semiring.Semiring;
import com.javafst.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Determinize operation.
 * 
 * <p>See: M. Mohri, "Finite-State Transducers in Language and Speech Processing",
 * Computational Linguistics, 23:2, 1997.
 */
public class Determinize {

  private static Pair<State, Float> getPair(
      final ArrayList<Pair<State, Float>> queue, final State state, final Float zero) {
    Pair<State, Float> res = null;
    for (final Pair<State, Float> tmp : queue) {
      if (state.getId() == tmp.getLeft().getId()) {
        res = tmp;
        break;
      }
    }
    if (res == null) {
      res = new Pair<State, Float>(state, zero);
      queue.add(res);
    }
    return res;
  }

  private static ArrayList<Integer> getUniqueLabels(final Fst fst,
                                                    final ArrayList<Pair<State, Float>> pa) {
    final ArrayList<Integer> res = new ArrayList<Integer>();
    for (Pair<State, Float> p : pa) {
      final State state = p.getLeft();
      for (final Arc arc : state.arcs()) {
        if (!res.contains(arc.getIlabel())) {
          res.add(arc.getIlabel());
        }
      }
    }
    return res;
  }

  private static State getStateLabel(final ArrayList<Pair<State, Float>> pa,
      final HashMap<String, State> stateMapper) {
    final StringBuilder sb = new StringBuilder();
    for (final Pair<State, Float> p : pa) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append("(" + p.getLeft() + "," + p.getRight() + ")");
    }
    return stateMapper.get(sb.toString());
  }

  /**
   * Determinizes an fst. The result will be an equivalent fst that has the
   * property that no state has two transitions with the same input label. For
   * this algorithm, epsilon transitions are treated as regular symbols.
   * 
   * @param fst the fst to determinize
   * @return the determinized fst
   */
  public static Fst get(final Fst fst) {
    if (fst.getSemiring() == null) {
      // semiring not provided
      return null;
    }
    // initialize the queue and new fst
    final Semiring semiring = fst.getSemiring();
    final Fst res = new Fst(semiring);
    res.setIsyms(fst.getIsyms());
    res.setOsyms(fst.getOsyms());

    // stores the queue (item in index 0 is next)
    final Queue<ArrayList<Pair<State, Float>>> queue = 
        new LinkedList<ArrayList<Pair<State, Float>>>();

    final HashMap<String, State> stateMapper = new HashMap<String, State>();

    final State s = new State(semiring.zero());
    final String stateString = "(" + fst.getStart() + "," + semiring.one() + ")";
    queue.add(new ArrayList<Pair<State, Float>>());
    queue.peek().add(new Pair<State, Float>(fst.getStart(), semiring.one()));
    res.addState(s);
    stateMapper.put(stateString, s);
    res.setStart(s);

    while (!queue.isEmpty()) {
      final ArrayList<Pair<State, Float>> p = queue.remove();
      final State pnew = getStateLabel(p, stateMapper);
      final ArrayList<Integer> labels = getUniqueLabels(fst, p);
      for (final int label : labels) {
        Float wnew = semiring.zero();
        // calc w'
        for (final Pair<State, Float> ps : p) {
          final State old = ps.getLeft();
          final Float u = ps.getRight();
          for (final Arc arc : old.arcs()) {
            if (label == arc.getIlabel()) {
              wnew = semiring.plus(wnew,
                  semiring.times(u, arc.getWeight()));
            }
          }
        }

        // calculate new states
        // keep residual weights to variable forQueue
        final ArrayList<Pair<State, Float>> forQueue = new ArrayList<Pair<State, Float>>();
        for (final Pair<State, Float> ps : p) {
          final State old = ps.getLeft();
          final Float u = ps.getRight();
          final Float wnewRevert = semiring.divide(semiring.one(), wnew);
          for (final Arc arc : old.arcs()) {
            if (label == arc.getIlabel()) {
              final State oldstate = arc.getNextState();
              final Pair<State, Float> pair = getPair(forQueue,
                  oldstate, semiring.zero());
              pair.setRight(semiring.plus(
                  pair.getRight(),
                  semiring.times(wnewRevert,
                      semiring.times(u, arc.getWeight()))));
            }
          }
        }

        // build new state's id and new elements for queue
        String qnewid = "";
        for (final Pair<State, Float> ps : forQueue) {
          final State old = ps.getLeft();
          final Float unew = ps.getRight();
          if (!qnewid.equals("")) {
            qnewid = qnewid + ",";
          }
          qnewid = qnewid + "(" + old + "," + unew + ")";
        }

        if (stateMapper.get(qnewid) == null) {
          State qnew = new State(semiring.zero());
          res.addState(qnew);
          stateMapper.put(qnewid, qnew);
          // update new state's weight
          Float fw = qnew.getFinalWeight();
          for (Pair<State, Float> ps : forQueue) {
            fw = semiring.plus(fw, semiring.times(ps.getLeft()
                .getFinalWeight(), ps.getRight()));
          }
          qnew.setFinalWeight(fw);
          queue.add(forQueue);
        }
        pnew.addArc(new Arc(label, label, wnew, stateMapper.get(qnewid)));
      }
    }
    return res;
  }
}
