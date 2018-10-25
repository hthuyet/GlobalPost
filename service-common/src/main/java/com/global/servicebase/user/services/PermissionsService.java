package com.global.servicebase.user.services;

import com.global.core.SsdcCrudService;
import com.global.jdbc.factories.RepositoryFactory;
import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.global.servicebase.user.model.Permission;
import com.global.servicebase.user.model.PermissionSearchForm;
import com.global.servicebase.utils.CommonService;
import com.global.utils.ObjectUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ThuyetLV
 */
@Service
public class PermissionsService extends SsdcCrudService<Long, Permission> {

    @Autowired
    private CommonService commonService;
    @Autowired
    private RoleService roleService;

    @Autowired
    public PermissionsService(RepositoryFactory repositoryFactory) {
        this.repository = repositoryFactory.create(Permission.class);
    }

    public List<Permission> searchPermission(String limit, String indexPage) {
        List<Permission> permissionArrayList = new ArrayList<Permission>();
        Page<Permission> all = this.repository.findAll(new PageRequest(Integer.parseInt(indexPage), Integer.parseInt(limit)));
        permissionArrayList = all.getContent();
        return permissionArrayList;
    }

    public int checkGroupName(String addGroupName, String addName) {
        String whereExp = "group_name=? and name=?";
        return this.repository.search(whereExp, addGroupName, addName).size();
    }

    public List<Permission> getByNameSC(String name) {
        List<Permission> search = this.repository.search("name=?", name);
        return search;
    }

    public boolean getByName(String name) {
        int count = this.repository.search("name=?", name).size();
        if (count == 0) {
            return false;
        }
        return true;
    }

    public List<Permission> findByQuery(String query, Integer index, Integer limit) {
        return this.repository.search(query, new PageRequest(index, limit)).getContent();
    }

    public Page<Permission> getPage(int page, int limit) {
        return this.repository.findAll(new PageRequest(page, limit));
    }

    public List<Permission> findByQuery(String query) {
        return this.repository.search(query);
    }

    private Set<String> generateConditionForSearch(PermissionSearchForm searchForm) {
        Set<String> conditions = new HashSet<>();
        if (!Strings.isNullOrEmpty(searchForm.name)) {
            conditions.add(String.format(" name like '%s' ", commonService.generateSearchLikeInput(searchForm.name)));
        }
        if (!Strings.isNullOrEmpty(searchForm.description)) {
            conditions.add(String.format(" description like '%s' ", commonService.generateSearchLikeInput(searchForm.description)));
        }
        if (!Strings.isNullOrEmpty(searchForm.groupName)) {
            conditions.add(String.format(" group_name like '%s' ", commonService.generateSearchLikeInput(searchForm.groupName)));
        }
        if (searchForm.operationIds != null) {
            for (String permissionID : searchForm.operationIds) {
                conditions.add(String.format(" operation_ids like '%s' ", commonService.generateSearchLikeInput(permissionID)));
            }
        }
        return conditions;
    }

    public List<Permission> search(PermissionSearchForm searchForm) {
        List<Permission> data = new ArrayList<>();
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
        return data;

    }

    public int count(PermissionSearchForm searchForm) {
        searchForm.limit = null;
        searchForm.page = null;
        List<Permission> data = search(searchForm);
        return data.isEmpty() ? 0 : data.size();
    }

    public List<Permission> getByName(PermissionSearchForm searchParameter) {
        List<Permission> byName = getByNameSC(searchParameter.name);
        return byName;
    }

    @Override
    public void afterUpdate(Permission oldEntity, Permission newEntity) {
        if (!ObjectUtils.equals(oldEntity.operationIds, newEntity.operationIds)) {
            roleService.updateRoleByPermission(newEntity);
        }
    }
}
