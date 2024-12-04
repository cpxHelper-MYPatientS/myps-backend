package com.cpxHelper.myPatients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cpxHelper.myPatients"})
public class MypsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MypsBackendApplication.class, args);
	}

}
