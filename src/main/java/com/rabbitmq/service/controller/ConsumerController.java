package com.rabbitmq.service.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.rabbitmq.service.config.MqConfig;
import com.rabbitmq.service.model.CustomMessage;

@RestController
public class ConsumerController {

	private List<SseEmitter> lsEmitters = new ArrayList<SseEmitter>();
	
	@CrossOrigin
	@RequestMapping("/subscribe")
	public SseEmitter stream()
	{
		SseEmitter emitter = new SseEmitter((long) 1800000);
		lsEmitters.add(emitter);
		
//		emitter.onCompletion(()->lsEmitters.remove(emitter));
//		emitter.onTimeout(()->lsEmitters.remove(emitter));
//		
		
		return emitter;
	}
	
	@RabbitListener(queues=MqConfig.QUEUEID)
	public void listener(CustomMessage message) {
		System.out.println(message);
		
		List<SseEmitter> deadEmitters = new ArrayList<SseEmitter>();
	    this.lsEmitters.forEach(emitter -> {
	      try {
	        emitter.send(message);
	      }
	      catch (Exception e) {
	    	  System.out.println("Error "+e);
	        deadEmitters.add(emitter);
	      }
	    });

	    this.lsEmitters.removeAll(deadEmitters);
	}
	
}
