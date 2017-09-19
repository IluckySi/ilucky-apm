package com.ilucky.apm.os.collector;

import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

public abstract class OsInfoCollector {

	public static SigarProxy sigar;
	
	public abstract Map<String, Object> collect();
	
	public static SigarProxy getSigarInstance() {
		if(sigar == null) {
			sigar = SigarProxyCache.newInstance();
		}
		return sigar;
	}
	
	public Map<String, Object> putInMap(String i, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(i, value);
		return map;
	}
	
	public Map<String, String> putInMap(HashMap<String, String> map, String i, String value) {
		map.put(i, value);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public void putInMap(Map<String, Object> map, String field, int i, String value) {
		String number = i + 1 + "";
		if(map.get(field) == null) {
			map.put(field, new HashMap<String, String>());
		}
		map.put(field, putInMap((HashMap<String, String>)map.get(field), number, value));
	}
}
