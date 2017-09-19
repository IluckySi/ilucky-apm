package com.ilucky.apm.os.collector;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudwise.os.utils.Constants;

public class ProcessInfoCollector extends OsInfoCollector {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Map<String, Object> collect() {
		sigar = getSigarInstance();
		Map<String, Object> processInfo = new HashMap<String, Object>();
		try {
			long[] procList = sigar.getProcList();
			processInfo.put("hrSystemProcesses", putInMap("0", Constants.GAUGE + (procList == null ? 0 : procList.length)));
		} catch (Exception e) {
			logger.error("Collect process info occur exception -> {}", e);
		}
		return processInfo;
	}
}
