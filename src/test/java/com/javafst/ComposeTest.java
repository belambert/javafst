package com.javafst;

import static com.javafst.Convert.importFst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

import com.javafst.operations.Compose;
import com.javafst.semiring.TropicalSemiring;


/**
 * Compose Testing for Examples provided by M. Mohri,
 * "Weighted Automata Algorithms", Handbook of Weighted Automata,
 * Springer-Verlag, 2009, pp. 213-254.
 */
public class ComposeTest {

  @Test
  public void testCompose() throws NumberFormatException, IOException, ClassNotFoundException, URISyntaxException {
    String path = "algorithms/compose/fstcompose.fst.ser";
    URL url = getClass().getResource(path);
    File parent = new File(url.toURI()).getParentFile();

    path = new File(parent, "A").getPath();
    Fst fstA = importFst(path, new TropicalSemiring());
    path = new File(parent, "B").getPath();
    Fst fstB = importFst(path, new TropicalSemiring());
    
    path = new File(parent, "fstcompose").getPath();
    Fst composed = importFst(path, new TropicalSemiring());
        
    Fst fstComposed = Compose.get(fstA, fstB, new TropicalSemiring());
    // comp
    assertThat(fstComposed, equalTo(composed));
    //assertThat(fstA, equalTo(fstA));
    
    
  }
}
