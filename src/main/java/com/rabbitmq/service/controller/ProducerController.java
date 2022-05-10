package com.rabbitmq.service.controller;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.service.model.CustomMessage;
import com.rabbitmq.service.config.MqConfig;

@RestController
public class ProducerController {
	
	@Autowired
	private RabbitTemplate template;

	@PostMapping("/publish")
	public String publishMessage(@RequestBody CustomMessage message) {
		message.setId(UUID.randomUUID().toString());
		message.setMessageDate(new Date());
		template.convertAndSend(MqConfig.EXCHANGEID,MqConfig.ROUTINGKEY,message);
		return "message sent successfully";
	}
}
