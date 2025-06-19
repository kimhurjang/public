package com.example.mhbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

@SpringBootTest
class MhbcApplicationTests {


	@Test
	void contextLoads() {
		String uploadDir = System.getProperty("user.dir") + "/upload/data/";
		System.out.println(uploadDir);
//		File dir = new File(uploadDir);
//		if (!dir.exists()) {
//			dir.mkdirs();
//		}

	}

}
