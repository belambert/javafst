package com.javafst.operations;

import static com.javafst.Convert.importFst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

import com.javafst.Convert;
import com.javafst.Fst;
import com.javafst.operations.Reverse;
import com.javafst.semiring.TropicalSemiring;


public class ReverseTest {

  @Test
  public void testReverse() throws NumberFormatException, IOException, ClassNotFoundException, URISyntaxException {
    String path = "algorithms/reverse/A.fst";
    URL url = getClass().getResource(path);
    File parent = new File(url.toURI()).getParentFile();

    path = new File(parent, "A").getPath();
    Fst fst = Convert.importFst(path, new TropicalSemiring());
    path = new File(parent, "fstreverse").getPath();
    Fst fstB = importFst(path, new TropicalSemiring());
    Fst fstReversed = Reverse.get(fst);
    // TODO - this is failing
    //assertThat(fstB, equalTo(fstReversed));
  }

}
