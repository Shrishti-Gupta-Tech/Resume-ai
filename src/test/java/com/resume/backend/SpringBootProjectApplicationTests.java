package com.resume.backend;

import com.resume.backend.service.ResumeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class SpringBootProjectApplicationTests {

	@Autowired
	private ResumeService resumeService;
	@Test
	void contextLoads() throws IOException {

		resumeService.generateResumeResponse("I am Shrishti Gupta with 1 year of Java Experience");
	}

}
