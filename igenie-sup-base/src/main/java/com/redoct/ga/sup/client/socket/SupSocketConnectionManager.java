package com.redoct.ga.sup.client.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.gatekeeper.SupServerInfo;
import com.oct.ga.stp.utility.BufferedRandomAccessFile;

/**
 * load balance manager, RoundRabin: step by step
 * [{'ip':'siriusb','port':15101},{'ip':'siriusb','port':15103}]
 * 
 * @author thomas
 */
public class SupSocketConnectionManager
{
	private int index = 0;
	private List<SupServerInfo> supArray = new ArrayList<SupServerInfo>();

	public void loadFromFile(String pathname)
			throws IOException
	{
		BufferedRandomAccessFile brafReadFile = new BufferedRandomAccessFile(pathname, "r");
		synchronized (brafReadFile) {
			int readSize = (int) brafReadFile.length();
			byte[] bytes = new byte[readSize];
			brafReadFile.read(bytes, 0, readSize);
			brafReadFile.close();

			String json = new String(bytes);
			List<SupServerInfo> serverList = null;

			if (json != null) {
				Gson gson = new Gson();
				serverList = gson.fromJson(json, new TypeToken<List<SupServerInfo>>()
				{
				}.getType());
			}

			for (SupServerInfo serverInfo : serverList) {
				logger.debug("id: " + serverInfo.getId() + " ip: " + serverInfo.getIp() + " port: "
						+ serverInfo.getPort() + " active:" + serverInfo.isActive());

				supArray.add(serverInfo);
			}
		}
	}

	public SupServerInfo next(short supType)
	{
		synchronized (supArray) {
			index++;
			if (index >= supArray.size())
				index = 0;

			if (supArray != null) {
				for (int i = index; i < supArray.size(); i++) {
					SupServerInfo sup = supArray.get(i);
					if (sup.isActive()) {
						if (sup.getType() == supType)
							return sup;
					}
				}
				for (int i = 0; i < supArray.size(); i++) {
					SupServerInfo sup = supArray.get(i);
					if (sup.isActive()) {
						if (sup.getType() == supType)
							return sup;
					}
				}
			}
		}
		return null;
	}

	public void setState(String supId, short state)
	{
		for (SupServerInfo sup : supArray) {
			if (supId.equals(sup.getId())) {
				sup.setActive(state == GlobalArgs.TRUE ? true : false); // sec
				break;
			}
		}
	}

	public void add(SupServerInfo sup)
	{
		for (SupServerInfo server : supArray) {
			if (sup.getId().equals(server.getId())) {
				return;
			}
		}
		supArray.add(sup);
	}

	public void clear()
	{
		this.supArray.clear();
	}

	private final static Logger logger = LoggerFactory.getLogger(SupSocketConnectionManager.class);
}
