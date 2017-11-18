package com.javafst.semiring;

/**
 * Log semiring implementation.
 */
public class LogSemiring extends Semiring {

  private static final long serialVersionUID = 5212106775584311083L;

  // zero value
  private static float zero = Float.POSITIVE_INFINITY;

  // one value
  private static float one = 0.f;

  @Override
  public float plus(float w1, float w2) {
    if (!isMember(w1) || !isMember(w2)) {
      return Float.NEGATIVE_INFINITY;
    }
    if (w1 == Float.POSITIVE_INFINITY) {
      return w2;
    } else if (w2 == Float.POSITIVE_INFINITY) {
      return w1;
    }
    return (float) -Math.log(Math.exp(-w1) + Math.exp(-w2));
  }

  @Override
  public float times(float w1, float w2) {
    if (!isMember(w1) || !isMember(w2)) {
      return Float.NEGATIVE_INFINITY;
    }

    return w1 + w2;
  }

  @Override
  public float divide(float w1, float w2) {
    if (!isMember(w1) || !isMember(w2)) {
      return Float.NEGATIVE_INFINITY;
    }

    if (w2 == zero) {
      return Float.NEGATIVE_INFINITY;
    } else if (w1 == zero) {
      return zero;
    }

    return w1 - w2;
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
  public boolean isMember(float w) {
    return (!Float.isNaN(w)) // not a NaN
        && (w != Float.NEGATIVE_INFINITY); // and different from -inf
  }

  @Override
  public float reverse(float w1) {
    // TODO: ???
    System.out.println("Not Implemented");
    return Float.NEGATIVE_INFINITY;
  }

}
