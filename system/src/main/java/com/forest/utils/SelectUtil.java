package com.forest.utils;

public class SelectUtil {
	public static int returnOp(String op)
	{
		switch (op) {
		case "eq":
			return 0;

		default:
			return 1;
		}
	}
}
