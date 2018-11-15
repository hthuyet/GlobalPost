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
import com.global.webapp.models.Operation;
import com.global.webapp.models.Permissions;
import com.global.webapp.models.Role;
import com.global.webapp.models.User;
import com.global.webapp.models.searchForm.PermissionSearchForm;
import com.global.webapp.models.searchForm.RoleSearchForm;
import com.global.webapp.models.searchForm.UserSearchForm;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class PermissionController {

    private static final String PER_PAGE = "permissions/permissions";
    private static final String PER_PAGE_ADD = "permissions/permissions_add_edit";

    private Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    HttpSession session;

    @Autowired
    OperationClient operationClient;

    @Autowired
    PermissionClient permissionsClient;

    @Autowired
    RoleClient roleClient;

    @Autowired
    UserClient userClient;

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('ONE:PERMISSION:READ')")
    public String index() {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to permissions page", "", "");
        return PER_PAGE;
    }

    @GetMapping("/permissions/permission-add")
    @PreAuthorize("hasAuthority('ONE:PERMISSION:CREATE')")
    public String indexAdd() {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to create new permission page", "", "");
        return PER_PAGE_ADD;
    }

    @GetMapping("/permissions/permission-edit/{perId}")
    @PreAuthorize("hasAuthority('ONE:PERMISSION:UPDATE')")
    public String indexEdit(Model model, @PathVariable("perId") String perId) {
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "go to create new permission page", "", "");

        Permissions byID = permissionsClient.getByID(Long.valueOf(perId));

        model.addAttribute("getName", byID.getName());
        model.addAttribute("getId", byID.getId());
        model.addAttribute("getOperationIds", byID.getOperationIds());
        model.addAttribute("getDescription", byID.getDescription());

        return PER_PAGE_ADD;
    }

    @GetMapping("/permissions/load-operation")
    @PreAuthorize("hasAuthority('ONE:PERMISSION:READ')")
    @ResponseBody
    public String loadOperation() {
        String response = loadDataOperation();
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "loadOperation", "", "");
        return response;
    }

    @GetMapping("/permissions/search")
    @PreAuthorize("hasAuthority('ONE:PERMISSION:READ')")
    @ResponseBody
    public Map<String, Object> getListPer(@RequestParam Map<String, String> params) {
        Map<String, Object> response = new HashMap<>();

        PermissionSearchForm permissionSearchForm = new PermissionSearchForm();
        permissionSearchForm.limit = Integer.valueOf(params.get("limit"));
        permissionSearchForm.page = Integer.valueOf(params.get("page"));

        if (params.get("permissionsName") != null && !params.get("permissionsName").isEmpty()) {
            permissionSearchForm.name = params.get("permissionsName");
        }

        Permissions[] permissionsList = permissionsClient.search(permissionSearchForm);
        int count = permissionsClient.count(permissionSearchForm);

        response.put("permissions", permissionsList);
        response.put("totalElements", count);
        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "search permissions on list permissions page", "", "");
        return response;
    }

    @PostMapping("/addPermission")
    @PreAuthorize("hasAuthority('ONE:PERMISSION:CREATE')")
    @ResponseBody
    public String addPermission(@RequestBody Map<String, String> params) {
        String result = "";
        if (params.size() > 0) {
            try {
                boolean resultName = checkExist(params.get("addPermission"), "");

                if (resultName) {
                    result = "201";
                } else {
                    Permissions permissions = initPermission(params, "");
                    permissionsClient.create(permissions);
                    result = "200";
                }

            } catch (Exception e) {
                result = "400";
            }

        }
        return result;
    }

    @PostMapping("/editPermission")
    @PreAuthorize("hasAuthority('ONE:PERMISSION:UPDATE')")
    @ResponseBody
    public String editPermission(@RequestBody Map<String, String> params) {
        String result = "";
        if (params.size() > 0) {
            try {

                boolean resultName = checkExist(params.get("addPermission"), params.get("addPermissionId"));

                if (resultName) {
                    result = "201";
                } else {
                    Permissions permissions = initPermission(params, params.get("addPermissionId"));
                    permissionsClient.update(permissions, Long.valueOf(params.get("addPermissionId")));
                    logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "update permission " + params.get("addPermissionId"), "", "");
                    updateRole(params.get("addPermissionId"));

                    result = "200";
                }

            } catch (Exception e) {
                result = "400";
            }

        }
        return result;
    }

    @GetMapping("/deletePermission")
    @PreAuthorize("hasAuthority('ONE:PERMISSION:DELETE')")
    @ResponseBody
    public String deletePermission(@RequestParam Map<String, String> params) {
        String result = "";
        if (params.keySet().contains("id")) {
            String ids = params.get("id");
            JsonArray array = new Gson().fromJson(ids, JsonArray.class);

            try {
                for (int i = 0; i < array.size(); i++) {
                    RoleSearchForm roleSearchForm = new RoleSearchForm();
                    Set<Long> longSet = new HashSet<Long>();
                    longSet.add(array.get(i).getAsLong());
                    roleSearchForm.permissionIds = longSet;
                    int count = roleClient.count(roleSearchForm);
                    if (count == 0) {
                        permissionsClient.delete(array.get(i).getAsLong());
                        logger.info("#USER_LOG {},{},{},{},{}", session.getId(), session.getAttribute("username"), "delete role " + array.get(i).getAsLong(), "", "");
                        result = "200";
                    } else {
                        result = "300";
                        break;
                    }
                }
            } catch (Exception e) {
                result = "400";
            }
        }
        return result;
    }

    private boolean checkExist(String name, String id) {
        PermissionSearchForm permissionSearchForm = new PermissionSearchForm();
        permissionSearchForm.name = name;
        Permissions[] permissions = permissionsClient.checkExistName(permissionSearchForm);

        if (permissions.length > 0) {

            if (id.isEmpty()) {
                return true;
            } else {
                for (Permissions p : permissions) {
                    if (p.getId() == Long.valueOf(id)) {
                        return false;
                    }
                }
                return true;
            }

        }
        return false;

    }

    private void updateRole(String permissionId) {

        RoleSearchForm roleSearchForm = new RoleSearchForm();
        Set<Long> longSet = new HashSet<Long>();
        longSet.add(Long.valueOf(permissionId));
        roleSearchForm.permissionIds = longSet;

        Role[] roles = roleClient.search(roleSearchForm);
        if (roles.length > 0) {
            List<String> idRoles = new ArrayList<String>();
            for (Role role : roles) {
                idRoles.add(String.valueOf(role.getId()));
                Set<Long> setPermission = new HashSet<>();
                Set<String> setOperation = new HashSet<>();

                Set<Long> setPermissionsIds = role.getPermissionsIds();
                for (Long l : setPermissionsIds) {
                    setPermission.add(l);
                    Permissions permissions = permissionsClient.getByID(l);
                    String lol = permissions.getOperationIds().toString().replaceAll(" ", "");
                    String[] lolArray = lol.substring(1, lol.length() - 1).trim().split(",");
                    Collections.addAll(setOperation, lolArray);
                }
                role.setOperationIds(setOperation);
                role.setPermissionsIds(setPermission);
                roleClient.update(role, role.getId());
            }

            for (String id : idRoles) {
                UserSearchForm userSearchForm = new UserSearchForm();
                userSearchForm.currentUserName = session.getAttribute("username").toString();
                Set<Integer> rolesSet = new HashSet<Integer>();
                rolesSet.add(Integer.valueOf(id));
                userSearchForm.roles = rolesSet;
                User[] users = userClient.search(userSearchForm);

                for (User user : users) {
                    Set<String> setOperation = new HashSet<>();
                    Set<String> setRoles = user.roleIds;
                    for (String s : setRoles) {
                        String lol = roleClient.get(s).getOperationIds().toString().replaceAll("\\s+", "");
                        String[] lolArray = lol.substring(1, lol.length() - 1).split(",");
                        Collections.addAll(setOperation, lolArray);
                    }
                    user.operationIds = setOperation;
                    userClient.update(String.valueOf(user.userName), user);
                }
            }
        }
    }

    private Permissions initPermission(Map<String, String> params, String permissionID) {
        Permissions permissions = new Permissions();
        if (!permissionID.equals("")) {
            permissions.setId(Long.valueOf(permissionID));
        }
        permissions.setName(params.get("addPermission"));
        permissions.setDescription(params.get("addDescription"));
        String[] addOperations = params.get("addOperations").replace("@", "").split(",");
        Set<String> setAddOperations = new HashSet<>();
        Collections.addAll(setAddOperations, addOperations);
        permissions.setOperationIds(setAddOperations);
        return permissions;
    }

    private String loadDataOperation() {
        Operation[] operations = operationClient.findAll();
        List<Operation> operationList = new ArrayList<Operation>();
        for (Operation operation : operations) {
            operationList.add(operation);
        }

        JsonArray jsonArray = new JsonArray();
        for (Operation operation : operationList) {
            List<String> listjsonObj = new ArrayList<String>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                String asString = asJsonObject.get("title").getAsString();
                listjsonObj.add(asString);
            }
            if (listjsonObj.contains(operation.getGroupName())) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : asJsonObject.entrySet()) {
                        if (entry.getKey().equals("key") && entry.getValue().toString().replace("\"", "").equals(operation.getGroupName())) {
                            JsonArray jsonArray1 = asJsonObject.getAsJsonArray("children");
                            JsonObject jsonObject1 = new JsonObject();
                            jsonObject1.addProperty("title", operation.getName());
                            jsonObject1.addProperty("key", operation.getId());
                            jsonArray1.add(jsonObject1);
                        }
                    }
                }
            } else {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("title", operation.getGroupName());
                jsonObject.addProperty("key", operation.getGroupName());
                JsonArray jsonArrayChildren = new JsonArray();
                jsonArrayChildren = loadDataOperationChildren(operation);
                jsonObject.add("children", jsonArrayChildren);
                jsonArray.add(jsonObject);
            }

        }
        return jsonArray.toString();
    }

    private JsonArray loadDataOperationChildren(Operation operation) {
        JsonArray jsonArrayChildren = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", operation.getName());
        jsonObject.addProperty("key", operation.getId());
        jsonArrayChildren.add(jsonObject);
        return jsonArrayChildren;
    }

}
