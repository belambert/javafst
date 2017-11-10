package com.javafst;

import static com.javafst.Convert.importFst;
import static com.javafst.operations.Compose.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

import com.javafst.semiring.TropicalSemiring;

public class ComposeEpsilonTest {

  @Test
  public void testCompose() throws NumberFormatException, IOException, URISyntaxException {
    URL url = getClass().getResource("algorithms/composeeps/A.fst.txt");
    File parent = new File(url.toURI()).getParentFile();

    String path = new File(parent, "A").getPath();
    Fst fstA = importFst(path, new TropicalSemiring());
    path = new File(parent, "B").getPath();
    Fst fstB = importFst(path, new TropicalSemiring());
    path = new File(parent, "fstcomposeeps").getPath();
    Fst fstC = importFst(path, new TropicalSemiring());

    Fst fstComposed = get(fstA, fstB, new TropicalSemiring());
    assertThat(fstC, equalTo(fstComposed));
  }

}
