package com.javafst;

import com.javafst.semiring.Semiring;
import com.javafst.utils.Utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * OpenFST text format I/O.
 */
public class Convert {

  private Convert() { }

  /**
   * Exports an fst to the OpenFST text format Several files are created as
   * follows: - basename.input.syms - basename.output.syms - basename.fst.txt.
   * 
   * <p>See <a href="http://www.openfst.org/twiki/bin/view/FST/FstQuickTour">OpenFst
   * Quick Tour</a>
   * 
   * @param fst               The fst to export.
   * @param basename          The files' base name.
   */
  public static void export(final Fst fst, final String basename) throws IOException {
    exportSymbols(fst.getIsyms(), basename + ".input.syms");
    exportSymbols(fst.getOsyms(), basename + ".output.syms");
    exportFst(fst, basename + ".fst.txt");
  }

  /**
   * Exports an fst to the OpenFST text format.
   * 
   * @param fst               The fst to export.
   * @param filename          The openfst's fst.txt filename.
   */
  private static void exportFst(final Fst fst, final String filename) throws IOException {
    FileWriter file;

    file = new FileWriter(filename);
    final PrintWriter out = new PrintWriter(file);

    // print start first
    final State start = fst.getStart();
    out.println(start.getId() + "\t" + start.getFinalWeight());

    // print all states
    int numStates = fst.getNumStates();
    for (int i = 0; i < numStates; i++) {
      final State s = fst.getState(i);
      if (s.getId() != fst.getStart().getId()) {
        out.println(s.getId() + "\t" + s.getFinalWeight());
      }
    }

    final String[] isyms = fst.getIsyms();
    final String[] osyms = fst.getOsyms();
    numStates = fst.getNumStates();
    for (int i = 0; i < numStates; i++) {
      final State s = fst.getState(i);
      final int numArcs = s.getNumArcs();
      for (int j = 0; j < numArcs; j++) {
        final Arc arc = s.getArc(j);
        final String isym = (isyms != null) ? isyms[arc.getIlabel()]
            : Integer.toString(arc.getIlabel());
        final String osym = (osyms != null) ? osyms[arc.getOlabel()]
            : Integer.toString(arc.getOlabel());

        out.println(s.getId() + "\t" + arc.getNextState().getId()
            + "\t" + isym + "\t" + osym + "\t" + arc.getWeight());
      }
    }
    out.close();
  }

  /**
   * Exports a symbols' map to the OpenFST text format.
   * 
   * @param syms              The symbols' map.
   * @param filename          The the openfst's symbols filename.
   */
  private static void exportSymbols(final String[] syms, final String filename)
      throws IOException {
    if (syms == null) {
      return;
    }

    final FileWriter file = new FileWriter(filename);
    final PrintWriter out = new PrintWriter(file);

    for (int i = 0; i < syms.length; i++) {
      final String key = syms[i];
      out.println(key + "\t" + i);
    }

    out.close();

  }

  /**
   * Imports an OpenFST's symbols file.
   * 
   * @param filename                   The symbols' filename.
   * @return HashMap                   Containing the imported string-to-id mapping.
   * @throws NumberFormatException     Import failed due to input data format.
   */
  private static HashMap<String, Integer> importSymbols(final String filename)
      throws NumberFormatException, IOException {

    final File symfile = new File(filename);
    if (!(symfile.exists() && symfile.isFile())) {
      return null;
    }

    final FileInputStream fis = new FileInputStream(filename); 
    final DataInputStream dis = new DataInputStream(fis);
    final BufferedReader br = new BufferedReader(new InputStreamReader(dis));
    final HashMap<String, Integer> syms = new HashMap<String, Integer>();
    String strLine;

    while ((strLine = br.readLine()) != null) {
      final String[] tokens = strLine.split("\\t");
      final String sym = tokens[0];
      final Integer index = Integer.parseInt(tokens[1]);
      syms.put(sym, index);

    }
    br.close();

    return syms;
  }

  /**
   * Imports an OpenFST text format Several files are imported as follows: -
   * basename.input.syms - basename.output.syms - basename.fst.txt.
   * 
   * @param basename                  The files' base name.
   * @param semiring                  The fst's semiring.
   * @return                          Imported FST.
   * @throws IOException              IO went wrong.
   * @throws NumberFormatException    Load failed due to data format issues.
   */
  public static Fst importFst(final String basename, final Semiring semiring)
      throws NumberFormatException, IOException {
    final Fst fst = new Fst(semiring);

    HashMap<String, Integer> isyms = importSymbols(basename + ".input.syms");
    if (isyms == null) {
      isyms = new HashMap<String, Integer>();
      isyms.put("<eps>", 0);
    }

    HashMap<String, Integer> osyms = importSymbols(basename
        + ".output.syms");
    if (osyms == null) {
      osyms = new HashMap<String, Integer>();
      osyms.put("<eps>", 0);
    }

    final HashMap<String, Integer> ssyms = importSymbols(basename
        + ".states.syms");

    // Parse input
    final FileInputStream fis = new FileInputStream(basename + ".fst.txt");

    final DataInputStream dis = new DataInputStream(fis);
    final BufferedReader br = new BufferedReader(new InputStreamReader(dis, "UTF-8"));
    boolean firstLine = true;
    String strLine;
    final HashMap<Integer, State> stateMap = new HashMap<Integer, State>();

    while ((strLine = br.readLine()) != null) {
      final String[] tokens = strLine.split("\\t");
      Integer inputStateId;
      if (ssyms == null) {
        inputStateId = Integer.parseInt(tokens[0]);
      } else {
        inputStateId = ssyms.get(tokens[0]);
      }
      State inputState = stateMap.get(inputStateId);
      if (inputState == null) {
        inputState = new State(semiring.zero());
        fst.addState(inputState);
        stateMap.put(inputStateId, inputState);
      }

      if (firstLine) {
        firstLine = false;
        fst.setStart(inputState);
      }

      if (tokens.length > 2) {
        Integer nextStateId;
        if (ssyms == null) {
          nextStateId = Integer.parseInt(tokens[1]);
        } else {
          nextStateId = ssyms.get(tokens[1]);
        }

        State nextState = stateMap.get(nextStateId);
        if (nextState == null) {
          nextState = new State(semiring.zero());
          fst.addState(nextState);
          stateMap.put(nextStateId, nextState);
        }
        // Adding arc
        if (isyms.get(tokens[2]) == null) {
          isyms.put(tokens[2], isyms.size());
        }
        final int iLabel = isyms.get(tokens[2]);
        if (osyms.get(tokens[3]) == null) {
          osyms.put(tokens[3], osyms.size());
        }
        final int oLabel = osyms.get(tokens[3]);

        float arcWeight;
        if (tokens.length > 4) {
          arcWeight = Float.parseFloat(tokens[4]);
        } else {
          arcWeight = 0;
        }
        final Arc arc = new Arc(iLabel, oLabel, arcWeight, nextState);
        inputState.addArc(arc);
      } else {
        if (tokens.length > 1) {
          final float finalWeight = Float.parseFloat(tokens[1]);
          inputState.setFinalWeight(finalWeight);
        } else {
          inputState.setFinalWeight(0.0f);
        }
      }
    }
    dis.close();

    fst.setIsyms(Utils.toStringArray(isyms));
    fst.setOsyms(Utils.toStringArray(osyms));

    return fst;
  }
}
