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

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.javafst.weight.Semiring#plus(com.javafst.weight.float,
   * com.javafst.weight.float)
   */
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

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.javafst.weight.Semiring#times(com.javafst.weight.float,
   * com.javafst.weight.float)
   */
  @Override
  public float times(float w1, float w2) {
    if (!isMember(w1) || !isMember(w2)) {
      return Float.NEGATIVE_INFINITY;
    }

    return w1 + w2;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.javafst.weight.Semiring#divide(com.javafst.weight.float
   * , com.javafst.weight.float)
   */
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

  /*
   * (non-Javadoc)
   * 
   * @see com.javafst.weight.Semiring#zero()
   */
  @Override
  public float zero() {
    return zero;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.javafst.weight.Semiring#one()
   */
  @Override
  public float one() {
    return one;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.javafst.weight.Semiring#isMember(com.javafst.weight
   * .float)
   */
  @Override
  public boolean isMember(float w) {
    return (!Float.isNaN(w)) // not a NaN
        && (w != Float.NEGATIVE_INFINITY); // and different from -inf
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.javafst.semiring.Semiring#reverse(float)
   */
  @Override
  public float reverse(float w1) {
    // TODO: ???
    System.out.println("Not Implemented");
    return Float.NEGATIVE_INFINITY;
  }

}
