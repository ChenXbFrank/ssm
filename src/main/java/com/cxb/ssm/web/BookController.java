package com.cxb.ssm.web;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cxb.ssm.entity.Book;
import com.cxb.ssm.exception.NoNumberException;
import com.cxb.ssm.exception.RepeatAppointException;
import com.cxb.ssm.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.cxb.ssm.dto.AppointExecution;
import com.cxb.ssm.dto.Result;
import com.cxb.ssm.enums.AppointStateEnum;

@Controller
@RequestMapping("/book") // url:/模块/资源/{id}/细分 /seckill/list
public class BookController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private String list(Model model) {
		List<Book> list = bookService.getList();
		model.addAttribute("list", list);
		logger.info("获取所有的图书{};", list);
		return "list";
	}

	@RequestMapping(value = "/detail/{bookId}", method = RequestMethod.GET)
	private String detail(@PathVariable("bookId") Long bookId, Model model) {
		if (bookId == null) {
			return "redirect:/book/list";
		}
		Book book = bookService.getById(bookId);
		logger.info("查询出的图书{};", book);
		if (book == null) {
			return "forward:/book/list";
		}
		model.addAttribute("book", book);
		return "detail";
	}

	// ajax json
	@RequestMapping(value = "/appoint", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ResponseBody
	private Result<AppointExecution> appoint(@RequestParam("bookId") Long bookId, @RequestParam("studentId") Long studentId) {
		if (studentId == null || studentId.equals("")) {
			return new Result<AppointExecution>(false, "学号不能为空");
		}
		AppointExecution execution = null;
		try {
			execution = bookService.appoint(bookId, studentId);
		} catch (NoNumberException e1) {
			execution = new AppointExecution(bookId, AppointStateEnum.NO_NUMBER);
		} catch (RepeatAppointException e2) {
			execution = new AppointExecution(bookId, AppointStateEnum.REPEAT_APPOINT);
		} catch (Exception e) {
			execution = new AppointExecution(bookId, AppointStateEnum.INNER_ERROR);
		}
		return new Result<AppointExecution>(true, execution);
	}

	@Autowired
	private RedisTemplate redisTemplate;
	/**
	 * 这里就是测试redis
	 */
	@RequestMapping(value = "/redis", method = RequestMethod.GET)
	private String redis(Model model) {
		Book book = (Book) redisTemplate.opsForValue().get("book");
		if (book == null){
			book = bookService.getById(1001);
			logger.info("从数据库获取id为1001的图书{};", book);
			// 将数据设置到缓存中 30秒的有效期
			redisTemplate.opsForValue().set("book",book,30, TimeUnit.SECONDS);
		}else {
			logger.info("从redis缓存中获取id为1001的图书{};", book);
		}
		model.addAttribute("book", book);
		return "detail";
	}

	@RequestMapping(value = "/redisList", method = RequestMethod.GET)
	private String redisList(Model model) {
		List<Book> list = (List<Book>) redisTemplate.opsForValue().get("book_list");
		if (list == null){
			list = bookService.getList();
			logger.info("从数据库获取所有的图书{};", list);
			// 将数据设置到缓存中 30秒的有效期
			redisTemplate.opsForValue().set("book_list",list,30, TimeUnit.SECONDS);
		}else {
			logger.info("从redis缓存中获取所有的图书{};", list);
		}
		model.addAttribute("list", list);
		return "list";
	}

}
