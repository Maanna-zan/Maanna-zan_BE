package com.hanghae99.maannazan.global.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hanghae99.maannazan.domain.kakaoapi.dto.KakaoResponseDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public ObjectMapper objectMapper(){


        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }

    @Bean
    public CacheManager testCacheManager(RedisConnectionFactory cf) {
        ObjectMapper objectMapper = objectMapper();
        Jackson2JsonRedisSerializer<PostResponseDto> postResponseDtoJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(PostResponseDto.class);
        postResponseDtoJackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        Jackson2JsonRedisSerializer<List<KakaoResponseDto>> listJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper.getTypeFactory().constructCollectionType(List.class, KakaoResponseDto.class));
        listJackson2JsonRedisSerializer.setObjectMapper(objectMapper);


        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(cf);
        builder.withCacheConfiguration("Alkol",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(60))
                        .disableCachingNullValues()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(listJackson2JsonRedisSerializer))
        );
        builder.withCacheConfiguration("Post",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(60))
                        .disableCachingNullValues()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(postResponseDtoJackson2JsonRedisSerializer))
        );
        return builder.build();
    }
}
