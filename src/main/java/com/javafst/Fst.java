package com.javafst;

import com.javafst.semiring.Semiring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

/**
 * A mutable finite state transducer implementation.
 * 
 * <p>Holds an ArrayList of {@link com.javafst.State} objects allowing
 * additions/deletions.
 */
public class Fst {

  // This is private, but not immutable.
  // TODO - I'd like to be able to return an iterator or something for
  // users to loop over.
  private ArrayList<State> states = null;

  // start state
  protected State start;

  protected String[] isyms;

  protected String[] osyms;

  protected Semiring semiring;

  
  public Fst(final State start, final String[] isyms,
             final String[] osyms, final Semiring semiring) {
    states = new ArrayList<State>();
    this.start = start;
    this.isyms = isyms;
    this.osyms = osyms;
    this.semiring = semiring;
  }
  
  public Fst() {
    states = new ArrayList<State>();
  }

  /**
   * Constructor specifying the initial capacity of the states ArrayList (this
   * is an optimization used in various operations).
   * 
   * @param numStates     The initial capacity.
   */
  public Fst(final int numStates) {
    if (numStates > 0) {
      states = new ArrayList<State>(numStates);
    }
  }

  /**
   * Constructor specifying the fst's semiring.
   * 
   * @param semiring   The fst's semiring.
   */
  public Fst(final Semiring semiring) {
    this();
    this.semiring = semiring;
  }

  /**
   * Get the initial states.
   * @return The initial state.
   */
  public State getStart() {
    return start;
  }

  /**
   * Get the semiring.
   * @return      Used semiring.
   */
  public Semiring getSemiring() {
    return semiring;
  }

  /**
   * Set the Semiring.
   * 
   * @param semiring    The semiring to set.
   */
  public void setSemiring(final Semiring semiring) {
    this.semiring = semiring;
  }

  /**
   * Set the initial state.
   * 
   * @param start     The initial state.
   */
  public void setStart(final State start) {
    this.start = start;
  }

  /**
   * Get the number of states in the fst.
   * 
   * @return     The number of states.
   */
  public int getNumStates() {
    return this.states.size();
  }

  public State getState(final int index) {
    return states.get(index);
  }

  // Or could use a stream
  public List<State> states() {
    return Collections.unmodifiableList(states);
  }

  public Stream<State> finalStates() {
    return states.stream().filter(x -> x.getFinalWeight() != Float.POSITIVE_INFINITY 
                                       && x.getFinalWeight() != Float.NEGATIVE_INFINITY);
  }

  /**
   * Adds a state to the fst.
   * 
   * @param state     The state to be added.
   */
  public void addState(final State state) {
    this.states.add(state);
    state.id = states.size() - 1;
  }

  /**
   * Get the input symbols' array.
   * 
   * @return     Array of input symbols.
   */
  public String[] getIsyms() {
    return isyms;
  }

  /**
   * Set the input symbols.
   * 
   * @param isyms     The isyms to set.
   */
  public void setIsyms(final String[] isyms) {
    this.isyms = isyms;
  }

  /**
   * Get the output symbols' array.
   * 
   * @return    An array of output symbols.
   */
  public String[] getOsyms() {
    return osyms;
  }

  /**
   * Set the output symbols.
   * 
   * @param osyms     The osyms to set.
   */
  public void setOsyms(final String[] osyms) {
    this.osyms = osyms;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Fst other = (Fst) obj;
    if (!Arrays.equals(isyms, other.isyms)) {
      return false;
    }
    if (!Arrays.equals(osyms, other.osyms)) {
      return false;
    }
    if (start == null) {
      if (other.start != null) {
        return false;
      }
    } else if (!start.equals(other.start)) {
      return false;
    }
    if (states == null) {
      if (other.states != null) {
        return false;
      }
    } else if (!states.equals(other.states)) {
      return false;
    }
    if (semiring == null) {
      if (other.semiring != null) {
        return false;
      }
    } else if (!semiring.equals(other.semiring)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return 31 * (Arrays.hashCode(isyms)
        + 31 * (Arrays.hashCode(osyms)
            + 31 * ((start == null ? 0 : start.hashCode())
               + 31 * ((states == null ? 0 : states.hashCode())
                  +  31 * ((semiring == null ? 0 : semiring.hashCode()))))));
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("Fst(start=" + start + ", isyms=" + Arrays.toString(isyms) + ", osyms="
        + Arrays.toString(osyms) + ", semiring=" + semiring + ")\n");
    for (int i = 0; i < states.size(); i++) {
      final State state = states.get(i);
      sb.append("  " + state + "\n");
      for (int j = 0; j < state.getNumArcs(); j++) {
        sb.append("    " + state.getArc(j) + "\n");
      }
    }

    return sb.toString();
  }

  /**
   * Deletes a state.
   * 
   * @param state    the state to delete
   */
  public void deleteState(final State state) {

    if (state == start) {
      System.err.println("Cannot delete start state.");
      return;
    }

    states.remove(state);

    for (State s1 : states) {
      final ArrayList<Arc> newArcs = new ArrayList<Arc>();
      for (int j = 0; j < s1.getNumArcs(); j++) {
        final Arc arc = s1.getArc(j);
        if (!arc.getNextState().equals(state)) {
          newArcs.add(arc);
        }
      }
      s1.setArcs(newArcs);
    }
  }

  /**
   * Remaps the states' ids.
   * 
   * <p>States' ids are renumbered starting from 0 up to @see
   * {@link com.javafst.Fst#getNumStates()}
   */
  public void remapStateIds() {
    for (int i = 0; i < states.size(); i++) {
      states.get(i).id = i;
    }

  }

  /**
   * Delete the given Set of states.
   */
  public void deleteStates(final HashSet<State> toDelete) {

    if (toDelete.contains(start)) {
      System.err.println("Cannot delete start state.");
      return;
    }

    final ArrayList<State> newStates = new ArrayList<State>();

    for (State s1 : states) {
      if (!toDelete.contains(s1)) {
        newStates.add(s1);
        final ArrayList<Arc> newArcs = new ArrayList<Arc>();
        for (int j = 0; j < s1.getNumArcs(); j++) {
          Arc arc = s1.getArc(j);
          if (!toDelete.contains(arc.getNextState())) {
            newArcs.add(arc);
          }
        }
        s1.setArcs(newArcs);
      }
    }
    states = newStates;

    remapStateIds();
  }
}
