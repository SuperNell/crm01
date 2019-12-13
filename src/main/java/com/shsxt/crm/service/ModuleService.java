package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.db.dao.ModuleMapper;
import com.shsxt.crm.db.dao.PermissionMapper;
import com.shsxt.crm.model.TreeDto;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.Module;
import com.shsxt.crm.vo.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    public List<Map<String,Object>> queryAllModules(){
        return moduleMapper.queryAllModules();
    }

    public List<TreeDto> queryAllModules02(Integer rid){
        List<TreeDto> treeDtos=moduleMapper.queryAllModules02();
        List<Integer> roleHasMids=permissionMapper.queryAllModuleIdsByRoleId(rid);
        if(null!=roleHasMids&& roleHasMids.size()>0){
            treeDtos.forEach(treeDto -> {
                if (roleHasMids.contains(treeDto.getId())){
                    treeDto.setChecked(true);
                }
            });
        }
        return treeDtos;
    }

    public void saveModule(Module module){
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请输入模块名");
        Integer grade=module.getGrade();
        AssertUtil.isTrue(grade==null,"请指定层级！");
        AssertUtil.isTrue(!(grade==1||grade==2||grade==0),"层级值非法！");
        Module temp=moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(null!=temp,"同一层级下模块名不能重复！");
        if(grade==1||grade==2){
            AssertUtil.isTrue(module.getParentId()==null,"请指定上级菜单！");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入菜单权限码！");
        temp=moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null!=temp,"权限码不能重复！");

        module.setIsValid((byte)1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(save(module)<1,"模块添加失败！");
    }


    public void updateModule(Module module){
        Integer mid = module.getId();
        AssertUtil.isTrue(null == mid || null == queryById(mid), "待更新的记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "请输入模块名!");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(null == grade, "请指定层级!");
        AssertUtil.isTrue(!(grade == 1 || grade == 2 || grade == 0), "层级值非法!");
        Module temp = moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(module.getId())),"同一层级下模块名不可重复!");
        if (grade == 1 || grade == 2) {
            AssertUtil.isTrue(null == module.getParentId(), "请指定上级菜单!");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "请输入菜单权限码!");
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(module.getId())),"权限码不可重复!");
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(update(module)<1,"模块更新失败!");

    }

    public void deleteModuleByModuleId(Integer mid){
        Module module=queryById(mid);
        AssertUtil.isTrue(null==mid||null==module,"待删除的记录不存在！");
        int count=moduleMapper.countSubModulesByMid(mid);
        AssertUtil.isTrue(count>0,"当前菜单存在子菜单，暂不支持删除！");
        module.setIsValid((byte)0);
        AssertUtil.isTrue(update(module)<1,"模块删除成功！");
    }
}












