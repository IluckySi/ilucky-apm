package com.ilucky.apm.os.collector;

import java.util.HashMap;
import java.util.Map;

public class CpuInfoCollector extends OsInfoCollector {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * ssCpuRawKernel=ssCpuRawUser+ssCpuRawSystem.
	 */
	@Override
	public Map<String, Object> collect() {
		sigar = getSigarInstance();
		Map<String, Object> cpuInfo = new HashMap<String, Object>();
		try {
			Cpu cpu = sigar.getCpu();
			cpuInfo.put("ssCpuRawUser", putInMap("0", Constants.COUNTER + cpu.getUser()));
			cpuInfo.put("ssCpuRawInterrupt",putInMap("0",  Constants.COUNTER + cpu.getIrq()));
			cpuInfo.put("ssCpuRawNice", putInMap("0", Constants.COUNTER + cpu.getNice()));
			cpuInfo.put("ssCpuRawSystem", putInMap("0", Constants.COUNTER + cpu.getSys()));
			cpuInfo.put("ssCpuRawWait", putInMap("0", Constants.COUNTER + cpu.getWait()));
			cpuInfo.put("ssCpuRawSoftIRQ", putInMap("0", Constants.COUNTER + cpu.getSoftIrq()));
			cpuInfo.put("ssCpuRawIdle", putInMap("0", Constants.COUNTER + cpu.getIdle()));
			cpuInfo.put("ssCpuRawKernel", putInMap("0", Constants.COUNTER + (cpu.getUser() + cpu.getSys())));
		} catch (Exception e) {
			logger.error("Collect cpu info occur exception -> {}", e);
		}
		return cpuInfo;
	}
}
