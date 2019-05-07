# mvc-orm-ioc

主要使用反射、注解和常用数据结构(List、Map等)开发的微型框架，使得能够复用[另一个项目](https://github.com/EugeneYoung1515/zero-xml-forum)(基于Spring、Spring MVC、Hibernate开发的论坛)Dao层以外的大部分代码。框架代码主要在com.smart.core包及其子包。  

- **MVC**

仿Spring MVC开发了多个注解。简单地支持@ResquestMapping、@PathVariable和@RequestParam注解。@PathVariable的实现利用到了Java 8的编译参数-parameters。支持Servlet API类(HttpServletRequest、HttpServletResponse和HttpSession)作为方法参数。支持简单的表单参数-POJO映射。  

支持String和ModelAndView两种视图逻辑名，支持使用@ResponseBody(使用Fastjson实现)把方法返回值转换为JSON。  

编写过滤器，以及重写HttpServletRequest的service方法，以支持GET和POST以外的HTTP方法。  

开发中使用Servlet的原生注解@WebServlet、@WebFilter和@WebListener，结合ServletContextListener实现类，减少对web.xml的使用。  

- **ORM**

支持@Table、@Column、@Id注解，实现表和对象间的映射。使用@ManyToOne，可以获得对多对一关系的简单支持。  

BaseDao实现了简单的增删改查方法，以POJO对象作为方法参数或者返回值，可供其子类使用。也提供了以预备语句为参数的方法，用于复杂的查询。BaseDao还提供了分页方法。  

使用ThreadLocal和JDK的动态代理实现简单地事务管理。  

- **IoC**

简单地支持@Component、@Repository、@Service、@Controller和@Autowired注解。