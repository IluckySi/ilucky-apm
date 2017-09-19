package com.ilucky.apm.os.collector;

import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudwise.os.utils.Constants;

public class DiskUsageCollector extends OsInfoCollector {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * TODO:获取type,desc,hrStorageAllocationUnits有问题.
	 */
	public Map<String, Object> collect() {
		sigar = getSigarInstance();
		Map<String, Object> diskUsageInfo = new HashMap<String, Object>();
		try {
			FileSystem fslist[] = sigar.getFileSystemList();
			for (int i = 0; i < fslist.length; i++) {
				FileSystem fs = fslist[i];
				FileSystemUsage fsUsage = null;
				try {
					fsUsage = sigar.getFileSystemUsage(fs.getDevName());
				} catch (Exception e) {
					continue;
				}
				if(fsUsage != null && fs.getType() != 1) {
					//putInMap(diskUsageInfo, "hrStorageType", i, Constants.OID_HOST_RESOURCES_TYPE + fs.getSysTypeName());
					putInMap(diskUsageInfo, "hrStorageType", i, Constants.OID_HOST_RESOURCES_TYPE + "FixedDisk");
					putInMap(diskUsageInfo, "hrStorageDescr", i, Constants.STRING + fs.getTypeName());
					putInMap(diskUsageInfo, "hrStorageUsed", i, Constants.INTEGER + fsUsage.getUsed());
					putInMap(diskUsageInfo, "hrStorageAllocationUnits", i, Constants.INTEGER + fsUsage.getAvail() + Constants.BYTES);
					putInMap(diskUsageInfo, "hrStorageSize", i, Constants.INTEGER + fsUsage.getTotal());
				}
			}
		} catch (Exception e) {
			logger.error("Collect disk usage occur exception -> {}", e);
		}
		return diskUsageInfo;
	}
}
