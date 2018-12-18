package com.global.servicebase.user.services;

import com.global.core.SsdcCrudService;
import com.global.jdbc.factories.RepositoryFactory;
import com.global.servicebase.user.model.Permission;
import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.global.servicebase.user.model.Role;
import com.global.servicebase.user.model.RoleSearchForm;
import com.global.servicebase.user.model.User;
import com.global.servicebase.utils.CommonService;
import com.global.utils.ObjectUtils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ThuyetLV
 */
@Service
public class RoleService extends SsdcCrudService<BigInteger, Role> {

    @Autowired
    public UserService userService;

    @Autowired
    private CommonService commonService;

    @Autowired
    public RoleService(RepositoryFactory repositoryFactory) {
        this.repository = repositoryFactory.create(Role.class);
    }

    public List<Role> searchRole(String limit, String indexPage) {
        Page<Role> all = this.repository.findAll(new PageRequest(Integer.parseInt(indexPage), Integer.parseInt(limit)));
        return all.getContent();
    }

    public int checkName(String addName) {
        String whereExp = "name=?";
        return this.repository.search(whereExp, addName).size();
    }

    public List<Role> getByNameSC(String name) {
        List<Role> search = this.repository.search("name=?", name);
        return search;
    }

    public List<Role> checkByPermissionId(String permissionId) {
        List<Role> rtn = new ArrayList<>();
        List<Role> lstRole = this.repository.search("permissions_ids LIKE '%" + permissionId + "%'");

        for (Role role : lstRole) {
            role.id = new BigInteger("" + role.id);
        }

        Set<Long> permissionsIds;
        boolean find = false;
        for (Role role : lstRole) {
            find = false;
            permissionsIds = role.permissionsIds;
            if (permissionsIds != null && !permissionsIds.isEmpty()) {
                for (Long permissionsId : permissionsIds) {
                    if (permissionsId.toString().equalsIgnoreCase(permissionId)) {
                        find = true;
                        break;
                    }
                }
                if (find) {
                    rtn.add(role);
                }
            }
        }
        return rtn;
    }

    public List<Role> getListChildren(String username) {
        List<Role> childrenRoles = new ArrayList<>();
        try {
            User currentUser = userService.findByUserName(username);
            List<Role> roles = getAll();

            if (currentUser.roleNames.contains("SuperAdmin")) {
                childrenRoles = roles;
            } else {
                for (Role role : roles) {
                    if (currentUser.operationIds.containsAll(role.operationIds)) {
                        childrenRoles.add(role);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return childrenRoles;
    }

    public List<Role> findByQuery(String query, Integer index, Integer limit) {
        return this.repository.search(query, new PageRequest(index, limit)).getContent();
    }

    public Page<Role> getPage(int page, int limit) {
        return this.repository.findAll(new PageRequest(page, limit));
    }

    public List<Role> findByQuery(String query) {
        return this.repository.search(query);
    }

    private Set<String> generateConditionForSearch(RoleSearchForm searchForm) {
        Set<String> conditions = new HashSet<>();
        if (!Strings.isNullOrEmpty(searchForm.name)) {
            conditions.add(String.format(" name like '%s' ", commonService.generateSearchLikeInput(searchForm.name)));
        }
        if (!Strings.isNullOrEmpty(searchForm.description)) {
            conditions.add(String.format(" description like '%s' ", commonService.generateSearchLikeInput(searchForm.description)));
        }
        if (searchForm.permissionIds != null) {
            for (Long permissionID : searchForm.permissionIds) {
                conditions.add(String.format(" permissions_ids like '%s' ", commonService.generateSearchLikeInput(String.valueOf(permissionID))));
            }
        }
        return conditions;
    }

    public List<Role> search(RoleSearchForm searchForm) {
        List<Role> data = new ArrayList<>();
        if (searchForm.userName != null) {
            User currentUser = userService.findByUserName(searchForm.userName);
            List<Role> roles = getAll();
            if (currentUser.roleNames.contains("SuperAdmin")) {
                data = roles;
            } else {
                for (Role role : roles) {
                    if (currentUser.operationIds.containsAll(role.operationIds)) {
                        data.add(role);
                    }
                }
            }

        } else {
            Set<String> conditions = generateConditionForSearch(searchForm);
            if (searchForm.limit != null && searchForm.page != null) {
                if (!conditions.isEmpty()) {
                    String query = String.join(" AND ", conditions);
                    data = findByQuery(query, searchForm.page - 1, searchForm.limit);
                } else {
                    data = getPage(searchForm.page - 1, searchForm.limit).getContent();
                }
            } else {
                if (!conditions.isEmpty()) {
                    String query = String.join(" AND ", conditions);
                    data = findByQuery(query);
                } else {
                    data = getAll();
                }
            }
        }
        return data;

    }

    public int count(RoleSearchForm searchForm) {
        searchForm.limit = null;
        searchForm.page = null;
        List<Role> data = search(searchForm);
        return data.isEmpty() ? 0 : data.size();
    }

    public List<Role> getByName(RoleSearchForm searchParameter) {
        List<Role> byName = getByNameSC(searchParameter.name);
        return byName;
    }

    public int updateRoleByPermission(Permission permission) {
        int rtn = 0;
        List<Role> listRole = checkByPermissionId(permission.id.toString());
        if (listRole != null && !listRole.isEmpty()) {
            for (Role role : listRole) {
                rtn += userService.updateByRoleId(role.id.toString());
            }
        }
        return rtn;
    }

    @Override
    public void afterUpdate(Role oldEntity, Role newEntity) {
        if (!ObjectUtils.equals(oldEntity.permissionsIds, newEntity.permissionsIds)) {
            userService.updateByRoleId(newEntity.id.toString());
        }
    }
}
