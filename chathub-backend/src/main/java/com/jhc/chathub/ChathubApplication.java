package com.jhc.chathub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class ChathubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChathubApplication.class, args);
	}

}
