package com.zzc.init.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplateWithProxy() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        // 配置代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7897)); // 替换为代理地址和端口
        requestFactory.setProxy(proxy);

        return new RestTemplate(requestFactory);
    }
}
