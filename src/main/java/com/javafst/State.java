package com.javafst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The fst's mutable state implementation.
 * 
 * <p>Holds its outgoing {@link com.javafst.Arc} objects in an ArrayList
 * allowing additions/deletions.
 */
public class State {

  // State's Id
  protected int id = -1;

  // Final weight
  private float fnlWeight;

  // Outgoing arcs
  private ArrayList<Arc> arcs = null;

  // initial number of arcs
  protected int initialNumArcs = -1;

  protected State() {
    arcs = new ArrayList<Arc>();
  }

  /**
   * Constructor specifying the state's final weight.
   * 
   * @param fnlWeight         Final weight.
   */
  public State(float fnlWeight) {
    this();
    this.fnlWeight = fnlWeight;
  }

  /**
   * Constructor specifying the initial capacity of the arc's ArrayList (this
   * is an optimization used in various operations).
   * 
   * @param initialNumArcs    Number of arcs.
   */
  public State(int initialNumArcs) {
    this.initialNumArcs = initialNumArcs;
    if (initialNumArcs > 0) {
      arcs = new ArrayList<Arc>(initialNumArcs);
    }
  }

  /**
   * Shorts the arc's ArrayList based on the provided Comparator.
   * @param cmp               Comparator.
   */
  public void arcSort(Comparator<Arc> cmp) {
    Collections.sort(arcs, cmp);
  }

  /**
   * Get the state's final Weight.
   * @return                  Final weight.
   */
  public float getFinalWeight() {
    return fnlWeight;
  }

  /**
   * Set the state's arcs ArrayList.
   * 
   * @param arcs              The arcs ArrayList to set.
   */
  public void setArcs(ArrayList<Arc> arcs) {
    this.arcs = arcs;
  }

  /**
   * Set the state's final weight.
   * 
   * @param fnlfloat          The final weight to set.
   */
  public void setFinalWeight(float fnlfloat) {
    this.fnlWeight = fnlfloat;
  }

  /**
   * Get the state's integer id.
   * @return                  The state id.
   */
  public int getId() {
    return id;
  }

  /**
   * Get the number of outgoing arcs.
   * @return                  Number of arcs.
   */
  public int getNumArcs() {
    return this.arcs.size();
  }

  /**
   * Add an outgoing arc to the state.
   * 
   * @param arc               The arc to add.
   */
  public void addArc(Arc arc) {
    this.arcs.add(arc);
  }

  /**
   * Get an arc based on it's index the arcs ArrayList.
   * 
   * @param index             The arc's index.
   * @return                  The arc.
   */
  public Arc getArc(int index) {
    return this.arcs.get(index);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    State other = (State) obj;
    if (id != other.id) {
      return false;
    }
    if (!(fnlWeight == other.fnlWeight)) {
      if (Float.floatToIntBits(fnlWeight) != Float
          .floatToIntBits(other.fnlWeight)) {
        return false;
      }
    }
    if (arcs == null) {
      if (other.arcs != null) {
        return false;
      }
    } else if (!arcs.equals(other.arcs)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(" + id + ", " + fnlWeight + ")");
    return sb.toString();
  }

  /**
   * Delete an arc based on its index.
   * 
   * @param index             The arc's index.
   * @return                  The deleted arc.
   */
  public Arc deleteArc(int index) {
    return this.arcs.remove(index);
  }

  @Override
  public int hashCode() {
    return id * 991;
  }

  /**
   * Set an arc at the specified position in the arcs' ArrayList.
   * 
   * @param index             The position to the arcs' array.
   * @param arc               The arc value to set.
   */
  public void setArc(int index, Arc arc) {
    arcs.set(index, arc);
  }

}
