package com.javafst;

import java.util.Arrays;
import java.util.Comparator;

/**
 * The fst's immutable state implementation.
 * 
 * <p>holds its outgoing {@link com.javafst.Arc} objects in a fixed size
 * array not allowing additions/deletions.
 */
public class ImmutableState extends State {

  // Outgoing arcs
  private Arc[] arcs = null;

  /**
   * Default protected constructor.
   * 
   * <p>An ImmutableState cannot be created directly. It needs to be deserialized
   * as part of an ImmutableFst.
   * 
   * @see com.javafst.ImmutableFst#loadModel(String)
   * 
   */
  protected ImmutableState() {
  }

  /**
   * Constructor specifying the capacity of the arcs array.
   * 
   * @param numArcs number of arcs
   */
  protected ImmutableState(int numArcs) {
    super(0);
    this.initialNumArcs = numArcs;
    arcs = new Arc[numArcs];
  }

  @Override
  public void arcSort(Comparator<Arc> cmp) {
    Arrays.sort(arcs, cmp);
  }

  @Override
  public void addArc(Arc arc) {
    throw new IllegalArgumentException(
        "You cannot modify an ImmutableState.");
  }

  /**
   * Set an arc at the specified position in the arcs' array.
   * 
   * @param index the position to the arcs' array
   * @param arc the arc value to set
   */
  @Override
  public void setArc(int index, Arc arc) {
    arcs[index] = arc;
  }

  @Override
  public Arc deleteArc(int index) {
    throw new IllegalArgumentException(
        "You cannot modify an ImmutableState.");
  }

  /**
   * Set the state's arcs array.
   * 
   * @param arcs the arcs array to set
   */
  public void setArcs(Arc[] arcs) {
    this.arcs = arcs;
  }

  @Override
  public int getNumArcs() {
    return initialNumArcs;
  }

  @Override
  public Arc getArc(int index) {
    return this.arcs[index];
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ImmutableState other = (ImmutableState) obj;
    if (!Arrays.equals(arcs, other.arcs)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    return true;
  }
}
