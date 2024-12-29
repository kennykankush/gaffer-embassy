package ai.lumidah.gaffer.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    
    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.username}")
    private String redisUsername;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration rsc = new RedisStandaloneConfiguration();
        rsc.setHostName(redisHost);
        rsc.setPort(redisPort);

        if(redisUsername.trim().length() > 0) {
            rsc.setUsername(redisUsername);
            rsc.setPassword(redisPassword);
        }

        JedisClientConfiguration jcc = JedisClientConfiguration.builder().build();
        JedisConnectionFactory jcf = new JedisConnectionFactory(rsc, jcc);
        jcf.afterPropertiesSet();

        return jcf;
    }

    @Bean
    @Qualifier("StringTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
    
        StringRedisSerializer serializer = new StringRedisSerializer();
    
        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
    
        return template;
    }
    
    @Bean
    @Qualifier("jsonTemplate")
    public RedisTemplate<String, Object> postRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
    
        StringRedisSerializer serializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
    
        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
    
        return template;
    }
    
}