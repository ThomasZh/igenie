package com.oct.ga.comm.parser;

//权限认证： owner:4, group:2, other:1
// rws: (read, write, share), eg:664
public class PermissionModeParser {

	public static int setMode(int owner, int group, int other) {
		return setMode((short) owner, (short) group, (short) other);
	}

	public static int setMode(short owner, short group, short other) {
		int mode = ((owner << 6) + (group << 3) + other) & 0x01ff;
		return mode;
	}

	public static short getOwnerMode(final int mode) {
		return (short) ((mode & 0x01c0) >> 6);
	}

	public static short getGroupMode(final int mode) {
		return (short) ((mode & 0x38) >> 3);
	}

	public static short getOtherMode(final int mode) {
		return (short) (mode & 0x07);
	}

	public static boolean isRead(final short mode) {
		return (mode & 0x04) == 4;
	}
	
	public static boolean isWrite(final short mode) {
		return (mode & 0x02) == 2;
	}
	
	public static boolean isShare(final short mode) {
		return (mode & 0x01) == 1;
	}

	public static void main(String[] args) {
		int mode = PermissionModeParser.setMode(7, 7, 0);
		System.out.println(mode);

		short owner = PermissionModeParser.getOwnerMode(mode);
		System.out.println(owner);
		short group = PermissionModeParser.getGroupMode(mode);
		System.out.println(group);
		short other = PermissionModeParser.getOtherMode(mode);
		System.out.println(other);
		
		System.out.println(PermissionModeParser.isRead(owner));
		System.out.println(PermissionModeParser.isWrite(owner));
		System.out.println(PermissionModeParser.isShare(owner));
		
		System.out.println(PermissionModeParser.isRead(group));
		System.out.println(PermissionModeParser.isWrite(group));
		System.out.println(PermissionModeParser.isShare(group));
		
		System.out.println(PermissionModeParser.isRead(other));
		System.out.println(PermissionModeParser.isWrite(other));
		System.out.println(PermissionModeParser.isShare(other));
	}

}
