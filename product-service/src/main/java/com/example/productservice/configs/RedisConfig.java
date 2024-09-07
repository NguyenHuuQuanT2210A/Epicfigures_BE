package com.example.productservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
//        redisStandaloneConfiguration.setPassword("12345678");
        redisStandaloneConfiguration.setPort(redisPort);

        JedisPoolConfig p = new JedisPoolConfig();
        p.setTestWhileIdle(true);
        p.setMinEvictableIdleTime(Duration.ofMillis(60000));
        p.setTimeBetweenEvictionRuns(Duration.ofMillis(30000));

        JedisClientConfiguration.JedisClientConfigurationBuilder  jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.usePooling().poolConfig(p);
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(60000));
        jedisClientConfiguration.readTimeout(Duration.ofMillis(60000));
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
    }

    @Bean
    <K, V> RedisTemplate<K, V> redisTemplate(ObjectMapper objectMapper) {
        RedisTemplate<K, V> template = new RedisTemplate<>();
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    <K, F, V> HashOperations<K, F, V> hashOperations(RedisTemplate<K, V> redisTemplate){
        return redisTemplate.opsForHash();
    }
}
