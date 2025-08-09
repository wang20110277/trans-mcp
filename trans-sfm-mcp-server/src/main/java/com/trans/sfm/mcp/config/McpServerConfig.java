package com.trans.sfm.mcp.config;

import com.trans.sfm.mcp.api.IMcpServerEndpoint;
import org.noear.solon.Solon;
import org.noear.solon.ai.chat.tool.MethodToolProvider;
import org.noear.solon.ai.mcp.server.McpServerEndpointProvider;
import org.noear.solon.ai.mcp.server.annotation.McpServerEndpoint;
import org.noear.solon.ai.mcp.server.prompt.MethodPromptProvider;
import org.noear.solon.ai.mcp.server.resource.MethodResourceProvider;
import org.noear.solon.web.servlet.SolonServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Configuration
public class McpServerConfig {
    @PostConstruct
    public void start() {
        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcpserver.yml"});
    }

    @PreDestroy
    public void stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
    }

    @Bean
    public McpServerConfig init(List<IMcpServerEndpoint> serverEndpoints) {
        for (IMcpServerEndpoint serverEndpoint : serverEndpoints) {
            //这里注意一下，如果有代理的话需要用 AnnotationUtils 获取注解
            McpServerEndpoint anno = AnnotationUtils.findAnnotation(serverEndpoint.getClass(), McpServerEndpoint.class);

            if (anno == null) {
                continue;
            }

            McpServerEndpointProvider serverEndpointProvider = McpServerEndpointProvider.builder()
                    .from(serverEndpoint.getClass(), anno)
                    .build();

            serverEndpointProvider.addTool(new MethodToolProvider(serverEndpoint));
            serverEndpointProvider.addResource(new MethodResourceProvider(serverEndpoint));
            serverEndpointProvider.addPrompt(new MethodPromptProvider(serverEndpoint));

            serverEndpointProvider.postStart();

            //可以再把 serverEndpointProvider 手动转入 SpringBoot 容器
        }

        //为了能让这个 init 能正常运行
        return this;
    }

    @Bean
    public FilterRegistrationBean mcpServerFilter() {
        FilterRegistrationBean<SolonServletFilter> filter = new FilterRegistrationBean<>();
        filter.setName("SolonFilter");
        filter.addUrlPatterns("/mcp/*");
        filter.setFilter(new SolonServletFilter());
        return filter;
    }
}