package com.ilucky.apm.os.collector;

import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.OperatingSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudwise.os.utils.Constants;

public class LoadInfoCollector extends OsInfoCollector {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Map<String, Object> collect() {
		sigar = getSigarInstance();
		Map<String, Object> loadInfo = new HashMap<String, Object>();
		try {
			if (!OperatingSystem.IS_WIN32) {
				double[] loadAverageArray = sigar.getLoadAverage();
				if(loadAverageArray != null && loadAverageArray.length == 3) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("1", Constants.OPAQUE_FLOAT + loadAverageArray[0]);
					map.put("2", Constants.OPAQUE_FLOAT + loadAverageArray[1]);
					map.put("3", Constants.OPAQUE_FLOAT + loadAverageArray[2]);
					loadInfo.put("laLoadFloat", map);
				}
			}
		} catch (Exception e) {
			logger.error("Collect load info occur exception -> {}", e);
		}
		return loadInfo;
	}
}