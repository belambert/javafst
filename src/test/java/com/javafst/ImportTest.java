package com.javafst;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

import com.javafst.semiring.TropicalSemiring;


public class ImportTest  {

    @Test
    public void testConvert() throws NumberFormatException, IOException, ClassNotFoundException, URISyntaxException {
        URL url = getClass().getResource("openfst/basic.fst");
        String dir = new File(url.toURI()).getParent();
        
        String path = new File(dir, "basic").getPath();
        Fst fst1 = Convert.importFst(path, new TropicalSemiring());

        path = new File(dir, "basic.fst.ser").getPath();
        Fst fst2 = Fst.loadModel(path);

        assertThat(fst1, equalTo(fst2));
    }

}
