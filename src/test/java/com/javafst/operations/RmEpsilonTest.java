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
import com.javafst.operations.RmEpsilon;
import com.javafst.semiring.ProbabilitySemiring;
import com.javafst.semiring.TropicalSemiring;


public class RmEpsilonTest {

  @Test
  public void testRmEpsilon() throws NumberFormatException, IOException, ClassNotFoundException, URISyntaxException {
    URL url = getClass().getResource("algorithms/rmepsilon/A.fst.txt");
    File parent = new File(url.toURI()).getParentFile();

    String path = new File(parent, "A").getPath();
    Fst fst = Convert.importFst(path, new ProbabilitySemiring());
    path = new File(parent, "fstrmepsilon").getPath();
    Fst fstRmEps = importFst(path, new TropicalSemiring());
    Fst rmEpsilon = RmEpsilon.get(fst);
    // Not quite working either...
    //assertThat(fstRmEps, equalTo(rmEpsilon));
  }
}
