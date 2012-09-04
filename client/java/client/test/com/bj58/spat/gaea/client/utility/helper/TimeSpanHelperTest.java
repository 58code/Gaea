package com.bj58.spat.gaea.client.utility.helper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bj58.spat.gaea.client.utility.helper.TimeSpanHelper;

import static org.junit.Assert.*;

/**
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TimeSpanHelperTest {

    public TimeSpanHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getIntFromTimeSpan method, of class TimeSpanHelper.
     */
    @Test
    public void testGetIntFromTimeSpan() {
        System.out.println("getIntFromTimeSpan");
        String timeSpan = "00:00:03";
        int expResult = 3000;
        int result = TimeSpanHelper.getIntFromTimeSpan(timeSpan);
        System.out.println(result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getTimeSpanFromInt method, of class TimeSpanHelper.
     */
    @Test
    public void testGetTimeSpanFromInt() throws Exception {
        System.out.println("getTimeSpanFromInt");
        int timeSpan = 0;
        String expResult = "";
        String result = TimeSpanHelper.getTimeSpanFromInt(timeSpan);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}