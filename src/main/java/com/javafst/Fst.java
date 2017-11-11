package com.javafst;

import com.javafst.semiring.Semiring;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A mutable finite state transducer implementation.
 * 
 * <p>Holds an ArrayList of {@link com.javafst.State} objects allowing
 * additions/deletions.
 */
public class Fst {

  private ArrayList<State> states = null;

  // start state
  protected State start;

  protected String[] isyms;

  protected String[] osyms;

  protected Semiring semiring;

  public Fst() {
    states = new ArrayList<State>();
  }

  /**
   * Constructor specifying the initial capacity of the states ArrayList (this
   * is an optimization used in various operations).
   * 
   * @param numStates     the initial capacity
   */
  public Fst(int numStates) {
    if (numStates > 0) {
      states = new ArrayList<State>(numStates);
    }
  }

  /**
   * Constructor specifying the fst's semiring.
   * 
   * @param semiring   the fst's semiring
   */
  public Fst(Semiring semiring) {
    this();
    this.semiring = semiring;
  }

  /**
   * Get the initial states.
   * @return the initial state
   */
  public State getStart() {
    return start;
  }

  /**
   * Get the semiring.
   * @return 
   *           used semiring
   */
  public Semiring getSemiring() {
    return semiring;
  }

  /**
   * Set the Semiring.
   * 
   * @param semiring
   *            the semiring to set
   */
  public void setSemiring(Semiring semiring) {
    this.semiring = semiring;
  }

  /**
   * Set the initial state.
   * 
   * @param start
   *            the initial state
   */
  public void setStart(State start) {
    this.start = start;
  }

  /**
   * Get the number of states in the fst.
   * 
   * @return number of states
   */
  public int getNumStates() {
    return this.states.size();
  }

  public State getState(int index) {
    return states.get(index);
  }

  /**
   * Adds a state to the fst.
   * 
   * @param state
   *            the state to be added
   */
  public void addState(State state) {
    this.states.add(state);
    state.id = states.size() - 1;
  }

  /**
   * Get the input symbols' array.
   * 
   * @return array of input symbols
   */
  public String[] getIsyms() {
    return isyms;
  }

  /**
   * Set the input symbols.
   * 
   * @param isyms
   *            the isyms to set
   */
  public void setIsyms(String[] isyms) {
    this.isyms = isyms;
  }

  /**
   * Get the output symbols' array.
   * 
   * @return array fo output symbols
   */
  public String[] getOsyms() {
    return osyms;
  }

  /**
   * Set the output symbols.
   * 
   * @param osyms
   *            the osyms to set
   */
  public void setOsyms(String[] osyms) {
    this.osyms = osyms;
  }

  /**
   * Serializes a symbol map to an ObjectOutputStream.
   * 
   * @param out    the ObjectOutputStream. It should be already be initialized by
   *               the caller.
   * @param map    the symbol map to serialize
   */
  private void writeStringMap(ObjectOutputStream out, String[] map)
      throws IOException {
    out.writeInt(map.length);
    for (int i = 0; i < map.length; i++) {
      out.writeObject(map[i]);
    }
  }

  /**
   * Serializes the current Fst instance to an ObjectOutputStream.
   * 
   * @param out  the ObjectOutputStream. It should be already be initialized by
   *              the caller.
   */
  private void writeFst(ObjectOutputStream out) throws IOException {
    writeStringMap(out, isyms);
    writeStringMap(out, osyms);
    out.writeInt(states.indexOf(start));

    out.writeObject(semiring);
    out.writeInt(states.size());

    HashMap<State, Integer> stateMap = new HashMap<State, Integer>(
        states.size(), 1.f);
    for (int i = 0; i < states.size(); i++) {
      State state = states.get(i);
      out.writeInt(state.getNumArcs());
      out.writeFloat(state.getFinalWeight());
      out.writeInt(state.getId());
      stateMap.put(state, i);
    }

    int numStates = states.size();
    for (int i = 0; i < numStates; i++) {
      State state = states.get(i);
      int numArcs = state.getNumArcs();
      for (int j = 0; j < numArcs; j++) {
        Arc arc = state.getArc(j);
        out.writeInt(arc.getIlabel());
        out.writeInt(arc.getOlabel());
        out.writeFloat(arc.getWeight());
        out.writeInt(stateMap.get(arc.getNextState()));
      }
    }
  }

  /**
   * Saves binary model to disk.
   * 
   * @param filename      the binary model filename
   * @throws IOException  if IO went wrong
   */
  public void saveModel(String filename) throws IOException {
    FileOutputStream fos = new FileOutputStream(filename);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    writeFst(oos);
    oos.flush();
    oos.close();
    bos.close();
    fos.close();
  }

  /**
   * Deserializes a symbol map from an ObjectInputStream.
   * 
   * @param in  the ObjectInputStream. It should be already be initialized by
   *            the caller.
   * @return the deserialized symbol map
   * @throws IOException             if IO went wrong
   * @throws ClassNotFoundException  if serialization went wrong
   */
  protected static String[] readStringMap(ObjectInputStream in)
      throws IOException, ClassNotFoundException {

    int mapSize = in.readInt();
    String[] map = new String[mapSize];
    for (int i = 0; i < mapSize; i++) {
      String sym = (String) in.readObject();
      map[i] = sym;
    }

    return map;
  }

  /**
   * Deserializes an Fst from an ObjectInputStream.
   * 
   * @param in  the ObjectInputStream. It should be already be initialized by
   *            the caller.
   * @return Created FST
   */
  private static Fst readFst(ObjectInputStream in) throws IOException,
  ClassNotFoundException {
    String[] is = readStringMap(in);
    String[] os = readStringMap(in);
    final int startid = in.readInt();
    Semiring semiring = (Semiring) in.readObject();
    int numStates = in.readInt();
    Fst res = new Fst(numStates);
    res.isyms = is;
    res.osyms = os;
    res.semiring = semiring;
    for (int i = 0; i < numStates; i++) {
      int numArcs = in.readInt();
      State state = new State(numArcs + 1);
      float weight = in.readFloat();
      if (weight == res.semiring.zero()) {
        weight = res.semiring.zero();
      } else if (weight == res.semiring.one()) {
        weight = res.semiring.one();
      }
      state.setFinalWeight(weight);
      state.id = in.readInt();
      res.states.add(state);
    }
    res.setStart(res.states.get(startid));

    numStates = res.getNumStates();
    for (int i = 0; i < numStates; i++) {
      State s1 = res.getState(i);
      for (int j = 0; j < s1.initialNumArcs - 1; j++) {
        Arc arc = new Arc();
        arc.setIlabel(in.readInt());
        arc.setOlabel(in.readInt());
        arc.setWeight(in.readFloat());
        arc.setNextState(res.states.get(in.readInt()));
        s1.addArc(arc);
      }
    }

    return res;
  }

  /**
   * Deserializes an Fst from disk.
   * 
   * @param filename   the binary model filename
   * @return deserialized FST
   * @throws IOException             io IO went wrong
   * @throws ClassNotFoundException  if serialization went wrong
   */
  public static Fst loadModel(String filename) throws IOException,
  ClassNotFoundException {
    final long starttime = Calendar.getInstance().getTimeInMillis();
    final Fst obj;

    FileInputStream fis = null;
    BufferedInputStream bis = null;
    ObjectInputStream ois = null;
    fis = new FileInputStream(filename);
    bis = new BufferedInputStream(fis);
    ois = new ObjectInputStream(bis);
    obj = readFst(ois);
    ois.close();
    bis.close();
    fis.close();

    System.err.println("Load Time: "
        + (Calendar.getInstance().getTimeInMillis() - starttime)
        / 1000.);
    return obj;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Fst(start=" + start + ", isyms=" + Arrays.toString(isyms) + ", osyms="
        + Arrays.toString(osyms) + ", semiring=" + semiring + ")\n");
    int numStates = states.size();
    for (int i = 0; i < numStates; i++) {
      State state = states.get(i);
      sb.append("  " + state + "\n");
      int numArcs = state.getNumArcs();
      for (int j = 0; j < numArcs; j++) {
        Arc arc = state.getArc(j);
        sb.append("    " + arc + "\n");
      }
    }

    return sb.toString();
  }

  /**
   * Deletes a state.
   * 
   * @param state    the state to delete
   */
  public void deleteState(State state) {

    if (state == start) {
      System.err.println("Cannot delete start state.");
      return;
    }

    states.remove(state);

    for (State s1 : states) {
      ArrayList<Arc> newArcs = new ArrayList<Arc>();
      for (int j = 0; j < s1.getNumArcs(); j++) {
        Arc arc = s1.getArc(j);
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
    int numStates = states.size();
    for (int i = 0; i < numStates; i++) {
      states.get(i).id = i;
    }

  }

  /**
   * Delete the given Set of states.
   */
  public void deleteStates(HashSet<State> toDelete) {

    if (toDelete.contains(start)) {
      System.err.println("Cannot delete start state.");
      return;
    }

    ArrayList<State> newStates = new ArrayList<State>();

    for (State s1 : states) {
      if (!toDelete.contains(s1)) {
        newStates.add(s1);
        ArrayList<Arc> newArcs = new ArrayList<Arc>();
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
