package com.jason.file.util;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class OpenOfficeUtil {

	private static Log logger = LogFactory.getLog(OpenOfficeUtil.class);

	private static final String OPENOFFICE_HOME = "C:\\Program Files (x86)\\OpenOffice 4\\";

	private static final String COMMAND = "program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
	
	private static final String TYPE = ".pdf";

	public static File office2PDF(String sourceFile) {
		File file = new File(sourceFile);  
		if (!file.exists()) {  
			return null;
		}  
		File pdfFile = new File(sourceFile.substring(0, sourceFile.lastIndexOf(".")) + TYPE);
		logger.info("pdfFile path:" + pdfFile.getPath());
		
		Process pro = null;
		try {
			String startCommand = OPENOFFICE_HOME + COMMAND;
			pro = Runtime.getRuntime().exec(startCommand);

			OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
			connection.connect();

			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(file, pdfFile);

			connection.disconnect();
			pro.destroy();
		} catch (ConnectException e) {
			logger.error("PDF 转换失败，OpenOffice 服务未启动！", e);
		} catch (IOException e) {
			logger.error("PDF 转换失败，OpenOffice 服务关闭失败！", e);
		} finally {
			if (pro != null) {
				pro.destroy();
			}
		}

		return pdfFile;
	}
}
