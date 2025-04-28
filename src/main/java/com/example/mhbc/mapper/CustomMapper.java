package com.example.mhbc.mapper;


import com.example.mhbc.dto.MemberMapper;

import com.example.mhbc.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomMapper {
    List<MemberEntity> selectAll();
    List<MemberEntity> selectId(String userid);
}
