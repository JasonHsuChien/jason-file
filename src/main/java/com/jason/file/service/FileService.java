package com.jason.file.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
//		String filename = "learning list.txt";
		File file = new File(filePath + "/" + filename);
		if (file.exists()) {
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;

			OutputStream os = null;
			try {
				os = response.getOutputStream();
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer);
					i = bis.read(buffer);
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			logger.info("----------file download" + filename);
			try {
				bis.close();
				fis.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
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
