package nahama.starwoods.util;

import java.util.ArrayList;

public class ArrayMap<K, V> {

	private ArrayList<K> listKey = new ArrayList<K>();
	private ArrayList<V> listValue = new ArrayList<V>();

	public void put(K key, V value) {
		listKey.add(key);
		listValue.add(value);
	}

	public boolean containsKey(K key) {
		return listKey.contains(key);
	}

	public V get(K key) {
		int i = listKey.indexOf(key);
		if (i < 0)
			return null;
		return listValue.get(i);
	}

	public K getKey(int index) {
		return listKey.get(index);
	}

	public V getValue(int index) {
		return listValue.get(index);
	}

	public void clear() {
		listKey.clear();
		listValue.clear();
	}

	public int size() {
		return listKey.size();
	}

}
