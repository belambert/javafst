package com.javafst.semiring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.testng.annotations.Test;


// TODO - these all also need more tests... like...
// division, 'reverse' (mostly not implemented?), equality, and naturalLess

public class TropicalSemiringTest {

  // TODO - create the Semiring in a setUp() function..

  @Test
  public void testPlus() {
    Semiring sr = new TropicalSemiring();
    assertEquals(0.0, sr.plus(0.0f, 1.0f), 0.0);
    assertEquals(2.0, sr.plus(2.0f, 5.0f), 0.0);
    assertEquals(-5.0, sr.plus(-5.0f, 0.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.plus(Float.NEGATIVE_INFINITY, 0.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.plus(0.0f, Float.NEGATIVE_INFINITY), 0.0);
  }

  @Test
  public void testTimes() {
    Semiring sr = new TropicalSemiring();
    assertEquals(1.0, sr.times(0.0f, 1.0f), 0.0);
    assertEquals(7.0, sr.times(2.0f, 5.0f), 0.0);
    assertEquals(-5.0, sr.times(-5.0f, 0.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.times(Float.NEGATIVE_INFINITY, 0.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.times(0.0f, Float.NEGATIVE_INFINITY), 0.0);
  }

  @Test
  public void testAdditiveIdentity() {
    Semiring sr = new TropicalSemiring();
    assertEquals(0.0, sr.plus(0.0f, sr.zero()), 0.0);
    assertEquals(5.0, sr.plus(sr.zero(), 5.0f), 0.0);
    assertEquals(-5.0, sr.plus(-5.0f, sr.zero()), 0.0);
  }

  @Test
  public void testMultiplicativeIdentity() {
    Semiring sr = new TropicalSemiring();
    assertEquals(0.0, sr.times(0.0f, sr.one()), 0.0);
    assertEquals(5.0, sr.times(sr.one(), 5.0f), 0.0);
    assertEquals(-5.0, sr.times(-5.0f, sr.one()), 0.0);
  }
  
  @Test
  public void testDivision() {
    Semiring sr = new TropicalSemiring();
    assertEquals(-1.0, sr.divide(0.0f, 1.0f), 0.0);
    assertEquals(-3.0, sr.divide(2.0f, 5.0f), 0.0);
    assertEquals(-5.0, sr.divide(-5.0f, 0.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.divide(-5.0f, sr.zero()), 0.0);
    assertEquals(sr.zero(), sr.divide(sr.zero(), 10.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.divide(Float.NEGATIVE_INFINITY, 0.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.divide(0.0f, Float.NEGATIVE_INFINITY), 0.0);
  }

  @Test
  public void testMembership() {
    Semiring sr = new TropicalSemiring();
    assertTrue(sr.isMember(0.0f));
    assertTrue(sr.isMember(1000.0f));
    assertTrue(sr.isMember(-2000.0f));
    assertTrue(sr.isMember(Float.POSITIVE_INFINITY));
    assertFalse(sr.isMember(Float.NEGATIVE_INFINITY));  // Not sure if this is right...
    assertTrue(sr.isMember(sr.one()));
    assertTrue(sr.isMember(sr.zero()));
    assertFalse(sr.isMember(Float.NaN));
    assertTrue(sr.isMember(Float.MAX_VALUE));
    assertTrue(sr.isMember(Float.MIN_VALUE));
    assertTrue(sr.isMember(Float.MIN_NORMAL));
    assertTrue(sr.isMember(-Float.MAX_VALUE));
    assertTrue(sr.isMember(-Float.MIN_VALUE));
    assertTrue(sr.isMember(-Float.MIN_NORMAL));
  }

}
