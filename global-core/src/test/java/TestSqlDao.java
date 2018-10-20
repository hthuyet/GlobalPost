import org.junit.Assert;
import org.junit.Test;
import com.global.jdbc.SsdcSqlDao;

import java.util.List;

/**
 * Created by quocvi3t on 1/20/17.
 */

public class TestSqlDao extends BaseTest {

    protected SsdcSqlDao<Demo,Long> demoDao;

    @Test
    public void testCreate() {
        demoDao = new SsdcSqlDao<>(dataSource,Demo.class);
        Demo entity = new Demo();
        entity.name = "Test";
        entity = demoDao.insert(entity);
        List<Demo> demos = demoDao.findAll();
        Assert.assertEquals(1,demos.size());
    }

    @Test
    public void testUpdate() {
        demoDao = new SsdcSqlDao<>(dataSource,Demo.class);
        Demo entity = new Demo();
        entity.name = "Test Update";
        entity = demoDao.insert(entity);

        entity.name = "updated";
        entity = demoDao.update(entity);

        Demo updatedEntity = demoDao.findOne(entity.id);
        Assert.assertEquals("updated",updatedEntity.name);
    }
}
