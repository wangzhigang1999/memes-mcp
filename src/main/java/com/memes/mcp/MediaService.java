package com.memes.mcp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MediaService {
    private final MediaMapper mediaMapper;

    public MediaService(MediaMapper mediaMapper) {
        this.mediaMapper = mediaMapper;
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
            randomMeme = mediaMapper.getRandomMeme(adjustedLimit);
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
