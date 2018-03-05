package com.ywzlp.webchat.msg.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

@SuppressWarnings("unchecked")
public class ConcurrentHashBidiMap<K, V> implements ConcurrentBidiMap<K, V> {

	private final BidiMap map = new DualHashBidiMap();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Override
	public V putIfAbsent(K key, V value) {
		try {
			lock.writeLock().lock();
			V oldVal;
			return (oldVal = (V) map.get(key)) == null ? (V) map.put(key, value) : oldVal;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public boolean remove(Object key, Object value) {
		try {
			lock.writeLock().lock();
			return map.remove(key, value);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		try {
			lock.writeLock().lock();
			return map.replace(key, oldValue, newValue);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public V replace(K key, V value) {
		try {
			lock.writeLock().lock();
			return (V) map.replace(key, value);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public int size() {
		try {
			lock.readLock().lock();
			return map.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		try {
			lock.readLock().lock();
			return map.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsKey(Object key) {
		try {
			lock.readLock().lock();
			return map.containsKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsValue(Object value) {
		try {
			lock.readLock().lock();
			return map.containsValue(value);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V get(Object key) {
		try {
			lock.readLock().lock();
			return (V) map.get(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V put(K key, V value) {
		try {
			lock.writeLock().lock();
			return (V) map.put(key, value);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public V remove(Object key) {
		try {
			lock.writeLock().lock();
			return (V) map.remove(key);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		try {
			lock.writeLock().lock();
			map.putAll(m);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void clear() {
		try {
			lock.writeLock().lock();
			map.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Set<K> keySet() {
		try {
			lock.readLock().lock();
			return map.keySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Collection<V> values() {
		try {
			lock.readLock().lock();
			return map.values();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		try {
			lock.readLock().lock();
			return map.entrySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public K getKey(V v) {
		try {
			lock.readLock().lock();
			return (K) map.getKey(v);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V getValue(K k) {
		try {
			lock.readLock().lock();
			return (V) map.get(k);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public K removeByValue(V v) {
		try {
			lock.writeLock().lock();
			return (K) map.removeValue(v);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
}
