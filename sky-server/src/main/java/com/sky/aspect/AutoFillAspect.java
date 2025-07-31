package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 公共字段自动填充的切面
 *
 * @Author itcast
 * @Create 2024/6/11
 **/
@Aspect //通过这个注解来标记当前这个类是一个切面类
@Component //注意：切面对象本质也是一个spring容器中的bean对象
public class AutoFillAspect {

    /**
     * 通过切入点指定我们需要拦截哪些类或者哪些方法
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFilePointCut(){}

    /**
     * 前置通知
     */
    @Before("autoFilePointCut()")
    public void autoFill(JoinPoint joinPoint){
        System.out.println("前置通知开始执行...");
        //为实体类中公共的属性设置值

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获得方法签名对象
        //获得当前被拦截的方法对象
        Method method = signature.getMethod();

        //获得当前方法上的注解对象
        AutoFill annotation = method.getAnnotation(AutoFill.class);
        //获得当前数据库操作类型，从注解中就可以获取到
        //本次数据库操作的类型是什么？
        OperationType operationType = annotation.type();
        System.out.println(operationType);

        //实体类在哪？
        Object[] args = joinPoint.getArgs();//获得当前被拦截的方法上的参数列表
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];

        //赋的值是什么？
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //判断当前数据库操作类型
        if(operationType == OperationType.INSERT){
            //需要为4个属性赋值，需要通过反射来赋值
            try {
                Method setCreateTimeMethod = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                //通过反射来调用上面的方法
                setCreateTimeMethod.invoke(entity,now);//相当于 category.setCreateTime(now);
                Method setUpdateTimeMethod = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                setUpdateTimeMethod.invoke(entity,now);
                Method setCreateUserMethod = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                setCreateUserMethod.invoke(entity,currentId);
                Method setUpdateUserMethod = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                setUpdateUserMethod.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //需要为2个属性赋值
            //需要为4个属性赋值，需要通过反射来赋值
            try {
                Method setUpdateTimeMethod = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                setUpdateTimeMethod.invoke(entity,now);
                Method setUpdateUserMethod = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                setUpdateUserMethod.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
