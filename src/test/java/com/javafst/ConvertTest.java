package com.javafst;

import static com.javafst.Convert.importFst;
import static com.javafst.operations.Connect.apply;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Test;

import com.javafst.semiring.TropicalSemiring;

import junit.framework.TestCase;

public class ConvertTest extends TestCase {

  @Test
  public void testIO() throws URISyntaxException, NumberFormatException, IOException {

    // This gets an error...
//    Path inputPath = Paths.get(ClassLoader.getSystemResource("openfst/basic.fst.txt").toURI());
//    Path subpath = inputPath.subpath(0, inputPath.toString().length() - 8);
    
//    System.out.println(subpath);
//    // This is a mess.
//    String path = "openfst/basic.fst.txt";
//    URL url = getClass().getResource(path);
//    String basename = (new File(url.toURI())).toString().replace(".fst.txt", "");
//    System.out.println(basename);
//    Fst fst = importFst(basename, new TropicalSemiring());    
//    Path temp = Files.createTempFile("output", "");
//    
//    // TODO - have to compare content.
//    Convert.export(fst, temp.toString());
//    System.out.println(temp);
//    
//    Path p = temp.resolveSibling(temp.getFileName() + ".fst.txt");
//    System.out.println(Files.readAllLines(p));
//    byte[] f1 = Files.readAllBytes(p);
//    System.out.println(f1);
//    byte[] f2 = Files.readAllBytes(Paths.get(basename).resolveSibling(Paths.get(basename).getFileName() + ".fst.txt"));
//    
//    Files.readAllLines(p).forEach(System.out::println);
//    Files.readAllLines(Paths.get(basename).resolveSibling(Paths.get(basename).getFileName() + ".fst.txt")).forEach(System.out::println);

    //assertTrue(Arrays.equals(f1, f2));

  }

}
