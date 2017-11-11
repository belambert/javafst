package com.javafst.utils;

// TODO - can replace this with actual tuples...?

/**
 * Pairs two elements.
 * 
 * <p>Original code obtained by
 * http://stackoverflow.com/questions/521171/a-java-collection-of-value
 * -pairs-tuples
 * 
 */
public class Pair<L, R> {

  // The left element
  private L left;

  // The right element
  private R right;

  /*
   * Constructor specifying the left and right elements of the Pair.
   */
  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public void setLeft(L left) {
    this.left = left;
  }

  public void setRight(R right) {
    this.right = right;
  }

  public L getLeft() {
    return left;
  }

  public R getRight() {
    return right;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + left.hashCode();
    result = prime * result + right.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    @SuppressWarnings("unchecked")
    Pair<L,R> other = (Pair<L,R>) obj;
    if (!left.equals(other.left)) {
      return false;
    }
    if (!right.equals(other.right)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "(" + left + ", " + right + ")";
  }

}
