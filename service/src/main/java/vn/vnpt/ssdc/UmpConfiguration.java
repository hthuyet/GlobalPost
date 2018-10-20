package vn.vnpt.ssdc;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import java.io.IOException;
import java.util.Properties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import vn.vnpt.ssdc.jdbc.factories.RepositoryFactory;

import javax.sql.DataSource;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;
import vn.vnpt.ssdc.core.ObjectCache;
import vn.vnpt.ssdc.core.RedisCache;
import vn.vnpt.ssdc.event.AMQPService;
import vn.vnpt.ssdc.event.EventBus;
import vn.vnpt.ssdc.event.amqp.AMQPEventBus;

/**
 * Created by vietnq on 10/25/16.
 */
@Configuration
//@EnableKafka
//@EnableAsync
@EnableAspectJAutoProxy
public class UmpConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setIgnoreUnresolvablePlaceholders(true);
        return c;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

//    @Bean
//    public RepositoryFactory repositoryFactory(DataSource dataSource) {
//        return new RepositoryFactory(dataSource);
//    }
    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public ObjectCache ssdcCache(RedisTemplate redisTemplate) {
        return new RedisCache(redisTemplate);
    }

    @Bean
    public RepositoryFactory repositoryFactory(DataSource dataSource, ObjectCache objectCache) {
        return new RepositoryFactory(dataSource, objectCache);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /* ========================= RABBIT MQ ============================ */
    @Bean
    @ConfigurationProperties(prefix = "spring.rabbitmq")
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cnn = new CachingConnectionFactory();
        System.out.println("-------cnn: " + cnn.getHost() + " - " + cnn.getUsername());
        return cnn;
    }

    @Bean
    public EventBus eventBus(CachingConnectionFactory connectionFactory, ApplicationContext ctx) {
        EventBus eventBus = new AMQPEventBus(connectionFactory, ctx);
        return eventBus;
    }

    @Bean
    public AMQPService amqpService(ApplicationContext ctx) {
        return new AMQPService(ctx);
    }

    @Bean
    public JobFactory jobFactory(ApplicationContext ctx) {
        QuartzJobFactory sampleJobFactory = new QuartzJobFactory();
        sampleJobFactory.setApplicationContext(ctx);
        return sampleJobFactory;
    }

    @Bean
    Scheduler scheduler(ApplicationContext ctx) {
        try {
            Properties prop = new Properties();
            Resource resource = ctx.getResource("classpath:quartz.properties");
            prop.load(resource.getInputStream());
            if (prop.size() <= 0) {
                return null;
            }
            Scheduler scheduler = new StdSchedulerFactory(prop).getScheduler();
            scheduler.setJobFactory(jobFactory(ctx));
            scheduler.start();
            return scheduler;
        } catch (IOException | SchedulerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Value("${elasticSearchUrl}")
    public String elasticSearchUrl;

    @Bean
    public JestClient elasticSearchClient() {
        if (elasticSearchUrl == null || elasticSearchUrl.trim().length() <= 0) {
            return null;
        }
        JestClientFactory jestClientFactory = new JestClientFactory();
        jestClientFactory.setHttpClientConfig(new HttpClientConfig.Builder(elasticSearchUrl)
                .multiThreaded(true)
                .build());
        return jestClientFactory.getObject();
    }
}
