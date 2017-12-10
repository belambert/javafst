package com.javafst.operations;

import com.javafst.Arc;

import java.util.Comparator;

/**
 * Comparator used in {@link com.javafst.operations.ArcSort} for sorting
 * based on input labels.
 */
public class ILabelCompare implements Comparator<Arc> {

  @Override
  public int compare(Arc o1, Arc o2) {
    if (o1 == null) {
      return 1;
    }
    if (o2 == null) {
      return -1;
    }

    return (o1.getIlabel() < o2.getIlabel()) ? -1 : ((o1.getIlabel() == o2
        .getIlabel()) ? 0 : 1);
  }

}
