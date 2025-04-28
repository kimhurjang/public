package com.example.mhbc;

import com.example.mhbc.dto.MemberMapper;
import com.example.mhbc.entity.MemberEntity;
import com.example.mhbc.mapper.CustomMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MhbcApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	CustomMapper customMapper;

	@Test
	void test11() {
		List<MemberEntity> list = customMapper.selectId("");

			for (MemberEntity m : list) {
				System.out.print(m.getIdx());
				System.out.print(" | ");
				System.out.print(m.getName());
				System.out.print(" | ");
				System.out.println(m.getUserid());
			}
		}

	}

