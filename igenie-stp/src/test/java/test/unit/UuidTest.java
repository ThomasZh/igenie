package test.unit;

import java.util.UUID;

public class UuidTest {
	public static void main(String argv[]) {
		for (int i = 0; i < 50; i++)
			System.out.println(UUID.randomUUID().toString());
	}
}
