package vn.vnpt.ssdc.test.role;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import vn.vnpt.ssdc.test.UmpTestConfiguration;
import vn.vnpt.ssdc.user.model.Role;
import vn.vnpt.ssdc.user.services.RoleService;
import vn.vnpt.ssdc.jdbc.factories.RepositoryFactory;

import java.util.List;

/**
 * Created by Lamborgini on 6/2/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UmpTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class TesRoleService {
    @Autowired
    RepositoryFactory repositoryFactory;

    @Test
    public void testSearchRole() {
        RoleService roleService = new RoleService(repositoryFactory);
        Role role = new Role();
        role.id = 1L;
        roleService.create(role);
        List<Role> roles = roleService.searchRole("1", "20");
        Assert.assertNotNull(roles.size());
    }

    @Test
    public void testCheckName() {
        RoleService roleService = new RoleService(repositoryFactory);
        Role role = new Role();
        role.id = 2L;
        role.name = "test";
        roleService.create(role);
        int test = roleService.checkName("test");
        Assert.assertNotNull(test);
    }
}
