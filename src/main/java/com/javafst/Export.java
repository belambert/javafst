package com.javafst;

import java.io.IOException;


/**
 * Provides a command line utility to convert a java binary fst model to
 * openfst's text format.
 */
public class Export {

  private Export() {
  }

  /**
   * Exports a java binary model to openfst text format  
   * Several files are exported as follows:
   * - basename.input.syms
   * - basename.output.syms
   * - basename.fst.txt
   * First argument is the java binary model filename, second
   * argument openfst's files basename
   *
   * @param args arguments
   * @throws IOException IO went wrong
   * @throws ClassNotFoundException loading failed due to failed serialization
   */
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    if (args.length < 2) {
      System.err.println("Input and output files not provided");
      System.err
      .println("You need to provide both the input serialized java fst model");
      System.err.println("and the output binary openfst model.");
      System.exit(1);
    }

    Fst fst = Fst.loadModel(args[0]);

    // Serialize the java fst model to disk
    System.out.println("Saving as openfst text model...");
    Convert.export(fst, args[1]);
  }

}
