import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.jvm.HsqlConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.junit.BeforeClass;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by quocvi3t on 1/23/17.
 */
public class BaseTest {
    protected static DataSource dataSource;
    protected static Liquibase liquibase;

    @BeforeClass
    public static void beforeClass() throws ClassNotFoundException, SQLException, LiquibaseException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.hsqldb.jdbcDriver");
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("");
        hikariConfig.setJdbcUrl("jdbc:hsqldb:mem:test");

        dataSource = new HikariDataSource(hikariConfig);

        ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
        Class.forName("org.hsqldb.jdbcDriver");
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:test","sa","");

        HsqlConnection hsqlConnection = new HsqlConnection(connection);
        liquibase = new Liquibase("src/test/resources/liquibase/test.db.changelog-master.xml",resourceAccessor,hsqlConnection);
        liquibase.dropAll();
        liquibase.update("test");
        connection.close();
    }
}
