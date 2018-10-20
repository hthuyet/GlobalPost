package vn.vnpt.ssdc.test.permission;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import vn.vnpt.ssdc.test.UmpTestConfiguration;
import vn.vnpt.ssdc.user.model.Permission;
import vn.vnpt.ssdc.user.services.PermissionsService;
import vn.vnpt.ssdc.jdbc.factories.RepositoryFactory;

import java.util.List;

/**
 * Created by Lamborgini on 6/2/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UmpTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class TestPermissionService {
    @Autowired
    RepositoryFactory repositoryFactory;

    @Test
    public void testSearchPermission() {
        PermissionsService permissionsService = new PermissionsService(repositoryFactory);
        Permission permission = new Permission();
        permission.id = 1L;
        permission.groupName = "test";
        permissionsService.create(permission);
        List<Permission> permissions = permissionsService.searchPermission("20", "1");
        Assert.assertNotNull(permissions.size());
    }

    @Test
    public void testCheckGroupName() {
        PermissionsService permissionsService = new PermissionsService(repositoryFactory);
        Permission permission = new Permission();
        permission.id = 2L;
        permission.groupName = "groupName";
        permission.name = "name";
        permissionsService.create(permission);
        int i = permissionsService.checkGroupName("groupName", "name");
        Assert.assertNotNull(i);
    }

    @Test
    public void testGetByName() {
        PermissionsService permissionsService = new PermissionsService(repositoryFactory);
        Permission permission = new Permission();
        permission.id = 3L;
        permission.groupName = "groupName";
        permission.name = "name";
        permissionsService.create(permission);
        boolean check = permissionsService.getByName( "name");
        Assert.assertNotNull(check);
    }
}
