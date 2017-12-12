package com.javafst.semiring;

/**
 * Probability semiring implementation.
 */
public class ProbabilitySemiring extends Semiring {

  private static final long serialVersionUID = 5592668313009971909L;
  // zero value
  private static float zero = 0.f;

  // one value
  private static float one = 1.f;

  @Override
  public float plus(final float w1, final float w2) {
    if (!isMember(w1) || !isMember(w2)) {
      return Float.NEGATIVE_INFINITY;
    }

    return w1 + w2;
  }

  @Override
  public float times(final float w1, final float w2) {
    if (!isMember(w1) || !isMember(w2)) {
      return Float.NEGATIVE_INFINITY;
    }

    return w1 * w2;
  }

  @Override
  public float divide(final float w1, final float w2) {
    // TODO Is this right?
    return w1 / w2;
  }

  @Override
  public float zero() {
    return zero;
  }

  @Override
  public float one() {
    return one;
  }

  @Override
  public boolean isMember(final float w) {
    return !Float.isNaN(w) // not a NaN,
        && (w >= 0); // and positive
  }

  @Override
  public float reverse(final float w1) {
    // TODO: ???
    System.out.println("Not Implemented");
    return Float.NEGATIVE_INFINITY;
  }

}
