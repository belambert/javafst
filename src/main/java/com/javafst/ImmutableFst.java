package com.javafst;

import com.javafst.semiring.Semiring;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;


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
  private ImmutableFst() {

  }

  /**
   * Private Constructor specifying the capacity of the states array
   * 
   * <p>An ImmutableFst cannot be created directly. It needs to be deserialized.
   * 
   * @see com.javafst.ImmutableFst#loadModel(String)
   * 
   * @param numStates
   *            the number of fst's states
   */
  private ImmutableFst(int numStates) {
    super(0);
    this.numStates = numStates;
    this.states = new ImmutableState[numStates];
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.javafst.Fst#getNumStates()
   */
  @Override
  public int getNumStates() {
    return this.numStates;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.javafst.Fst#getState(int)
   */
  @Override
  public ImmutableState getState(int index) {
    return states[index];
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.javafst.Fst#addState(com.javafst.State)
   */
  @Override
  public void addState(State state) {
    throw new IllegalArgumentException("You cannot modify an ImmutableFst.");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.javafst.Fst#saveModel(java.lang.String)
   */
  @Override
  public void saveModel(String filename) throws IOException {
    throw new IllegalArgumentException(
        "You cannot serialize an ImmutableFst.");
  }

  /**
   * Deserializes an ImmutableFst from an ObjectInputStream
   * 
   * @param in  the ObjectInputStream. It should be already be initialized by
   *            the caller.
   * @return Created FST
   */
  private static ImmutableFst readImmutableFst(ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    final String[] is = readStringMap(in);
    final String[] os = readStringMap(in);
    final int startid = in.readInt();
    Semiring semiring = (Semiring) in.readObject();
    int numStates = in.readInt();
    ImmutableFst res = new ImmutableFst(numStates);
    res.isyms = is;
    res.osyms = os;
    res.semiring = semiring;
    for (int i = 0; i < numStates; i++) {
      int numArcs = in.readInt();
      ImmutableState s = new ImmutableState(numArcs + 1);
      float f = in.readFloat();
      if (f == res.semiring.zero()) {
        f = res.semiring.zero();
      } else if (f == res.semiring.one()) {
        f = res.semiring.one();
      }
      s.setFinalWeight(f);
      s.id = in.readInt();
      res.states[s.getId()] = s;
    }
    res.setStart(res.states[startid]);

    numStates = res.states.length;
    for (int i = 0; i < numStates; i++) {
      ImmutableState s1 = res.states[i];
      for (int j = 0; j < s1.initialNumArcs - 1; j++) {
        Arc a = new Arc();
        a.setIlabel(in.readInt());
        a.setOlabel(in.readInt());
        a.setWeight(in.readFloat());
        a.setNextState(res.states[in.readInt()]);
        s1.setArc(j, a);
      }
    }

    return res;
  }

  /**
   * Deserializes an ImmutableFst from an InputStream
   * 
   * @param inputStream
   *            the InputStream. It should be already be initialized by the
   *            caller.
   * @return Immutable FST structure
   * @throws IOException IO went wrong
   * @throws ClassNotFoundException serialization had issues
   */
  public static ImmutableFst loadModel(InputStream inputStream)
      throws IOException, ClassNotFoundException {
    ImmutableFst obj;

    BufferedInputStream bis = null;
    ObjectInputStream ois = null;
    bis = new BufferedInputStream(inputStream);
    ois = new ObjectInputStream(bis);
    obj = readImmutableFst(ois);
    ois.close();
    bis.close();
    inputStream.close();

    return obj;
  }

  /**
   * Deserializes an ImmutableFst from disk.
   * 
   * @param filename    the binary model filename
   * @return loaded FST
   */
  public static ImmutableFst loadModel(String filename) {
    ImmutableFst obj;

    try {
      FileInputStream fis = null;
      BufferedInputStream bis = null;
      ObjectInputStream ois = null;
      fis = new FileInputStream(filename);
      bis = new BufferedInputStream(fis);
      ois = new ObjectInputStream(bis);
      obj = readImmutableFst(ois);
      ois.close();
      bis.close();
      fis.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }

    return obj;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.javafst.Fst#deleteState(com.javafst.State)
   */
  @Override
  public void deleteState(State state) {
    throw new IllegalArgumentException("You cannot modify an ImmutableFst.");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.javafst.Fst#toString()
   */
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
