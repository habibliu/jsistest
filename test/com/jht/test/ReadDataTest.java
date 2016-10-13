package com.jht.test;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.UUID;

import org.junit.Test;

public class ReadDataTest {

	@Test
	public void testRunTest() {
		System.out.println(UUID.randomUUID().toString());
		
		int max=200;
        int min=1;
        Random random = new Random();

        int pageIndex = random.nextInt(max - min) + min;
        System.out.println(pageIndex);
		fail("Not yet implemented");
	}

	@Test
	public void testSetupTest() {
		
	}

}
