package com.redoct.ga.web.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ����дһ������jsp/servlet��webӦ�ó���,
 * ���пͻ���һ������,���������ļ�,��Ȼ������Ҫ����java.util.zip���(���˴��,û�뵽��������).
 * ���������Ȳ�֪���ͻ�Ҫ���ĸ�Ŀ¼�µ��ļ�,�����ļ�Ŀ¼�µ��ļ�Ҳ�ǻ���,����Ҫ��̬�Ĵ��.
 * 
 * ͨ��HttpServletResponse��getOutputStream()�������ServletOutputStream,
 * Ȼ���ٵõ�ZipOutputStream����Ϳ�����, �����ĺ���ͨ���ļ���дһ��.
 * 
 * @author Thomas.H.Zhang
 */
public class ZipDownLoad
		extends HttpServlet
{
	/**
	 * Handles GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		// ������Ӧͷ,MIMEtype������������͵��ļ�����
		response.setContentType("application/x-zip-compressed ");
		// inline;������������������ش���,����������ҳ�д��ļ�.filename�趨�ļ���
		response.setHeader("Content-Disposition ",
				"inline;   filename=download.zip ");
		// ͨ��response���ServletOutputStream����
		ServletOutputStream sos = response.getOutputStream();
		// ���ZipOutputStream����
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(sos));
		// �õ�Ҫ���ص��ļ�����
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				"c:\\grub.exe "));
		// ��zip�ļ����½�һ��grub.exe�ļ�
		out.putNextEntry(new ZipEntry("grub.exe "));
		// ���ֶ���д��
		int c;
		while ((c = in.read()) != -1) {
			out.write(c);
		}
		in.close();
		out.close(); // ����һ��һ��Ҫ,Ҫ���ͻ���ļ�����
	}

	/**
	 * Handles POST requests
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException
	{
		this.doGet(request, response);
	}

	/**
	 * Destroy the servlet
	 */
	public void destroy()
	{
	}
}
