package com.javafst;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

import com.javafst.operations.RmEpsilon;
import com.javafst.semiring.ProbabilitySemiring;


public class RmEpsilonTest {

    @Test
    public void testRmEpsilon() throws NumberFormatException, IOException, ClassNotFoundException, URISyntaxException {
        URL url = getClass().getResource("algorithms/rmepsilon/A.fst.txt");
        File parent = new File(url.toURI()).getParentFile();

        String path = new File(parent, "A").getPath();
        Fst fst = Convert.importFst(path, new ProbabilitySemiring());
        path = new File(parent, "fstrmepsilon.fst.ser").getPath();
        Fst fstRmEps = Fst.loadModel(path);

        Fst rmEpsilon = RmEpsilon.get(fst);
        assertThat(fstRmEps, equalTo(rmEpsilon));
    }
}
