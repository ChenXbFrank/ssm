package com.cxb.ssm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

@Controller
@RequestMapping("/file")
public class FileController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/filePage", method = RequestMethod.GET)
	private String filePage() {
		return "upload";
	}

    @RequestMapping(value = "/sendEmail", method = RequestMethod.GET)
    private String sendEmail() {
        return "sendEmail";
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

	@RequestMapping(value = "/send", method = RequestMethod.POST)
	@ResponseBody
	public void send(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();//必须定义在37,38行设置编码格式后边，否则输出中文为？？？
		String sender = request.getParameter("sender");
		String password = request.getParameter("password");
		String receiver = request.getParameter("receiver");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		//生成SMTP的主机名称
		int n = sender.indexOf("@");
		int m = sender.length();
		String mailserver = "smtp." + sender.substring(n + 1, m);
		//建立邮件会话
		Properties pro = new Properties();
		pro.put("mail.smtp.host", mailserver);
		pro.put("mail.smtp.auth", true);
		Session sess = Session.getInstance(pro);
		sess.setDebug(true);
		MimeMessage message = new MimeMessage(sess);//新建一个消息对象
		try {
			//设置发件人
			InternetAddress sender_address = new InternetAddress(sender);
			message.setFrom(sender_address);
			//设置收件人
			InternetAddress receiver_address = new InternetAddress(receiver);
			message.setRecipient(javax.mail.Message.RecipientType.TO, receiver_address);
//			message.add
			message.setSubject(subject);//设置主题
			message.setText(content);//设置内容  不是setContent
			message.setSentDate(new Date());//设置发送时间
			//发送邮件
			System.out.println("地址为：" + mailserver + " " + sender + " " + password);
			message.saveChanges();//保证报头域同会话内容保持一致
			Transport transport = sess.getTransport("smtp");
			transport.connect(mailserver, sender, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			out.print("<script>alert('邮件已发送！');window.location.href='Index.jsp';</script>");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发送邮件产生的错误：" + e.getMessage());
			out.print("<script>alert('邮件发送失败！');window.location.href='Index.jsp';</script>");
		}
	}
}
