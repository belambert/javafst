package com.javafst.semiring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.testng.annotations.Test;

public class LogSemiringTest {

  @Test
  public void testPlus() {
    Semiring sr = new LogSemiring();
    assertEquals(-0.3132616877555847, sr.plus(0.0f, 1.0f), 0.00001);
    assertEquals(1.9514126777648926, sr.plus(2.0f, 5.0f), 0.00001);
    assertEquals(-5.006715297698975, sr.plus(-5.0f, 0.0f), 0.00001);
    assertEquals(Float.NEGATIVE_INFINITY, sr.plus(Float.NaN, 0.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.plus(0.0f, Float.NaN), 0.0);
  }

  @Test
  public void testTimes() {
    Semiring sr = new LogSemiring();
    assertEquals(1.0, sr.times(0.0f, 1.0f), 0.0);
    assertEquals(7.0, sr.times(2.0f, 5.0f), 0.0);
    assertEquals(-5.0, sr.times(-5.0f, 0.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.times(Float.NaN, 0.0f), 0.0);
    assertEquals(Float.NEGATIVE_INFINITY, sr.times(0.0f, Float.NaN), 0.0);
  }

  @Test
  public void testAdditiveIdentity() {
    Semiring sr = new LogSemiring();
    assertEquals(0.0, sr.plus(0.0f, sr.zero()), 0.0);
    assertEquals(5.0, sr.plus(sr.zero(), 5.0f), 0.0);
    assertEquals(-5.0, sr.plus(-5.0f, sr.zero()), 0.0);
  }

  @Test
  public void testMultiplicativeIdentity() {
    Semiring sr = new LogSemiring();
    assertEquals(0.0, sr.times(0.0f, sr.one()), 0.0);
    assertEquals(5.0, sr.times(sr.one(), 5.0f), 0.0);
    assertEquals(-5.0, sr.times(-5.0f, sr.one()), 0.0);
  }

  @Test
  public void testMembership() {
    Semiring sr = new LogSemiring();
    assertTrue(sr.isMember(0.0f));
    assertTrue(sr.isMember(1000.0f));
    assertTrue(sr.isMember(-2000.0f));
    assertTrue(sr.isMember(Float.POSITIVE_INFINITY));
    assertFalse(sr.isMember(Float.NEGATIVE_INFINITY)); // Is this right?
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
