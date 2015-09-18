package test.unit;

import java.util.Date;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.EcryptUtil;

public class TimestampTest
{
	public static void main(String argv[])
	{
		// ga: 1412976704
		// 679f9fdfdff54036995049c2efc3da8b
		Date date1 = new Date(1412976704000L);
		System.out.println(date1.toString());
		System.out.println(EcryptUtil.md5(date1.toString()));

		// ce22ec3c67bdace20cbdc7eb1dd7ab25
		// 3234691716632fb1d74f61fd8497a637

		// cscart: 1413019757
		// 9502ad4eccc5d594f50abb265ab39d6c
		Date date2 = new Date(1413019757000L);
		System.out.println(date2.toString());
		System.out.println(EcryptUtil.md5("" + 1413019757));

		System.out.println(DatetimeUtil.currentTimestamp());

		System.out.println(DatetimeUtil.today2Str());
	}

}
