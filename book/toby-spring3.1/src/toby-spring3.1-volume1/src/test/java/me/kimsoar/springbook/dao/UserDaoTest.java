package me.kimsoar.springbook.dao;


import me.kimsoar.springbook.dao.DataFactory;
import me.kimsoar.springbook.dao.UserDao;
import me.kimsoar.springbook.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {

        this.user1 = new User("gyumee", "박성철", "springno1");
        this.user2 = new User("leegw700", "이길원", "springno2");
        this.user3 = new User("park100", "홍길동", "springno3 ");
    }

    @Test
    public void addAndGet() throws SQLException {
        this.dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        this.dao.add(this.user1);
        this.dao.add(this.user2);
        assertThat(this.dao.getCount(), is(2));

        User userget1 = this.dao.get(this.user1.getId());
        assertThat(this.user1.getName(), is(userget1.getName()));
        assertThat(this.user1.getPassword(), is(userget1.getPassword()));

        User userget2 = this.dao.get(this.user2.getId());
        assertThat(this.user2.getName(), is(userget2.getName()));
        assertThat(this.user2.getPassword(), is(userget2.getPassword()));


    }

    @Test
    public void count() throws SQLException {

        this.dao.deleteAll();
        assertThat(this.dao.getCount(), is(0));

        this.dao.add(user1);
        assertThat(this.dao.getCount(), is(1));

        this.dao.add(user2);
        assertThat(this.dao.getCount(), is(2));

        this.dao.add(user3);
        assertThat(this.dao.getCount(), is(3));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        this.dao.deleteAll();
        assertThat(this.dao.getCount(), is(0));

        this.dao.get("unkown_id");
    }
}
