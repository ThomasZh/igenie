package com.oct.ga.gatekeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.domain.gatekeeper.StpServerInfoJsonBean;
import com.oct.ga.comm.parser.JsonParser;
import com.oct.ga.stp.utility.BufferedRandomAccessFile;

// [{'serverIp':'182.92.71.66','port':13103},{'serverIp':'182.92.71.66','port':13105}]
//RoundRabin: step by step
public class ServerListCache
{
	private int index = 0;
	private List<StpServerInfoJsonBean> stpArray = new ArrayList<StpServerInfoJsonBean>();

	public List<StpServerInfoJsonBean> loadFromFile(String pathname)
			throws IOException
	{
		BufferedRandomAccessFile brafReadFile = new BufferedRandomAccessFile(pathname, "r");
		synchronized (brafReadFile) {
			int readSize = (int) brafReadFile.length();
			byte[] bytes = new byte[readSize];
			brafReadFile.read(bytes, 0, readSize);
			brafReadFile.close();

			String jsonArrayStr = new String(bytes);
			List<StpServerInfoJsonBean> serverList = JsonParser.json2ServerList(jsonArrayStr);

			for (StpServerInfoJsonBean serverInfo : serverList) {
				logger.debug("id: " + serverInfo.getStpId() + " ip: " + serverInfo.getServerIp() + " port: "
						+ serverInfo.getPort() + " active:" + serverInfo.isActive());

				stpArray.add(serverInfo);
			}

			return serverList;
		}
	}

	public StpServerInfoJsonBean next(String clientVersion)
	{
		synchronized (stpArray) {
			index++;
			if (index >= stpArray.size())
				index = 0;

			if (stpArray != null) {
				for (int i = index; i < stpArray.size(); i++) {
					StpServerInfoJsonBean stp = stpArray.get(i);
					if (stp.isActive()) {
						if (stp.getMaxVersion().compareTo(clientVersion) >= 0
								&& stp.getMinVersion().compareTo(clientVersion) <= 0)
							return stp;
					}
				}
				for (int i = 0; i < stpArray.size(); i++) {
					StpServerInfoJsonBean stp = stpArray.get(i);
					if (stp.isActive()) {
						if (stp.getMaxVersion().compareTo(clientVersion) >= 0
								&& stp.getMinVersion().compareTo(clientVersion) <= 0)
							return stp;
					}
				}
			}
		}
		return null;
	}

	public StpServerInfoJsonBean get(String stpId)
	{
		synchronized (stpArray) {
			for (StpServerInfoJsonBean serverInfo : stpArray) {
				if (serverInfo.getStpId().equals(stpId)) {
					return serverInfo;
				}
			}
		}

		return null;
	}

	public long getLastTryTimestamp(String stpId)
	{
		for (StpServerInfoJsonBean stp : stpArray)
			if (stpId.equals(stp.getStpId()))
				return stp.getLastTryTimestamp();

		return 0;
	}

	public void active(String stpId, long currentTimestamp)
	{
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();

		for (StpServerInfoJsonBean stp : stpArray)
			if (stpId.equals(stp.getStpId())) {
				long deltaTime = currentTimestamp - stp.getLastTryTimestamp();
				stp.setActive(deltaTime < 2 * gcv.getHeartbitInterval() ? true : false); // sec
			}
	}

	public void setState(String stpId, short state)
	{
		for (StpServerInfoJsonBean stp : stpArray) {
			if (stpId.equals(stp.getStpId())) {
				stp.setActive(state == 100 ? true : false); // sec
				break;
			}
		}
	}

	public void clear()
	{
		this.stpArray.clear();
	}

	public void add(StpServerInfoJsonBean stp)
	{
		this.stpArray.add(stp);
	}

	// monitor print list
	public List<StpServerInfoJsonBean> getAll()
	{
		return stpArray;
	}

	private final static Logger logger = LoggerFactory.getLogger(ServerListCache.class);
}
