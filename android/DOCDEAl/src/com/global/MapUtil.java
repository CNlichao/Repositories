package com.global;

import java.util.Iterator;
import java.util.Map;

public class MapUtil {
	/**
	 * 移除map中所有元素
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
