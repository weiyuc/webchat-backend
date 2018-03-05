package com.ywzlp.webchat.msg.util;

import java.util.concurrent.ConcurrentMap;

/**
 * @author yuwei
 *
 * @param <K>
 * @param <V>
 */
public interface ConcurrentBidiMap<K, V> extends ConcurrentMap<K, V> {
	
	/**
	 * get key by value
	 * @param v
	 * @return
	 */
	K getKey(V v);
	
	/**
	 * get value by key
	 * @param k
	 * @return
	 */
	V getValue(K k);
	
	/**
	 * remove by value
	 * @param v
	 * @return
	 */
	K removeByValue(V v);
	
}
