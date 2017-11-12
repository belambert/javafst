package com.javafst;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class ArcTest extends TestCase {

  public ArcTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ArcTest.class);
  }

  public void testApp() {
    assertTrue( true );
  }
  
  public void testPrint() {
    //Arc arc = new Arc()
    final Arc arc = new Arc(0, 0, 1.0f, new State());
    assertEquals("(0, 0, 1.0, (-1, 0.0))", arc.toString());
  }
  
  public void testSetWeight() {
    final float newWeight = 5.0f;
    final Arc arc = new Arc(0, 0, 1.0f, new State());
    arc.setWeight(newWeight);
    assertEquals(newWeight, arc.getWeight());
  }
  
  public void testSetNextState() {
    final State nextState = new State();
    final Arc arc = new Arc(0, 0, 1.0f, new State());
    arc.setNextState(nextState);
    assertEquals(nextState, arc.getNextState());
  }
  
  public void testEmptyObjects() {
    final Arc arc = new Arc();
    assertEquals(0, arc.getIlabel());
    assertEquals(0, arc.getOlabel());
    assertEquals(0.0f, arc.getWeight());
    assertNull(arc.getNextState());
  }
  
  public void testEquals() {
    final Arc arc = new Arc(0, 0, 0.0f, new State());
    assertTrue(arc.equals(arc));    // object equality.
    assertFalse(arc.equals(null));  // equal to a null value
    assertFalse(arc.equals(new Object()));  // Other type of object
    assertFalse(arc.equals(new Arc(1, 0, 0.0f, new State()))); // diff input label
    assertFalse(arc.equals(new Arc(0, 1, 0.0f, new State()))); // diff output label
    assertFalse(arc.equals(new Arc(0, 0, 1.0f, new State()))); // diff weights

    // Various null values
    //assertFalse(arc.equals(new Arc(0, 0, 1.0f, null)));  // doesn't work
    assertFalse(new Arc(0, 0, 0.0f, null).equals(arc));
  }

  public void testHash() {
    final Arc arc = new Arc(0, 0, 0.0f, new State());
    assertEquals(-29791, arc.hashCode());
  }
  
}
