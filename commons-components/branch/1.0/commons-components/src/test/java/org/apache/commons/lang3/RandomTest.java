package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

/**
 * TODO
 * 
 * @author dongdaiming
 * @date 2017年12月6日
 */
public class RandomTest {

	@Test
	public void test1() {
		for (int i = 0; i < 10; i++) {
			System.out.println(Math.random());
		}
	}

	@Test
	public void test2() {
		Random r = new Random();
		int limit = 1000;
		Set<Integer> set = new HashSet<>(limit);
		for (int i = 0; i < limit; i++) {
			set.add(r.nextInt(limit));
		}
		System.out.println(set.size());
	}

	@Test
	public void test3() {
		SecureRandom r = new SecureRandom();
		int limit = 10;
		Set<Integer> set = new HashSet<>(limit);
		for (int i = 0; i < limit; i++) {
			set.add(r.nextInt(limit));
		}
		System.out.println(set.size());
		System.out.println(set);
	}

	@Test
	public void test4() {
		for (int i = 0; i < 6; i++) {
			System.out.println(RandomStringUtils.randomNumeric(6));
		}
	}
	@Test
	public void test5() {
		for (int i = 0; i < 100; i++) {
			System.out.println(RandomStringUtils.random(4, true, false));
		}
	}
	@Test
	public void test6() {
		for (int i = 0; i < 100; i++) {
			System.out.println(RandomStringUtils.random(4, 'p', 'h', 'a', 'n'));
		}
	}

}
