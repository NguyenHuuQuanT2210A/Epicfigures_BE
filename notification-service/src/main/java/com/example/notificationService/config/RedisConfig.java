package com.example.notificationService.config;

import com.example.notificationService.service.NotificationEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.pubsub.topic.all}")
    private String topicNotificationAll;

    @Value("${redis.pubsub.topic.user}")
    private String topicNotificationUser;

    @Value("${redis.pubsub.topic.group}")
    private String topicNotificationGroup;

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
    RedisTemplate<String, Object> redisTemplate(ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public ChannelTopic channelTopicNotificationAll() {
        return new ChannelTopic(topicNotificationAll);
    }

    @Bean
    public MessageListenerAdapter messageListener(NotificationEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter messageListener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListener, channelTopicNotificationAll());
        container.addMessageListener(messageListener, new PatternTopic(topicNotificationUser + ":*"));
        container.addMessageListener(messageListener, new PatternTopic(topicNotificationGroup + ":*"));
        return container;
    }
}
