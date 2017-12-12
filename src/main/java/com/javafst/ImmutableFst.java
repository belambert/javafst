package com.javafst;

import java.util.ArrayList;
import java.util.Arrays;

import com.javafst.semiring.Semiring;

// Probably don't need this as is anymore...?

/**
 * An immutable finite state transducer implementation.
 * 
 * <p>Holds a fixed size array of {@link com.javafst.ImmutableState} objects
 * not allowing additions/deletions
 */
public class ImmutableFst extends Fst {

  // fst states
  private ImmutableState[] states = null;

  // number of states
  private int numStates;

  /**
   * Default private constructor.
   * 
   * <p>An ImmutableFst cannot be created directly. It needs to be deserialized.
   * 
   * @see com.javafst.ImmutableFst#loadModel(String)
   */
  private ImmutableFst() { }

  /**
   * Private Constructor specifying the capacity of the states array
   * 
   * <p>An ImmutableFst cannot be created directly. It needs to be deserialized.
   * 
   * @see com.javafst.ImmutableFst#loadModel(String)
   * 
   * @param numStates     The number of fst's states.
   */
  private ImmutableFst(final int numStates) {
    super(0);
    this.numStates = numStates;
    this.states = new ImmutableState[numStates];
  }

  public ImmutableFst(final State start, final String[] isyms, final String[] osyms, final Semiring semiring) {
    //states = Arrays.toArray(new ArrayList<ImmutableState>());
    this.states = new ImmutableState[0];
    this.start = start;
    this.isyms = isyms;
    this.osyms = osyms;
    this.semiring = semiring;
  }

  @Override
  public int getNumStates() {
    return this.numStates;
  }

  @Override
  public ImmutableState getState(final int index) {
    return states[index];
  }

  @Override
  public void addState(final State state) {
    throw new IllegalArgumentException("You cannot modify an ImmutableFst.");
  }

  @Override
  public void deleteState(final State state) {
    throw new IllegalArgumentException("You cannot modify an ImmutableFst.");
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Fst(start=" + start + ", isyms=" + Arrays.toString(isyms) + ", osyms="
        + Arrays.toString(osyms) + ", semiring=" + semiring + ")\n");
    for (final State s : states) {
      sb.append("  " + s + "\n");
      for (final Arc a : s.arcs()) {
        sb.append("    " + a + "\n");
      }
    }
    return sb.toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ImmutableFst other = (ImmutableFst) obj;
    if (!Arrays.equals(states, other.states)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(states) + super.hashCode();
  }

}
