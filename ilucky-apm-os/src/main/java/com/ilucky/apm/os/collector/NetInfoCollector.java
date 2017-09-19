package com.ilucky.apm.os.collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudwise.os.utils.Constants;

public class NetInfoCollector extends OsInfoCollector {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * TODO:有些字段不清楚什么意思:ifInUcastPkts,ifOutUcastPkts,ifInOctets...
	 */
	public Map<String, Object> collect() {
		sigar = getSigarInstance();
		Map<String, Object> netInfo = new HashMap<String, Object>();
		try {
			List<String> netInterfaceList = getEffectiveNetInterface();
			for(int i = 0; netInterfaceList != null && i < netInterfaceList.size(); i++) {
				String ifName = netInterfaceList.get(i);
				NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ifName);
				NetInterfaceStat ifStat = sigar.getNetInterfaceStat(ifName);
				//上行流量:tx=in,下行流量:rx=out;
				if(ifConfig != null && ifStat != null) {
					putInMap(netInfo, "ifIndex", i, Constants.INTEGER + i);
					putInMap(netInfo, "ifMt", i, Constants.INTEGER + ifConfig.getMtu());
					putInMap(netInfo, "ifDescr", i, Constants.STRING + ifConfig.getDescription());
					putInMap(netInfo, "ifSpeed", i, Constants.GAUGE + ifStat.getSpeed());
					putInMap(netInfo, "ifInUcastPkts", i, Constants.COUNTER + ifStat.getTxPackets());
					putInMap(netInfo, "ifOutUcastPkts", i, Constants.COUNTER + ifStat.getRxPackets());
					putInMap(netInfo, "ifInOctets", i, Constants.COUNTER + ifStat.getTxBytes());
					putInMap(netInfo, "ifOutOctets", i, Constants.COUNTER + ifStat.getRxBytes());
					putInMap(netInfo, "ifHCInOctets", i, Constants.COUNTER + ifStat.getTxBytes());
					putInMap(netInfo, "ifHCOutOctets", i, Constants.COUNTER + ifStat.getRxBytes());
				}
			}
		} catch (Exception e) {
			logger.error("Collect net info occur exception -> {}", e);
		}
		return netInfo;
	}
	
	/**
	 * 获取正在使用的网口.
	 * @return
	 */
	public List<String> getEffectiveNetInterface() {
		sigar = getSigarInstance();
		List<String> list = new ArrayList<String>();
		try {
			String[] netInterfaceList = sigar.getNetInterfaceList();
			for(int i = 0; netInterfaceList != null && i < netInterfaceList.length; i++) {
				String ifName = netInterfaceList[i];
				NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ifName);
				String mask = ifConfig.getNetmask();
				String address = ifConfig.getAddress();
				if (!mask.equals("0.0.0.0") && !address.equals("0.0.0.0")) {
					if (OperatingSystem.IS_WIN32) {
						logger.debug("This is Windows operating system");
						if (!mask.equals("255.0.0.0") && !address.equals("127.0.0.1")) {
							list.add(ifName);
						}
					} else {
						list.add(ifName);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Get effective net interface info occur exception -> {}", e);
		}
		return list;
	}
}