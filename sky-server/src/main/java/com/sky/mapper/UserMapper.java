package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * desc
 *
 * @Author itcast
 * @Create 2024/6/15
 **/
@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time)\n" +
            "        values (#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insert(User user);


    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    /**
     * 统计在一定日期范围的用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map<String, Object> map);
}
