package com.memes.mcp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaService extends ServiceImpl<MediaMapper, MediaContent> {
    public List<MediaContent> getRandomMediaContents(int num) {
        // 获取所有记录的数量
        long totalRecords = count();
        if (totalRecords == 0 || num <= 0) {
            return List.of();
        }
        // 计算需要查询的记录数
        int recordsToFetch = Math.min(num, (int) totalRecords);
        // 创建随机ID集合
        Random random = new Random();
        List<Long> randomIds = new ArrayList<>();
        while (randomIds.size() < recordsToFetch) {
            long randomId = (long) (random.nextDouble() * totalRecords + 1);
            if (!randomIds.contains(randomId)) {
                randomIds.add(randomId);
            }
        }
        // 使用QueryWrapper查询这些随机ID
        QueryWrapper<MediaContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", randomIds);
        return list(queryWrapper);
    }

    @Tool(description = "随机选取一些梗图；randomly select some memes")
    public String getRandomMeme(@ToolParam(description = "limit, 0 - 50") Integer limit) {
        // 如果 limit 为 null 或者超出范围，则默认为 10
        if (limit == null || limit < 0) {
            limit = 10;
        } else {
            limit = Math.min(limit, 50);
        }

        // 调整 limit 值，因为我们只取一半数量的结果
        int adjustedLimit = limit * 2;
        log.info("Fetching random meme content with adjusted limit: {}", adjustedLimit);

        List<MediaContent> randomMeme;
        try {
            randomMeme = getRandomMediaContents(adjustedLimit);
        } catch (Exception e) {
            log.error("Failed to fetch random meme content", e);
            return "An error occurred while fetching meme content.";
        }

        // 过滤并构建结果字符串
        String result = randomMeme.stream()
                .filter(Objects::nonNull)
                .filter(e -> e.getDataType().equals(MediaContent.DataType.IMAGE))
                .filter(e -> e.getStatus().equals(MediaContent.ContentStatus.APPROVED))
                .limit(limit) // 应用最终的 limit 限制
                .map(content -> "![](" + content.getDataContent() + ")\n")
                .collect(Collectors.joining());

        return result.isEmpty() ? "No approved images found." : result;
    }
}
