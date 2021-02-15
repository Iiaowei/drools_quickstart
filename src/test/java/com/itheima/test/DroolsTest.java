package com.itheima.test;

import com.itheima.drools.entity.ComparisonOperatorEntity;
import com.itheima.drools.entity.Order;
import com.itheima.drools.entity.Student;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;

public class DroolsTest {
    @Test
    public void test1() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();
        Order order = new Order();
        order.setOriginalPrice(50D);
        kieSession.insert(order);
        kieSession.fireAllRules();
        kieSession.dispose();

        System.out.printf("优惠后的价格:%s", order.getRealPrice());
    }

    //比较操作符
    @Test
    public void test2() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();
        ComparisonOperatorEntity entity = new ComparisonOperatorEntity();
        entity.setNames("李啊");
        List<String> list = new ArrayList<>();
        list.add("李四");
        entity.setList(list);
        kieSession.insert(entity);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    //指定规则
    @Test
    public void test3() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();
        ComparisonOperatorEntity entity = new ComparisonOperatorEntity();
        entity.setNames("李啊");
        List<String> list = new ArrayList<>();
        list.add("李四");
        entity.setList(list);
        kieSession.insert(entity);
//        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("rule_comparison_not_contains"));
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_"));
        kieSession.dispose();
    }

    //Drools内置方法update
    @Test
    public void test4() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();
        Student student = new Student();
        student.setAge(5);
        kieSession.insert(student);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    //Drools内置方法update
    @Test
    public void test5() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();
        Student student = new Student();
        student.setAge(10);
        kieSession.insert(student);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
