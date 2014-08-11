package com.global;

public class MathUtil {
	/**
	 * 排序由大到小
	 * 
	 * @param indexes
	 * @return
	 * @author lichao
	 */
	public static <T extends Number> T[] orderDesc(T[] indexes) {
		for (int i = 0; i < indexes.length; i++) {
			for (int j = i + 1; j < indexes.length; j++) {
				T temp;
				if (Float.valueOf(indexes[i].toString()) < Float
						.valueOf(indexes[j].toString())) {
					temp = indexes[i];
					indexes[i] = indexes[j];
					indexes[j] = temp;
				}
			}
		}
		return indexes;
	}
}
