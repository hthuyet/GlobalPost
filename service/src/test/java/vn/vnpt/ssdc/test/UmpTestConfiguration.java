package vn.vnpt.ssdc.test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.jvm.HsqlConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import vn.vnpt.ssdc.core.ObjectCache;
import vn.vnpt.ssdc.core.RedisCache;
import vn.vnpt.ssdc.jdbc.factories.RepositoryFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by THANHLX on 4/11/2017.
 */
@Configuration
public class UmpTestConfiguration {
    @Bean
    public RepositoryFactory repositoryFactory() throws ClassNotFoundException, SQLException, LiquibaseException {
        ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
        Class.forName("org.hsqldb.jdbcDriver");
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:test","sa","");

        HsqlConnection hsqlConnection = new HsqlConnection(connection);
        Liquibase liquibase = new Liquibase("src/test/resources/liquibase/test.db.changelog-master.xml",resourceAccessor,hsqlConnection);
        liquibase.dropAll();
        liquibase.update("test");
        connection.close();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.hsqldb.jdbcDriver");
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("");
        hikariConfig.setJdbcUrl("jdbc:hsqldb:mem:test");
        DataSource dataSource = new HikariDataSource(hikariConfig);

        return new RepositoryFactory(dataSource);
    }
}
