package com.ilucky.apm.os.collector;

import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudwise.os.utils.Constants;

public class DiskIoCollector extends OsInfoCollector {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Map<String, Object> collect() {
		sigar = getSigarInstance();
		Map<String, Object> diskIo = new HashMap<String, Object>();
		try {
			FileSystem fslist[] = sigar.getFileSystemList();
			for (int i = 0; i < fslist.length; i++) {
				FileSystem fs = fslist[i];
				FileSystemUsage fsUsage = null;
				try {
					fsUsage = sigar.getFileSystemUsage(fs.getDevName());
				} catch (Exception e) {
				}
				if(fsUsage != null && fs.getType() != 1) {
					putInMap(diskIo, "diskIODevice", i,  Constants.STRING + fs.getDevName());
					putInMap(diskIo, "diskIOWrites", i,  Constants.COUNTER + fsUsage.getDiskWrites());
					putInMap(diskIo, "diskIONWritten", i, Constants.COUNTER + fsUsage.getDiskWriteBytes());
					putInMap(diskIo, "diskIOReads", i, Constants.COUNTER + fsUsage.getDiskReads());
					putInMap(diskIo, "diskIONRead", i, Constants.COUNTER + fsUsage.getDiskReadBytes());
				}
			}
		}
		catch (Exception e) {
			logger.error("Collect disk io occur exception -> {}", e);	
		}
		return diskIo;
	}
}
