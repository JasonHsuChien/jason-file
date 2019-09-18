package com.jason.file.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jason.file.util.OpenOfficeUtil;

@Service
public class FileService {

	@Value("${file.upload.path}")
	private String filePath;

	private static Log logger = LogFactory.getLog(FileService.class);

	public String fileUpload(MultipartFile file) {
		if (file.isEmpty()) {
			return "false";
		}
		String fileName = file.getOriginalFilename();
		int size = (int) file.getSize();
		logger.info(fileName + "-->" + size);

		File dest = new File(filePath + "/" + fileName);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdir();
		}
		try {
			file.transferTo(dest);
			return "true";
		} catch (IllegalStateException e) {
			return "false";
		} catch (IOException e) {
			return "false";
		}
	}

	public String fileUpload(List<MultipartFile> files) {
		if (files.isEmpty()) {
			return "false";
		}

		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			int size = (int) file.getSize();
			logger.info(fileName + "-->" + size);

			if (file.isEmpty()) {
				return "false";
			} else {
				File dest = new File(filePath + "/" + fileName);
				if (!dest.getParentFile().exists()) {
					dest.getParentFile().mkdir();
				}
				try {
					file.transferTo(dest);
				} catch (Exception e) {
					return "false";
				}
			}
		}
		return "true";
	}

	public List<String> getFiles() {
		List<String> files = new ArrayList<String>();
		File file = new File(filePath);
		File[] tempList = file.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isDirectory()) {
				continue;
			}
			if (tempList[i].isFile()) {
				files.add(tempList[i].getName());
			}
		}
		return files;
	}

	public String downLoad(HttpServletResponse response, String filename) {
		File file = new File(filePath + "/" + filename);
		if (file.exists()) {
			try {
				response.setContentType("application/force-download");
				response.setHeader("Content-Disposition",
						"attachment;fileName=" + URLEncoder.encode(filename, "UTF-8").replaceAll("\\+","%20"));

				byte[] buffer = new byte[1024];
				OutputStream os = response.getOutputStream();
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer);
					i = bis.read(buffer);
				}
				bis.close();
				fis.close();
			} catch (IOException e) {
				logger.error("----------file download failed", e);
			}
		}
		logger.info("----------file download success" + filename);
		return null;
	}

	public String office2PDF(MultipartFile file) {
		if (file.isEmpty()) {
			return "false";
		}
		String fileName = file.getOriginalFilename();
		String sourceFile = filePath + "/" + fileName;
		OpenOfficeUtil.office2PDF(sourceFile);
		return "true";
	}

}
