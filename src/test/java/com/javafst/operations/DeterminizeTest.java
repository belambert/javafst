package com.javafst.operations;

import static com.javafst.Convert.importFst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

import com.javafst.Convert;
import com.javafst.Fst;
import com.javafst.operations.Determinize;
import com.javafst.semiring.TropicalSemiring;

  
public class DeterminizeTest {

  @Test
  public void testDeterminize() throws NumberFormatException, IOException, ClassNotFoundException, URISyntaxException {
    String path = "algorithms/determinize/fstdeterminize.fst.txt";
    URL url = getClass().getResource(path);
    File parent = new File(url.toURI()).getParentFile();

    path = new File(parent, "A").getPath();
    Fst fstA = Convert.importFst(path, new TropicalSemiring());
    path = new File(parent, "fstdeterminize").getPath();

    Fst determinized = importFst(path, new TropicalSemiring());
    Fst fstDeterminized = Determinize.get(fstA);
    assertThat(fstDeterminized, equalTo(determinized));

    fstA.setSemiring(null);
    assertNull(Determinize.get(fstA));
  }
}
