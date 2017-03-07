package com.example.api.test.misc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;


public class ParallelStreamTest {

	@Test
	public void parallelStreamTest(){
		List<Integer> tens = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
		List<Integer> thousands = IntStream.rangeClosed(2001, 2010).boxed().collect(Collectors.toList());
		
		final List<String> hits = Collections.synchronizedList(new ArrayList<String>());
		
		tens
		.parallelStream()
		.forEach(ten -> {
			thousands
			.parallelStream()
			.forEach(thousand -> {
				String print = ten + ", " + thousand;
				hits.add(print);
			});
		});
		
		Assert.assertTrue("Loop count has to be 100, and is "+hits.size(), hits.size() == 100);
	}
	
	
	@Test
	public void datesTests(){
		Instant instant = Instant.ofEpochSecond(1450005600);
		
		System.out.println(instant);
	}
	
	
	@Test
	public void zoneIdTest(){
		
		Integer milliseconds = Integer.parseInt("-3") * 60 * 60 * 1000;
		String[] iDs = TimeZone.getAvailableIDs(milliseconds);		
		
		System.out.println(iDs);
	}
	
	
}
