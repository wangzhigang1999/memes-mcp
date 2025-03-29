package com.memes.mcp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MediaMapper extends BaseMapper<MediaContent> {
    /**
     * 使用 MOD 和时间戳获取随机记录
     * @param num 需要获取的记录数
     * @return 随机记录列表
     */
    @Select("SELECT * FROM media_content " +
            "WHERE MOD(id, 100) = MOD(UNIX_TIMESTAMP(), 100) " +
            "LIMIT #{num}")
    List<MediaContent> getRandomMeme(int num);
}
