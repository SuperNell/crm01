package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.db.dao.UserMapper;
import com.shsxt.crm.db.dao.UserRoleMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import com.shsxt.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService<User,Integer> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    public UserModel login(String userName,String password){
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(password),"密码不能为空！");
        User user=userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(null==user,"用户记录不存在或者已注销");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(password))),"密码错误！");
        return buildUserModelInfo(user);
    }

    private UserModel buildUserModelInfo(User user){
        UserModel userModel =new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;

    }


    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        User user=userMapper.queryById(userId);
        AssertUtil.isTrue(null==userId||null==user,"用户不存在或未登录！");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword)||!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),
                "原密码不正确！");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword)||StringUtils.isBlank(confirmPassword),"新密码或确认密码不能为空！");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码和确认密码不一致！");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码不能和原密码一致！");
        user.setUserPwd(Md5Util.encode(newPassword));
        AssertUtil.isTrue(userMapper.update(user)<1,"密码更新失败！");
    }


    public void saveUser(User user){
        checkParams(user.getUserName(),user.getTrueName());
        AssertUtil.isTrue(null!=userMapper.queryUserByUserName(user.getUserName()),"该用户已存在！");
        user.setUserPwd(Md5Util.encode("123456"));
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());

        AssertUtil.isTrue(save(user)<1,"用户记录添加失败！");
        //关联中间表t_user_role
        Integer userId=userMapper.queryUserByUserName(user.getUserName()).getId();

        relationUserRole(userId,user.getRoleIds());
    }

    private void relationUserRole(Integer userId, List<Integer> roleIds) {
        //t_user_role用户角色记录

        int count =userRoleMapper.countUserRoleByUserId(userId);
        if (count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色记录关联失败！");
        }


        if(null!=roleIds&&roleIds.size()>0){
            List<UserRole> userRoles=new ArrayList<UserRole>();
            roleIds.forEach(rid->{
                UserRole userRole=new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(rid);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            });
            AssertUtil.isTrue(userRoleMapper.saveBatch(userRoles)!=roleIds.size(),"用户角色记录关联失败！");
        }


    }


    private void checkParams(String userName, String trueName) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(trueName),"请输入真实名称！");
    }

    public void updateUser(User user){
        checkParams(user.getUserName(),user.getTrueName());
        User tempUser=queryById(user.getId());
        AssertUtil.isTrue(null==user.getId()||null==tempUser,"待更新用户不存在！");
        tempUser=userMapper.queryUserByUserName(user.getUserName());
        AssertUtil.isTrue(null!=tempUser &&!(tempUser.getId().equals(user.getId())),"用户名已存在！");
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(update(user)<1,"用户更新失败！");

        relationUserRole(user.getId(),user.getRoleIds());
    }

    public void deleteUser(Integer userId){
        User user=queryById(userId);
        //AssertUtil.isTrue(null==userId||null==user,"待删除用户不存在！");

        //删除从表主表数据
        int count =userRoleMapper.countUserRoleByUserId(userId);
        if (count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色记录删除失败！");
        }

        user.setIsValid(0);
        AssertUtil.isTrue(update(user)<1,"用户记录删除失败！");
    }

}
