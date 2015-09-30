package com.redoct.ga.web.comm;

import java.util.ArrayList;
import java.util.List;

public class JSONUtil
{
	public static String toComboListJSONString(String[] id, String[] value)
	{
		if (id != null && value != null) {
			String rs = "{";
			for (int i = 0; i < id.length; i++) {
				if (i == 0)
					rs += "'':'All',";
				rs += "'" + id[i];
				rs += "':'";
				rs += value[i] + "'";
				if (i < id.length - 1)
					rs += ",";
			}
			rs += "}";
			return rs;
		} else
			return null;
	}

	public static List<Object> toSparklineSeriesData(
			List<Object[]> rawSeriesData, long startTime, long endTime,
			long interval)
	{
		List<Object> rsList = new ArrayList<Object>();

		int i = 0;
		int j = 0;
		for (long time = startTime; time <= endTime; j++) {
			long timestamp = 0;
			Object value = null;
			if (i < rawSeriesData.size()) {
				Object[] rawData = rawSeriesData.get(i);
				timestamp = (Long) rawData[0];
				value = rawData[1];
			}
			if (time == timestamp) {
				if (value != null)
					rsList.add(value);
				else
					rsList.add("null");
				i++;
			} else
				rsList.add("null");

			time += interval;
		}

		return rsList;
	}
}
