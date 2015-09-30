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
 * 我在写一个基于jsp/servlet的web应用程序,
 * 其中客户有一个需求,批量下载文件,当然首先需要先用java.util.zip打包(除了打包,没想到其他方法).
 * 由于是事先不知道客户要下哪个目录下的文件,并且文件目录下的文件也是会变的,所有要求动态的打包.
 * 
 * 通过HttpServletResponse的getOutputStream()方法获得ServletOutputStream,
 * 然后再得到ZipOutputStream对象就可以了, 其他的和普通的文件读写一样.
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
		// 设置响应头,MIMEtype告诉浏览器传送的文件类型
		response.setContentType("application/x-zip-compressed ");
		// inline;参数让浏览器弹出下载窗口,而不是在网页中打开文件.filename设定文件名
		response.setHeader("Content-Disposition ",
				"inline;   filename=download.zip ");
		// 通过response获得ServletOutputStream对象
		ServletOutputStream sos = response.getOutputStream();
		// 获得ZipOutputStream对象
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(sos));
		// 得到要下载的文件对象
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				"c:\\grub.exe "));
		// 在zip文件中新建一个grub.exe文件
		out.putNextEntry(new ZipEntry("grub.exe "));
		// 逐字读出写入
		int c;
		while ((c = in.read()) != -1) {
			out.write(c);
		}
		in.close();
		out.close(); // 这里一句一定要,要不就会打开文件出错
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
