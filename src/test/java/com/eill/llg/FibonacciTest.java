package com.eill.llg;



import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.*;


@RunWith(Parameterized.class)
public class FibonacciTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }
        });
    }
    @Parameter
    public int fInput;
    @Parameter(1)
    public int fExpected;



    @Test
    public void filenameIncludesUsername() {
        assumeThat(1, is(0));
        System.out.println("I'm here"); // 这句不会被执
    }



}