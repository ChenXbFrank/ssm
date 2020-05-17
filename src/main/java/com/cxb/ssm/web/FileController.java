package com.cxb.ssm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
@RequestMapping("/file")
public class FileController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/filePage", method = RequestMethod.GET)
	private String filePage() {
		return "upload";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public String upload(MultipartFile file, HttpServletRequest request) throws IOException {
		String path = request.getSession().getServletContext().getRealPath("upload");
		String fileName = file.getOriginalFilename();
		File dir = new File(path,fileName);
		if(!dir.exists()){
			dir.mkdirs();
		}

		file.transferTo(dir);
		logger.info("文件上传.");
		return fileName;
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void down(HttpServletRequest request, HttpServletResponse response) throws Exception{

		String fileName = request.getSession().getServletContext().getRealPath("upload")+"/bg_img.png";

		InputStream bis = new BufferedInputStream(new FileInputStream(new File(fileName)));

		String filename = "下载文件.jpg";

		filename = URLEncoder.encode(filename,"UTF-8");

		response.addHeader("Content-Disposition", "attachment;filename=" + filename);

		response.setContentType("multipart/form-data");

		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		int len = 0;
		while((len = bis.read()) != -1){
			out.write(len);
			out.flush();
		}
		logger.info("文件下载.");
		out.close();
	}

}
