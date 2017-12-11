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

import com.javafst.Fst;
import com.javafst.operations.Compose;
import com.javafst.semiring.Semiring;
import com.javafst.semiring.TropicalSemiring;


/**
 * Compose Testing for Examples provided by M. Mohri,
 * "Weighted Automata Algorithms", Handbook of Weighted Automata,
 * Springer-Verlag, 2009, pp. 213-254.
 */
public class ComposeTest {

  @Test
  public void testCompose() throws NumberFormatException, IOException, ClassNotFoundException, URISyntaxException {
    String path = "algorithms/compose/fstcompose.fst.txt";
    URL url = getClass().getResource(path);
    File parent = new File(url.toURI()).getParentFile();
    Semiring sr = new TropicalSemiring();

    path = new File(parent, "A").getPath();
    Fst fstA = importFst(path, sr);
    path = new File(parent, "B").getPath();
    Fst fstB = importFst(path, sr);
    
    path = new File(parent, "fstcompose").getPath();
    Fst expected = importFst(path, sr);
    Fst composed = Compose.get(fstA, fstB, sr);
    assertThat(composed, equalTo(expected));
    assertNull(Compose.get(fstA, null, sr));
    assertNull(Compose.get(null, fstB, sr));

    // Mangle fstB:
    fstB.setIsyms(new String[]{"blah", "hey", "yo"});
    assertNull(Compose.get(fstA, fstB, sr));
  }
}
