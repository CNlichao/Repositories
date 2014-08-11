package com.global;

import java.util.Iterator;
import java.util.Map;

public class MapUtil {
	/**
	 * �Ƴ�map������Ԫ��
	 * 
	 * @param map
	 */
	public static <K, V> void removeAll(Map<K, V> map) {
		if (map != null && !map.isEmpty()) {
			Iterator<K> keyI = map.keySet().iterator();
			while (keyI.hasNext()) {
				K key = keyI.next();
				keyI.remove();
			}

		}

	}
}
