package com.javafst.utils;

import java.util.HashMap;

/**
 * One general use utility function needed by the fst framework.
 */
public class Utils {

  /**
   * Convert a HashMap to string array.
   * 
   * @param syms             The input HashMap.
   * @return                 The strings array.
   */
  public static String[] toStringArray(final HashMap<String, Integer> syms) {
    return syms.keySet().toArray(new String[syms.size()]);
  }
}
