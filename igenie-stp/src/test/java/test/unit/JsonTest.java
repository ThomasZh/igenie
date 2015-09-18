package test.unit;

import net.sf.json.JSONArray;

import com.oct.ga.comm.parser.JsonParser;

public class JsonTest
{
	public static void main(String[] args)
	{
		String[] ids = new String[] { "a","b" };

		JSONArray jsonArray = JSONArray.fromObject(ids);
		String json = jsonArray.toString();

		System.out.println(json);

		String[] array = JsonParser.json2StringArray(json);

		for (String s : array) {
			System.out.println(s);
		}
	}
}
