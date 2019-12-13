package com.shsxt.crm.proxy;

import com.shsxt.crm.annotations.RequirePermission;
import com.shsxt.crm.utils.AssertUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

//定义权限切面类
@Component
@Aspect
public class PermissionProxy {

    @Autowired
    private HttpSession session;

    //切入点 通知
    //拦截指定注解标注的所有方法
    @Pointcut("@annotation(com.shsxt.crm.annotations.RequirePermission)")
    public void cut(){}

    @Around(value="cut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result=null;

        //获取用户用友的角色对应的所有权限
        List<String> aclValues=(List<String>)session.getAttribute("permissions");
        //获取客户端访问的url对应的方法 权限码值

        MethodSignature methodSignature=(MethodSignature)pjp.getSignature();
        RequirePermission requirePermission=methodSignature.getMethod().getAnnotation(RequirePermission.class);
        String aclValue=requirePermission.aclValue();
        //判断aclValues是否包含当前方法的权限码值
        AssertUtil.isTrue(!(aclValues.contains(aclValue)),"权限不足!");
        result=pjp.proceed();
        return result;
    }
}
