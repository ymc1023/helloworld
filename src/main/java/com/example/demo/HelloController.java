package com.example.demo;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/hello")
	public Hello greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		
		return new Hello(counter.incrementAndGet(), String.format(template, name));
	}
	
	@GetMapping("/helloW")
	public Hello greetingH(@RequestParam(value = "name", defaultValue = "World") String name) {
		
		return new Hello(counter.incrementAndGet(), String.format(template, name));
	}


	@GetMapping("/helloY")
	public Hello greetingI(@RequestParam(value = "name", defaultValue = "World") String name) {
		
		return new Hello(counter.incrementAndGet(), String.format(template, name));
	}

	
}