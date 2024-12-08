package com.cpxHelper.myPatients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.cpxHelper.myPatients.domain.entity")
public class MypsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MypsBackendApplication.class, args);
	}

}
