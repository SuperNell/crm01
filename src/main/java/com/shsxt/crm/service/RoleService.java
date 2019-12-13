package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.db.dao.ModuleMapper;
import com.shsxt.crm.db.dao.PermissionMapper;
import com.shsxt.crm.db.dao.RoleMapper;
import com.shsxt.crm.db.dao.UserRoleMapper;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.Permission;
import com.shsxt.crm.vo.Role;
import com.shsxt.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    public List<Map<String,Object>> queryAllRoles(){
        return roleMapper.queryAllRoles();
    }

    public void saveRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank((role.getRoleName())),"请输入角色名！");
        AssertUtil.isTrue(roleMapper.queryRoleByRoleName(role.getRoleName())!=null,"角色已存在！");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(save(role)<1,"角色记录添加失败！");

    }

    public void updateRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank((role.getRoleName())),"请输入角色名！");
        AssertUtil.isTrue(role.getId()==null||queryById(role.getId())==null,"待更新的角色记录不存在！");
        Role temRole=roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null!=temRole && !(temRole.getId().equals(role.getId())),"角色名已存在！");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(update(role)<1,"角色记录更新失败！");
    }

    public void deleteRole(Integer roleId){
        Role role=queryById(roleId);
        AssertUtil.isTrue(roleId==null||null==queryById(roleId),"待删除的角色记录不存在！");

        //从表记录删除
        int count=userRoleMapper.countUserRoleByRoleId(roleId);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByRoleId(roleId)!=count,"角色记录删除失败！");
        }

        role.setIsValid(0);
        AssertUtil.isTrue(update(role)<1,"角色记录删除失败！");
    }

    public void addGrant(Integer[] mid,Integer rid){
        int count =permissionMapper.countPermissionByRoleId(rid);
        if (count>0){
            AssertUtil.isTrue(permissionMapper.deleteByRoleId(rid)!=count,"角色授权失败！");
        }

        if(mid!=null&&mid.length>0){
            List<Permission> permissions=new ArrayList<Permission>();
            for (Integer id : mid) {
                Permission permission=new Permission();
                permission.setRoleId(rid);
                permission.setModuleId(id);
                permission.setAclValue(moduleMapper.queryById(id).getOptValue());
                permission.setUpdateDate(new Date());
                permission.setCreateDate(new Date());
                permissions.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.saveBatch(permissions)!=mid.length,"角色授权失败！");
        }
    }

}










