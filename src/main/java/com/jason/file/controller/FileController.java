package com.jason.file.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.jason.file.service.FileService;

@Controller
public class FileController {

	@Autowired
	private FileService fileService;
	
	/**
	 * 获取file.html页面
	 */
	@RequestMapping("file")
	public String file() {
		return "/file";
	}

	/**
	 * 实现文件上传
	 */
	@RequestMapping("fileUpload")
	@ResponseBody
	public String fileUpload(@RequestParam("fileName") MultipartFile file) {
		return fileService.fileUpload(file);
	}

	/**
	 * 获取multifile.html页面
	 */

	@RequestMapping("multifile")
	public String multifile() {
		return "/multifile";
	}

	/**
	 * 实现多文件上传
	 */
	@RequestMapping(value = "multifileUpload", method = RequestMethod.POST)
	public @ResponseBody String multifileUpload(HttpServletRequest request) {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fileName");
		return fileService.fileUpload(files);
	}
	
	@RequestMapping("fileDownload")
	public ModelAndView fileDownload(Model model) {
		model.addAttribute("u_list_info", fileService.getFiles());
		return new ModelAndView("/download", "umodel", model);
	}
	
	@RequestMapping("download")
	public void downLoad(HttpServletResponse response, String filename) {
		fileService.downLoad(response, filename);
	}
	
	@RequestMapping("office")
	public String office() {
		return "/office";
	}
	
	@RequestMapping("office2PDF")
	@ResponseBody
	public String office2PDF(@RequestParam("fileName") MultipartFile file) {
		fileService.fileUpload(file);
		return fileService.office2PDF(file);
	}
}