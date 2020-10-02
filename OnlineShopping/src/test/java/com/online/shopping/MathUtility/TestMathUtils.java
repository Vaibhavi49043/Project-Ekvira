package com.online.shopping.MathUtility;

import org.junit.Assert;
import org.junit.Test;

public class TestMathUtils {

	@Test
	public void shouldReturnDoubleValueWithPrecisionTwo() {
		Assert.assertEquals(Double.valueOf("11.11"), Double.valueOf(MathUtils.round(11.11111111111111, 2)));
	}
}
