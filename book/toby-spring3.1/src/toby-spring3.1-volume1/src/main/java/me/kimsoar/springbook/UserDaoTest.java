package me.kimsoar.springbook;


import me.kimsoar.springbook.dao.DataFactory;
import me.kimsoar.springbook.dao.UserDao;
import me.kimsoar.springbook.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        // ApplicationContext context = new AnnotationConfigApplicationContext(DataFactory.class);
        ApplicationContext context = new GenericXmlApplicationContext("classpath:applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        dao.add(user);
        
        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + "조회 성공");
    }
}
