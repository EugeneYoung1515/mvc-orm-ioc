package com.smart.core.orm;

import com.smart.core.orm.annotation.Column;
import com.smart.core.orm.annotation.Id;
import com.smart.core.orm.annotation.ManyToOne;
import com.smart.core.orm.annotation.Table;
import com.smart.core.orm.transaction.ConnectionManager;
import com.smart.dao.BoardDao;
import com.smart.dao.Page;
import com.smart.dao.TopicDao;
import com.smart.dao.UserDao;
import com.smart.domain.Board;
import com.smart.domain.Topic;
import com.smart.domain.User;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseDao<T> {

    private BoardDao boardDao;
    private TopicDao topicDao;
    private UserDao userDao;

    private Map<String,String> FKToClassName=new HashMap<>();;

    private String table;
    private String tablePK;

    private Class<T> entityClass;

    private Map<String,String> columnToVariable;//子类重写？

    /*
    private String tableFK;

    private String FKClassName;
    */

    /**
     * 通过反射获取子类确定的泛型类
     */
    public BaseDao(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];

        columnToVariable=new HashMap<>();

        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if(tableAnnotation!=null){
            table=tableAnnotation.name();
            Field[] fields = entityClass.getDeclaredFields();
            for(Field field:fields){
                Column column = field.getAnnotation(Column.class);
                if(column!=null){
                    columnToVariable.put(column.name(),field.getName());
                    if(field.isAnnotationPresent(Id.class)){
                        tablePK=column.name();
                    }
                    if(field.isAnnotationPresent(ManyToOne.class)){
                        FKToClassName.put(column.name(),field.getType().getSimpleName());
                    }
                }
            }
        }
    }

    public List<T> executeQuery(String sql,Object... args) throws Exception{///这里异常从SQLException
        PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
        if (args!=null) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
        }
        ResultSet rs = preparedStatement.executeQuery();
        return rowToObject(rs);
    }

    public int executeUpdate(String sql,Object... args) throws SQLException {
        PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        if (args!=null) {
            for (int i = 0; i < args.length; i++) {
                //System.out.println(args[i]);
                if(args[i]!=null && args[i].getClass().equals(Date.class)){//要是args[i]=null买就会抛出空指针异常
                    preparedStatement.setObject(i + 1, args[i],Types.TIMESTAMP);
                }else {
                    preparedStatement.setObject(i + 1, args[i]);//数据库set
                }
            }
        }
        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();//获取自动生成的键
        int key = -1;
        if(rs.next()){
            key=rs.getInt(1);
        }
        return key;
    }


    public List<T> rowToObject(ResultSet rs) throws Exception{//这里异常从SQLException
        System.out.println("rowToObject");
        T obj =null;
        List<T> list = new ArrayList<>();
        while(rs.next()){
            obj = (T)entityClass.newInstance();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for(int i = 0;i<columnCount;i++){
                String columnName = metaData.getColumnName(i+1);//列的原名和别名
                Field field = entityClass.getDeclaredField(columnToVariable.get(columnName));
                field.setAccessible(true);
                Object value = rs.getObject(columnName);//结果集get
                String className = FKToClassName.get(columnName);
                if(className!=null && field.getType().getSimpleName().equals(className)){
                    int FK = rs.getInt(columnName);
                    value=methodInvoke(className,FK);
                }
                /*
                if(field.getType().getSimpleName().equals("LocalDateTime")){
                    value=convertToEntityAttribute((Timestamp) value);
                    System.out.println(value);
                }
                */
                field.set(obj,value);//Field set //看文档 原始类型的包装类型会被转为基本数据类型

            }
            list.add(obj);
        }
        return list;
    }

    private Object methodInvoke(String className,int id) throws Exception{
        if(className.equals("Board")){
            return boardDao.get(id);
        }
        if(className.equals("User")){
            return userDao.get(id);
        }
        if (className.equals("Topic")){
            return topicDao.get(id);
        }
        return null;
    }

    public T get(int id) throws Exception{
        String sql = "SELECT * FROM "+table+" WHERE "+tablePK+" =?";
        List<T> list = executeQuery(sql,id);
        if(list.size()!=0){
            return list.get(0);
        }
        return null;
    }

    public List<T> loadAll() throws Exception{
        String sql = "SELECT * FROM "+table;
        return executeQuery(sql);
    }



    public void remove(T obj) throws Exception{
        String sql = "DELETE FROM "+table+" WHERE "+tablePK+" =?";
        Field field = entityClass.getDeclaredField(columnToVariable.get(tablePK));
        field.setAccessible(true);
        int value = field.getInt(obj);//Field get get(obj)应该也可以 就是返回值 基本数据类型会被包装成包装类 文档里说的
        executeUpdate(sql,value);

    }

    public void update(T obj) throws Exception{//这个也要改 外键哪里要改
        System.out.println("update");
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(table).append(" SET");
        columnToVariable.forEach((k,v)->{
            try {
                sql.append(" ").append(k).append("=?,");
                
                Field field = entityClass.getDeclaredField(v);
                field.setAccessible(true);

                String FK = FKToClassName.get(k);
                if(FK!=null && field.getType().getSimpleName().equals(FK)){
                    list.add(methodInvoke2(field.get(obj),FK));
                }
                /*
                else if(field.getType().getSimpleName().equals("LocalDateTime")){
                    list.add(convertToDatabaseColumn((LocalDateTime)field.get(obj))) ;
                    System.out.println(convertToDatabaseColumn((LocalDateTime)field.get(obj)));
                }*/
                else {
                    list.add(field.get(obj));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        sql.deleteCharAt(sql.length()-1);
        sql.append(" WHERE ").append(tablePK).append("=?");
        System.out.println(sql);
        Field field = entityClass.getDeclaredField(columnToVariable.get(tablePK));//这句上面还有 可能还能省掉
        field.setAccessible(true);
        list.add(field.get(obj));
        Object[] values = list.toArray(new Object[list.size()]);
        executeUpdate(sql.toString(),values);
    }

    public int save(T obj) throws Exception{//只是保存 不含更新
        System.out.println("save");
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        sql2.append("(");
        sql.append("INSERT INTO ").append(table).append(" (");
        columnToVariable.forEach((k,v)->{
            try {
                if(!k.equals(tablePK)) {
                    sql.append(" ").append(k).append(",");
                    sql2.append("?,");

                    Field field = entityClass.getDeclaredField(v);
                    field.setAccessible(true);

                    String FK = FKToClassName.get(k);
                    if (FK != null && field.getType().getSimpleName().equals(FK)) {
                        list.add(methodInvoke2(field.get(obj), FK));
                    }
                    /*
                    else if (field.getType().getSimpleName().equals("LocalDateTime")) {
                        list.add(convertToDatabaseColumn((LocalDateTime) field.get(obj)));
                        System.out.println(convertToDatabaseColumn((LocalDateTime) field.get(obj)));
                    }*/
                    else {
                        list.add(field.get(obj));
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        sql.deleteCharAt(sql.length()-1);
        sql2.deleteCharAt(sql2.length()-1);
        sql.append(" )").append(" VALUES ").append(sql2).append(")");
        System.out.println(sql);
        Object[] values = list.toArray(new Object[list.size()]);
        return executeUpdate(sql.toString(),values);
    }

    public int methodInvoke2(Object obj,String className){
        if(className.equals("Board")){
            Board board = (Board)obj;
            return board.getBoardId();
        }
        if(className.equals("User")){
            User user = (User)obj;
            return user.getUserId();
        }
        if (className.equals("Topic")){
            Topic topic = (Topic)obj;
            return topic.getTopicId();
        }
        return -1;
    }

    public Page pageQuery(String sql, int pageNo, int pageSize, final Object... values) throws Exception{
        String countQueryString = "SELECT COUNT(*) " + removeSelect(removeOrders(sql));
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(countQueryString);
        if (values!=null) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);//数据库set
            }
        }
        ResultSet rs = preparedStatement.executeQuery();
        int totalCount = 0;
        if(rs.next()){
            totalCount=rs.getInt(1);
        }
        if(totalCount<1){
            return new Page();
        }

        int startIndex = Page.getStartOfPage(pageNo, pageSize);
        PreparedStatement preparedStatement2 = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        preparedStatement2.setMaxRows(pageSize*pageNo);
        if (values!=null) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement2.setObject(i + 1, values[i]);//数据库set
            }
            ResultSet rs2= preparedStatement2.executeQuery();
            rs2.relative(pageSize*(pageNo-1));
            List<T> list = rowToObject(rs2);
            return new Page(startIndex, totalCount, pageSize, list);
        }
        return new Page();
    }

    private static String removeSelect(String sql) {//移除Select
        int beginPos = sql.indexOf("FROM");
        return sql.substring(beginPos);
    }

    private static String removeOrders(String sql) {//除hql的orderby 子句，用于pagedQuery.
        Pattern p = Pattern.compile("ORDER\\s*BY[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        //两个\\ 转义\ 用来表示\
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");//用空串去替换 orderby 子句
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /*
    public String getTableFK() {
        return tableFK;
    }

    public String getFKClassName() {
        return FKClassName;
    }
    */

    public void setBoardDao(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    public void setTopicDao(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /*
    public LocalDateTime convertToEntityAttribute(Timestamp ts) {
        if(ts!=null){
            return ts.toLocalDateTime();
        }
        return null;
    }

    public Timestamp convertToDatabaseColumn(LocalDateTime ldt) {
        return Timestamp.valueOf(ldt);
    }
    */
}
