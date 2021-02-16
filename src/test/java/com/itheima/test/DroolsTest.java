package com.itheima.test;

import com.itheima.drools.entity.ComparisonOperatorEntity;
import com.itheima.drools.entity.Order;
import com.itheima.drools.entity.Student;
import com.itheima.drools.service.UserService;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DroolsTest {
    @Test
    public void test1() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
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

    @Test
    public void test6() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();
        kieSession.getAgenda().getAgendaGroup("agenda_group_1").setFocus();
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void test7() throws InterruptedException {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();

        new Thread(() -> {
            kieSession.fireUntilHalt();
        }).start();

        TimeUnit.SECONDS.sleep(10);
        kieSession.halt();
        kieSession.dispose();
    }

    @Test
    public void test8()  {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();

        kieSession.fireAllRules();
        kieSession.dispose();
    }

    //测试全局变量
    @Test
    public void test9() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();

        List gList = new ArrayList();
        kieSession.setGlobal("count", 5);
        kieSession.setGlobal("gList", gList);
        kieSession.setGlobal("userService", new UserService());
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_global"));
        System.out.println("在Java程序中gList的size: " + gList.size());
        kieSession.dispose();
    }

    //query
    @Test
    public void test10() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();
        List gList = new ArrayList();
        kieSession.setGlobal("count", 5);
        kieSession.setGlobal("gList", gList);
        kieSession.setGlobal("userService", new UserService());

        Student student = new Student();
        student.setAge(50);
        kieSession.insert(student);

        QueryResults results1 = kieSession.getQueryResults("query_1");
        int size = results1.size();
        System.out.println("符合条件的fact对象size: " + size);

        for (QueryResultsRow row : results1) {
            Student s = (Student) row.get("$s");
            System.out.println(s);
        }

        Student student1 = new Student();
        student1.setAge(51);
        student1.setName("张三");
        kieSession.insert(student1);
        QueryResults results2 = kieSession.getQueryResults("query_2", "张三");
        int size2 = results2.size();
        System.out.println("符合条件的fact对象size: " + size2);

        for (QueryResultsRow row : results2) {
            Student s = (Student) row.get("$s");
            System.out.println(s);
        }
//        kieSession.fireAllRules();

        kieSession.dispose();
    }

    //function
    @Test
    public void test11() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();
        List gList = new ArrayList();
        kieSession.setGlobal("count", 5);
        kieSession.setGlobal("gList", gList);
        kieSession.setGlobal("userService", new UserService());

        Student student = new Student();
        student.setAge(61);
        student.setName("张三");
        kieSession.insert(student);

        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_function"));

        kieSession.dispose();
    }

    //lhs in , not in, eval, not, exists, extends
    @Test
    public void test12() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();

//        Student student = new Student();
//        student.setName("张三");
//        kieSession.insert(student);

        Student student1 = new Student();
        student1.setName("张三1");
        student1.setAge(15);
        kieSession.insert(student1);

        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_lhs"));

        kieSession.dispose();
    }

    //rhs drools对象方法
    @Test
    public void test13() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.newKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession();

        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_rhs"));

        kieSession.dispose();
    }
}
