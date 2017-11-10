package com.javafst;

import java.io.IOException;

import com.javafst.semiring.TropicalSemiring;

/**
 * Provides a command line utility to convert an Fst in openfst's text format to
 * java binary fst model
 */
public class Import {

  private Import() {
  }

  /**
   * Imports an openfst text format and serializes it as java binary model
   * Several files are imported as follows: - basename.input.syms -
   * basename.output.syms - basename.fst.txt
   * First argument is the java binary model filename, second
   * argument openfst's files basename
   *
   * @param args arguments
   * @throws IOException IO went wrong
   * @throws NumberFormatException data has wrong input format
   */
  public static void main(String[] args) throws NumberFormatException, IOException {
    if (args.length < 2) {
      System.err.println("Input and output files not provided");
      System.err
      .println("You need to provide both the input binary openfst model");
      System.err.println("and the output serialized java fst model.");
      System.exit(1);
    }

    Fst fst = Convert.importFst(args[0], new TropicalSemiring());

    // Serialize the java fst model to disk
    System.out.println("Saving as binary java fst model...");
    try {
      fst.saveModel(args[1]);

    } catch (IOException e) {
      System.err.println("Cannot write to file " + args[1]);
      System.exit(1);
    }
  }
}
