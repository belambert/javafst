package com.javafst;

import org.testng.annotations.Test;

import com.javafst.semiring.TropicalSemiring;

import junit.framework.TestCase;

public class ImmutableFstTest extends TestCase {

  @Test
  public void testToString() {
    final String output = "Fst(start=(-1, 0.0), isyms=[], osyms=[], semiring=class com.javafst.semiring.TropicalSemiring)\n";
    Fst fst = new ImmutableFst(new State(), new String[0], new String[0], new TropicalSemiring());
    assertEquals(output, fst.toString());
  }

  @Test
  public void testEquality() {
    Fst fst = new ImmutableFst(new State(), new String[0], new String[0], new TropicalSemiring());
    // Basic equality checks
    assertTrue(fst.equals(fst));
    //assertFalse(fst.equals(null));
    assertFalse(fst.equals(new Object()));

    // Check vocab mismatches
    assertFalse(new ImmutableFst(new State(), new String[]{"a","b"}, new String[]{"a","b","c"}, new TropicalSemiring())
        .equals(new ImmutableFst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));
    assertFalse(new ImmutableFst(new State(), new String[]{"a","b","c"}, new String[]{"a","b"}, new TropicalSemiring())
        .equals(new ImmutableFst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));

    // Check when various things are null
    assertFalse(new ImmutableFst(null, new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())
        .equals(new ImmutableFst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));
    assertFalse(new ImmutableFst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())
        .equals(new ImmutableFst(null, new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));
    assertFalse(new ImmutableFst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())
        .equals(new ImmutableFst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, null)));
    assertFalse(new ImmutableFst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, null)
        .equals(new ImmutableFst(new State(), new String[]{"a","b"}, new String[]{"a","b"}, new TropicalSemiring())));
  }

  @Test
  public void testHashCode() {
    Fst fst = new ImmutableFst(new State(), new String[0], new String[0], new TropicalSemiring());
    //assertEquals(-1840998495, fst.hashCode());
  }

}
