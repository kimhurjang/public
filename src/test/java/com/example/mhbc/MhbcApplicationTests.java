package com.example.mhbc;

import com.example.mhbc.dto.MemberDTO;
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
	void test(){
		List<MemberEntity> List = customMapper.selectName("a");

		for(MemberEntity m : List){
			System.out.print(m.getName());
			System.out.print("|");
			System.out.print(m.getUserid());
			System.out.print("|");
			System.out.print(m.getStatus());
			System.out.println();

		}
	}

}
