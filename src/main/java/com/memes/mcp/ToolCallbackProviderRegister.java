package com.memes.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolCallbackProviderRegister {

    @Bean
    public ToolCallbackProvider mediaTools(MediaService mediaService) {
        return MethodToolCallbackProvider.builder().toolObjects(mediaService).build();
    }
}
