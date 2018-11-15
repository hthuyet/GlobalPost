package com.global.webapp.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.global.webapp.clients.OperationClient;
import com.global.webapp.clients.PermissionClient;
import com.global.webapp.clients.RoleClient;
import com.global.webapp.clients.UserClient;
import com.global.webapp.models.Permissions;
import com.global.webapp.models.Role;
import com.global.webapp.models.User;
import com.global.webapp.models.searchForm.RoleSearchForm;
import com.global.webapp.models.searchForm.UserSearchForm;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class RoleController {

    private static final String ROLES_PAGE = "roles/roles";
    private static final String ROLES_PAGE_ADD = "roles/roles_add_edit";

    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    HttpSession session;

    @Autowired
    RoleClient roleClient;

    @Autowired
    PermissionClient permissionsClient;

    @Autowired
    OperationClient operationClient;

    @Autowired
    UserClient userClient;

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ONE:ROLE:READ')")
    public String index() {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to roles page", "", "");
        return ROLES_PAGE;
    }

    @GetMapping("/roles/role-add")
    @PreAuthorize("hasAuthority('ONE:ROLE:CREATE')")
    public String indexAdd() {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to create new roles page", "", "");
        return ROLES_PAGE_ADD;
    }

    @GetMapping("/roles/role-edit/{roleId}")
    @PreAuthorize("hasAuthority('ONE:ROLE:UPDATE')")
    public String indexEdit(Model model, @PathVariable("roleId") String roleId) {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to create new role page", "", "");

        Role role = roleClient.get(roleId);

        model.addAttribute("name", role.name);
        model.addAttribute("operationIds", role.operationIds);
        model.addAttribute("id", role.id);
        model.addAttribute("description", role.description);
        model.addAttribute("permissionsIds", role.permissionsIds);

        return ROLES_PAGE_ADD;
    }

    @GetMapping("/roles/search")
    @PreAuthorize("hasAuthority('ONE:ROLE:READ')")
    @ResponseBody
    public Map<String, Object> getListPer(@RequestParam Map<String, String> params) {
        Map<String, Object> response = new HashMap<>();

        RoleSearchForm roleSearchForm = new RoleSearchForm();
        roleSearchForm.limit = Integer.valueOf(params.get("limit"));
        roleSearchForm.page = Integer.valueOf(params.get("page"));

        if (params.get("roleName") != null && !params.get("roleName").isEmpty()) {
            roleSearchForm.name = params.get("roleName");
        }

        Role[] search = roleClient.search(roleSearchForm);
        int count = roleClient.count(roleSearchForm);

        response.put("roles", search);
        response.put("totalElements", count);
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search role on list roles page", "", "");
        return response;
    }

    @GetMapping("/roles/load-permission")
    @PreAuthorize("hasAuthority('ONE:ROLE:READ')")
    @ResponseBody
    public String loadOperation() {
        String response = loadDataPermission();
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "loadPermission", "", "");
        return response;
    }

    @PostMapping("/addRole")
    @PreAuthorize("hasAuthority('ONE:ROLE:CREATE')")
    @ResponseBody
    public String addRole(@RequestBody Map<String, String> params) {
        String result = "";
        if (params.size() > 0) {
            try {

                boolean resultName = checkExist(params.get("addNameRole"), "");
                if (resultName) {
                    result = "201";
                } else {
                    Role role = initRole(params, "");
                    Role role1 = roleClient.create(role);
                    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "create new role", "", "");
                    Set<Long> setPermission = role1.getPermissionsIds();
                    Set<String> setOperation = new HashSet<>();
                    for (Long l : setPermission) {
                        Permissions permissions = permissionsClient.getByID(l);
                        String operationIds = permissions.getOperationIds().toString().replaceAll("\\s+", "");
                        String[] operationIdsArray = operationIds.substring(1, operationIds.length() - 1).trim().split(",");
                        Collections.addAll(setOperation, operationIdsArray);
                    }
                    role1.setOperationIds(setOperation);
                    roleClient.update(role1, role1.getId());

                    result = "200";
                }

            } catch (Exception e) {
                result = "400";
            }

        }
        return result;
    }

    @PostMapping("/editRole")
    @PreAuthorize("hasAuthority('ONE:ROLE:UPDATE')")
    @ResponseBody
    public String editRole(@RequestBody Map<String, String> params) {
        String result = "";
        if (params.size() > 0) {
            try {

                boolean resultName = checkExist(params.get("addNameRole"), params.get("addId"));

                if (resultName) {
                    result = "201";
                } else {
                    Role role = initRole(params, params.get("addId"));
                    roleClient.update(role, Long.valueOf(params.get("addId")));
                    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "update role " + params.get("addId"), "", "");
                    updateRoleName(params.get("addId"));
                    result = "200";
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "400";
            }

        }
        return result;
    }

    @GetMapping("/deleteRole")
    @PreAuthorize("hasAuthority('ONE:ROLE:DELETE')")
    @ResponseBody
    public String deleteRole(@RequestParam Map<String, String> params) {
        String result = "";
        if (params.keySet().contains("id")) {
            String ids = params.get("id");
            JsonArray array = new Gson().fromJson(ids, JsonArray.class);
            try {
                int deleteOk = 0;
                for (int i = 0; i < array.size(); i++) {
                    UserSearchForm userSearchForm = new UserSearchForm();
                    Set<Integer> roles = new HashSet<Integer>();
                    roles.add(array.get(i).getAsInt());
                    userSearchForm.roles = roles;
                    int totalItem = userClient.search(userSearchForm).length;
                    if (totalItem == 0) {
                        roleClient.delete(array.get(i).getAsLong());
                        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "delete role " + array.get(i).getAsString(), "", "");
                        deleteOk++;
                    }
                }
                if (deleteOk == array.size()) {
                    result = "200";
                } else {
                    result = "300";
                }
            } catch (Exception e) {
                result = "400";
            }
        }
        return result;
    }

    private Role initRole(Map<String, String> params, String roleID) {
        Role role = new Role();
        if (!roleID.equals("")) {
            role.setId(Long.valueOf(roleID));
        }
        role.setName(params.get("addNameRole"));
        role.setDescription(params.get("addDescriptionRole"));
        String[] addPermissions = params.get("addPermission").replace("*", "").split("@");

        Long[] addPermissionsLong = new Long[addPermissions.length];
        String[] addOperations = new String[addPermissions.length];

        try {
            for (int i = 0; i < addPermissions.length; i++) {
                String[] split = addPermissions[i].split("-");
                addPermissionsLong[i] = Long.valueOf(split[0]);
                addOperations[i] = split[1].replace("[", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<Long> setAddPermissions = new HashSet<>();
        Collections.addAll(setAddPermissions, addPermissionsLong);
        role.setPermissionsIds(setAddPermissions);
        Set<String> setAddOperations = new HashSet<>();
        Collections.addAll(setAddOperations, addOperations);
        role.setOperationIds(setAddOperations);
        return role;
    }

    private String loadDataPermission() {
        Permissions[] permissions = permissionsClient.findAll();
        List<Permissions> permissionList = new ArrayList<Permissions>();
        for (Permissions permission : permissions) {
            permissionList.add(permission);
        }
        JsonArray jsonArray = new JsonArray();
        for (Permissions permission : permissionList) {
            List<String> listjsonObj = new ArrayList<String>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                String asString = asJsonObject.get("title").getAsString();
                listjsonObj.add(asString);
            }
            if (listjsonObj.contains(permission.getName())) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : asJsonObject.entrySet()) {
                        if (entry.getKey().equals("key") && entry.getValue().toString().replace("\"", "").equals(permission.getName())) {
                            JsonArray jsonArray1 = asJsonObject.getAsJsonArray("children");
                            JsonObject jsonObject1 = new JsonObject();
                            jsonObject1.addProperty("title", permission.getName());
                            jsonObject1.addProperty("key", permission.getId() + "-" + permission.getOperationIds());
                            jsonArray1.add(jsonObject1);
                        }
                    }
                }
            } else {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("title", permission.getName());
                jsonObject.addProperty("key", permission.getName());
                JsonArray jsonArrayChildren = new JsonArray();
                jsonArrayChildren = loadDataPermissionChildren(permission);
                jsonObject.add("children", jsonArrayChildren);
                jsonArray.add(jsonObject);
            }

        }

        return jsonArray.toString();
    }

    private JsonArray loadDataPermissionChildren(Permissions permission) {
        JsonArray jsonArrayChildren = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", permission.getName());
        jsonObject.addProperty("key", permission.getId() + "-" + permission.getOperationIds());
        jsonArrayChildren.add(jsonObject);
        return jsonArrayChildren;
    }

    private void updateRoleName(String roleID) {

        Role role = roleClient.get(roleID);
        Set<Long> setPermission = role.getPermissionsIds();
        Set<String> setOperation = new HashSet<>();
        for (Long l : setPermission) {
            Permissions permissions = permissionsClient.getByID(l);
            String operationIds = permissions.getOperationIds().toString().replaceAll("\\s+", "");
            String[] operationIdsArray = operationIds.substring(1, operationIds.length() - 1).trim().split(",");
            Collections.addAll(setOperation, operationIdsArray);
        }
        role.setOperationIds(setOperation);
        roleClient.update(role, role.getId());

        UserSearchForm userSearchForm = new UserSearchForm();
        Set<Integer> roles = new HashSet<Integer>();
        roles.add(Integer.valueOf(roleID));
        userSearchForm.roles = roles;

        User[] users = userClient.search(userSearchForm);
        for (User user : users) {
            Set<String> setOperation1 = new HashSet<>();
            Set<String> setRoles = user.roleIds;
            for (String s : setRoles) {
                String lol = roleClient.get(s).getName();
                setOperation1.add(lol);
            }
            user.roleNames = setOperation1;
            userClient.update(String.valueOf(user.userName), user);
        }

    }

    private boolean checkExist(String addNameRole, String roleID) {
        RoleSearchForm roleSearchForm = new RoleSearchForm();
        roleSearchForm.name = addNameRole;

        Role[] search = roleClient.checkExistName(roleSearchForm);
        if (search.length > 0) {

            if (roleID.isEmpty()) {
                return true;
            } else {
                for (Role p : search) {
                    if (p.getId() == Long.valueOf(roleID)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
