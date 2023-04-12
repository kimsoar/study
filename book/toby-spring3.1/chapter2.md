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

#### DiritesContext

xml 설정파일을 수정하지 않고 테스트 코드를 통해 오브젝트 관계를 재구성 할 수 있다.
주의점: applicaitonContext.xml 파일의 설정정보를 따라 구성한 오브젝트를 가져와 의존관계를 강제로 변경함.
스프링 테스트 컨텍스트 프레임워크를 적용했다면 애플리케이션 컨텍스트는 테스트 중에 딱 한 개만 만들어지고 모든 테스트 내에서 공유해서 사용한다. 따라서 애플리케이션 컨텍스트의 구성이나 상태를 테스트 내에서 변경 하지 않는 것이 원칙이다.
그런데 의존관계를 강제로 변경하면, 한 번 변경하면 나머지 모든 테스트를 수행 하는 동안 변경된 애플리케이션 컨텍스트가 계속 사용될 것이다.
그래서 @DirtiesContext라는 애토테이션을 추가한다. 이 애노테이션은 스프링의 테스트 컨텍스트 프레임워크에게 해당 클래스의 테스트에서 애플리케이션 컨텍스트의 상태를 변경한다는 것을 알려준다. 테스트 컨텍스트는 이 애노테이션이 붙은 테스트 클래스에는 애플리케이션 컨텍스트 공유를 허용하지 않는다. 따라서 테스트 메소드를 수행하고 나면 매번 새로운 애플리케이션 컨텍스트를 만들어서 다음 테스트가 사용하게 해준다.

#### DI와 테스트

DI를 테스트에 이용하는 세가지 방법 중 어떤 것을 선택해야 할까?

- 테스트 코드에 의한 DI
  - @DirtiesContext 사용
  - 테스트 설정을 따로 만들었다고 하더라도 때로는 예외적인 의존관계를 강제로 구성해야 할때
- 테스트를 위한 별도의 DI 설정
  - 테스트용 콘텍스트 xml 별도 생성
  - 여러 오브젝트와 복잡한 의존관계를 갖고 있는 오브젝트를 테스트해야 할 경우는 별도 DI설정
- 컨테이너 없는 DI 테스트
  - 컨텍스트를 사용하지 않고 직접 테스트
  - 항상 스프링 컨테이너 없이 테스트할 수 있는 방법을 가장 우선적으로 고려하자
