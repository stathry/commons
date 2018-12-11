package org.stathry.commons.model.dto;

/**
 * @author dongdaiming
 * @date 2018年1月4日
 */
public class DataRange<T> {

	private T min;
	private T max;

	public T getMin() {
		return min;
	}

	public void setMin(T min) {
		this.min = min;
	}

	public T getMax() {
		return max;
	}

	public void setMax(T max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "DataRange [min=" + min + ", max=" + max + "]";
	}

}
