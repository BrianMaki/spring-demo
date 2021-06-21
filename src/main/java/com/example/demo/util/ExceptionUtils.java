package com.example.demo.util;

public final class ExceptionUtils {

	private ExceptionUtils() {
	}

	public static Throwable getRootCause(Throwable ex) {
		if (ex == null) {
			return ex;
		}
		while (ex.getCause() != null) {
			ex = ex.getCause();
		}
		return ex;
	}

	public static boolean isCause(Class<? extends Throwable> expected, Throwable e) {
		return expected.isInstance(e) || (e != null && isCause(expected, e.getCause()));
	}
}
