package com.ilucky.apm.os.collector;

import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Swap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudwise.os.utils.Constants;

public class MemoryInfoCollector extends OsInfoCollector {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * TODO:获取buffer和cache有问题.
	 */
	@Override
	public Map<String, Object> collect() {
		sigar = getSigarInstance();
		Map<String, Object> memoryInfo = new HashMap<String, Object>();
		try {
			Mem mem = sigar.getMem();
			//memoryInfo.put("memAvailReal", putInMap("0", Constants.INTEGER + (mem.getActualFree() / 1024L) + Constants.KB));
			memoryInfo.put("memAvailReal", putInMap("0", Constants.INTEGER + (mem.getFree() / 1024L) + Constants.KB));
			memoryInfo.put("memTotalReal", putInMap("0", Constants.INTEGER + (mem.getTotal() / 1024L) + Constants.KB));
			memoryInfo.put("memBuffer", putInMap("0", Constants.INTEGER + ((mem.getUsed() - mem.getActualUsed()) / 1024L) + Constants.KB));
			memoryInfo.put("memCached", putInMap("0", Constants.INTEGER + 0L + Constants.KB));
			//memoryInfo.put("memCached", putInMap("0", Constants.INTEGER + ((mem.getUsed() - mem.getActualUsed()) / 1024L) + Constants.KB));
			Swap swap = sigar.getSwap();
			memoryInfo.put("memTotalSwap", putInMap("0", Constants.INTEGER + (swap.getTotal() / 1024L) + Constants.KB));
			memoryInfo.put("memAvailSwap", putInMap("0", Constants.INTEGER + (swap.getFree() / 1024L) + Constants.KB));
		} catch (Exception e) {
			logger.error("Collect memory info occur exception -> {}", e);
		}
		return memoryInfo;
	}
}
