package com.javafst;

import com.javafst.semiring.TropicalSemiring;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class FstTest extends TestCase {

   public FstTest( String testName ) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(FstTest.class);
  }
  
  public void testToString() {
    final String output = "Fst(start=(-1, 0.0), isyms=[], osyms=[], semiring=class com.javafst.semiring.TropicalSemiring)\n";
    Fst fst = new Fst(new State(), new String[0], new String[0], new TropicalSemiring());
    assertEquals(output, fst.toString());
  }

  public void testEquality() {
    Fst fst = new Fst(new State(), new String[0], new String[0], new TropicalSemiring());
    // Basic equality checks
    assertTrue(fst.equals(fst));
    assertFalse(fst.equals(null));
    assertFalse(fst.equals(new Object()));
    
    // Check vocab mismatches
    assertFalse(new Fst(new State(), new String[]{"a","b"}, new String[]{"a","b","c"}, new TropicalSemiring())
        .equals(new Fst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));
    assertFalse(new Fst(new State(), new String[]{"a","b","c"}, new String[]{"a","b"}, new TropicalSemiring())
        .equals(new Fst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));
    
    // Check when various things are null
    assertFalse(new Fst(null, new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())
        .equals(new Fst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));
    assertFalse(new Fst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())
        .equals(new Fst(null, new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));
    assertFalse(new Fst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())
        .equals(new Fst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, null)));
    assertFalse(new Fst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, null)
        .equals(new Fst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));
  }
  
  public void testHashCode() {
    Fst fst = new Fst(new State(), new String[0], new String[0], new TropicalSemiring());
    assertEquals(-158946312, fst.hashCode());
  }
  
}
