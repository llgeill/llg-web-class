package com.eill.llg;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.DEFAULT)
public class TestMethodOrder {

    @Test
    @Ignore("Test is ignored as a demonstration")
    public void testA() {
        System.out.println("first");
    }
    @Test(timeout = 1000)
    public  void testWithTimeout() throws InterruptedException {
        Thread.sleep(2000);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void empty() {
        new ArrayList<Object>().get(0);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldTestExceptionMessage() throws IndexOutOfBoundsException {
        List<Object> list = new ArrayList<Object>();

        thrown.expect(IndexOutOfBoundsException.class);
        thrown.expectMessage("Index: 0, Size: 0");
        list.get(0); // execution will never get past this line
    }
}
