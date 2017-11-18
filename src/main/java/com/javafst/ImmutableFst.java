package com.javafst;

import java.util.Arrays;

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
  private ImmutableFst(int numStates) {
    super(0);
    this.numStates = numStates;
    this.states = new ImmutableState[numStates];
  }

  @Override
  public int getNumStates() {
    return this.numStates;
  }

  @Override
  public ImmutableState getState(int index) {
    return states[index];
  }

  @Override
  public void addState(State state) {
    throw new IllegalArgumentException("You cannot modify an ImmutableFst.");
  }

  @Override
  public void deleteState(State state) {
    throw new IllegalArgumentException("You cannot modify an ImmutableFst.");
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Fst(start=" + start + ", isyms=" + Arrays.toString(isyms) + ", osyms="
        + Arrays.toString(osyms) + ", semiring=" + semiring + ")\n");
    int numStates = states.length;
    for (int i = 0; i < numStates; i++) {
      State s = states[i];
      sb.append("  " + s + "\n");
      int numArcs = s.getNumArcs();
      for (int j = 0; j < numArcs; j++) {
        Arc a = s.getArc(j);
        sb.append("    " + a + "\n");
      }
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
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
