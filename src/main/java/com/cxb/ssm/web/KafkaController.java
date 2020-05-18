package com.cxb.ssm.web;

import com.cxb.ssm.kafka.KafkaConsumerDemo;
import com.cxb.ssm.kafka.KafkaProducerDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class KafkaController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@Resource(name = "kafkaProducerDemo")
	KafkaProducerDemo producer;

	@Resource(name = "kafkaConsumerDemo")
	KafkaConsumerDemo consumer;

	@RequestMapping(value = "/welcome")
	public ModelAndView welcome() {
		logger.info("--------welcome--------");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("welcome");
		return mv;
	}

	@RequestMapping(value = "/sendmessage", method = RequestMethod.GET)
	public ModelAndView sendMessage() {
		logger.info("--------sendmessage--------");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(date);
		ModelAndView mv = new ModelAndView();
		mv.addObject("time", now);
		mv.setViewName("send");
		return mv;
	}

	@RequestMapping(value = "/onsend", method = RequestMethod.POST)
	public ModelAndView onsend(@RequestParam("message") String msg) {
		logger.info("--------onsend--------");
		producer.sendMessage(msg);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("welcome");
		return mv;
	}

	@RequestMapping(value = "/receive")
	public ModelAndView receive() {
		logger.info("--------receive--------");
		String msg = consumer.receive();
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", msg);
		mv.setViewName("receive");
		return mv;
	}

}
