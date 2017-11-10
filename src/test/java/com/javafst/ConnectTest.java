package com.javafst;

import static com.javafst.Convert.importFst;
import static com.javafst.operations.Connect.apply;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

import com.javafst.semiring.TropicalSemiring;


public class ConnectTest {

    @Test
    public void testConnect() throws NumberFormatException, IOException, ClassNotFoundException, URISyntaxException {
        String path = "algorithms/connect/fstconnect.fst.ser";
        URL url = getClass().getResource(path);
        File parent = new File(url.toURI()).getParentFile();

        path = new File(parent, "A").getPath();
        Fst fst = importFst(path, new TropicalSemiring());
        path = new File(parent, "fstconnect.fst.ser").getPath();
        Fst connectSaved = Fst.loadModel(path);
        
        apply(fst);
        assertThat(connectSaved, equalTo(fst));
    }

}
