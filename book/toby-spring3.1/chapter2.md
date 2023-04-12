# 2장. 테스트

---

```java
// 테스트 중에 발생할 것으로 기대하는 예외 클래스를 지정
@Test(expected=EmptyResultDataAccessException.class)
public void getUserFailure() throws SQLException {
    ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

    UserDao dao = context.getBean("userDao", UserDao.class);
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    // 이메소드 실행 중에 예외가 발생해야 한다.
    // 예외가 발생하지 않으면 테스트가 실패한다.
    dao.get("unknown_id");
}
```

|      | 단계                  | 내용                                         | 코드                                                  |
| ---- | --------------------- | -------------------------------------------- | ----------------------------------------------------- |
| 조건 | 어떤 조건을<br>가지고 | 가져올 사용자 정보가 존재하지<br>않는 경우에 | dao.deleteAll()<br>assertThat(dao.getCount(), is(0)); |
| 행위 | 무엇을 할 때          | 존재하지 않는 id로 get()을<br>실행하면       | get("unknown_id");                                    |
| 결과 | 어떤 결과가<br>나온다 | 특별한 예외가 던져진다                       | @Test(expected=EmptyResultDataAccessException.class)  |

<br/>

#### 픽스처(fixture)

테스트를 수행하는 데 필요한 정보나 오브젝트

```java
// 스프링 테스트 컨텍스트를 적용한 UserDaoTest

@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장 기능 지정
@ContextConfiguration(locations="/applicationContext.xml") // 테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;
    ...

    @Before
    public void setUp() {
        this.dao = this.context.getBean("userDao", UserDao.class);
        ...
    }
    ...

}

@RunWith는 JUnit 프레임워크의 테스트 실행 방법을 확장 때 사용하는 애노테이션.
SpringJUnit4ClassRunner라는 JUnit용 테스트 컨텍스트 프레임워크 확장 클래스를 지정해주면 JUnit이 테스트를 진행하는 중에 테스트가 사용할 애플리케이션 컨텍스트를 만들고 관리해준다.
@ContextConfiguration은 자동으로 만들어줄 애플리케이션 컨텍스트의 설정파일 위치를 지정한 것이다.

```
