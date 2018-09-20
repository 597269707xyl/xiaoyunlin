package com.zdtech.platform.simserver.utils;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;
import java.util.Properties;
import java.util.SortedMap;


public final class DBHelper {

    /**
     * 驱动
     */
    public static String driver ;
    /**
     * 连接字符串
     */
    public static String url ;
    /**
     * 用户名
     */
    public static String user ;
    /**
     * 密码
     */
    public static String password ;

    private static Properties properties;
    static {
        if (properties == null){
            properties = new Properties();
            InputStream is = DBHelper.class.getClassLoader().getResourceAsStream("./application.properties");
            try {
                properties.load(is);
                is.close();
            } catch (IOException e) {

            }
        }
        url = properties.getProperty("jdbc.url","jdbc:mysql://192.168.120.10:3306/atsp?useUnicode=true&characterEncoding=UTF-8").trim();
        driver = properties.getProperty("jdbc.driver","com.mysql.jdbc.Driver").trim();
        user = properties.getProperty("jdbc.username","root").trim();
        password = properties.getProperty("jdbc.password","root").trim();
    }

    // 此方法为获取数据库连接

    public static Connection getConnection() {

        Connection conn = null;



        try {
            Class.forName(driver); // 加载数据库驱动

            if (null == conn) {

                conn = DriverManager.getConnection(url, user, password);

            }

        } catch (ClassNotFoundException e) {

            System.out.println("Sorry,can't find the Driver!");

            e.printStackTrace();

        } catch (SQLException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return conn;

    }



    /**

     * 增删改【Add、Del、Update】

     *

     * @param sql

     * @return int

     */

    public static int executeNonQuery(String sql) {

        int result = 0;

        Connection conn = null;

        Statement stmt = null;



        try {

            conn = getConnection();

            stmt = conn.createStatement();

            result = stmt.executeUpdate(sql);

        } catch (SQLException err) {

            err.printStackTrace();

            free(null, stmt, conn);

        } finally {

            free(null, stmt, conn);

        }



        return result;

    }



    /**

     * 增删改【Add、Delete、Update】

     *

     * @param sql

     * @param obj

     * @return int

     */

    public static int executeNonQuery(String sql, Object... obj) {

        int result = 0;

        Connection conn = null;

        PreparedStatement pstmt = null;



        try {

            conn = getConnection();

            pstmt = conn.prepareStatement(sql);



            for (int i = 0; i < obj.length; i++) {

                pstmt.setObject(i + 1, obj[i]);

            }



            result = pstmt.executeUpdate();

        } catch (SQLException err) {

            err.printStackTrace();

            free(null, pstmt, conn);

        } finally {

            free(null, pstmt, conn);

        }

        return result;

    }



    /**

     * 查【Query】

     *

     * @param sql

     * @return ResultSet

     */

    public static ResultSet executeQuery(String sql) {

        Connection conn = null;

        Statement stmt = null;

        ResultSet rs = null;



        try {

            conn = getConnection();

            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);

        } catch (SQLException err) {

            err.printStackTrace();

            free(rs, stmt, conn);

        }



        return rs;

    }
    public static SortedMap[] myExecuteQuery(String sql){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            Result result = ResultSupport.toResult(rs);
            SortedMap[] maps = result.getRows();
            return maps;
        } catch (SQLException err) {
            err.printStackTrace();
            return null;
        } finally {
            free(rs, stmt, conn);
        }
    }

    public static Object myExecuteScalar(String sql){
        SortedMap[] rs = myExecuteQuery(sql);
        if (rs == null || rs.length == 0){
            return null;
        }
        Object ret = rs[0].get(rs[0].firstKey());
        return ret;
    }



    /**

     * 查【Query】

     *

     * @param sql

     * @param obj

     * @return ResultSet

     */

    public static ResultSet executeQuery(String sql, Object... obj) {

        Connection conn = null;

        PreparedStatement pstmt = null;

        ResultSet rs = null;



        try {

            conn = getConnection();

            pstmt = conn.prepareStatement(sql);



            for (int i = 0; i < obj.length; i++) {

                pstmt.setObject(i + 1, obj[i]);

            }



            rs = pstmt.executeQuery();

        } catch (SQLException err) {

            err.printStackTrace();

            free(rs, pstmt, conn);

        }



        return rs;

    }



    /**

     * 判断记录是否存在

     *

     * @param sql

     * @return Boolean

     */

    public static Boolean isExist(String sql) {

        ResultSet rs = null;



        try {

            rs = executeQuery(sql);

            rs.last();

            int count = rs.getRow();

            if (count > 0) {

                return true;

            } else {

                return false;

            }

        } catch (SQLException err) {

            err.printStackTrace();

            free(rs);

            return false;

        } finally {

            free(rs);

        }

    }



    /**

     * 判断记录是否存在

     *

     * @param sql

     * @return Boolean

     */

    public static Boolean isExist(String sql, Object... obj) {

        ResultSet rs = null;



        try {

            rs = executeQuery(sql, obj);

            rs.last();

            int count = rs.getRow();

            if (count > 0) {

                return true;

            } else {

                return false;

            }

        } catch (SQLException err) {

            err.printStackTrace();

            free(rs);

            return false;

        } finally {

            free(rs);

        }

    }



    /**

     * 获取查询记录的总行数

     *

     * @param sql

     * @return int

     */

    public static int getCount(String sql) {

        int result = 0;

        ResultSet rs = null;



        try {

            rs = executeQuery(sql);

            rs.last();

            result = rs.getRow();

        } catch (SQLException err) {

            free(rs);

            err.printStackTrace();

        } finally {

            free(rs);

        }



        return result;

    }



    /**

     * 获取查询记录的总行数

     *

     * @param sql

     * @param obj

     * @return int

     */

    public static int getCount(String sql, Object... obj) {

        int result = 0;

        ResultSet rs = null;



        try {

            rs = executeQuery(sql, obj);

            rs.last();

            result = rs.getRow();

        } catch (SQLException err) {

            err.printStackTrace();

        } finally {

            free(rs);

        }



        return result;

    }



    /**

     * 释放【ResultSet】资源

     *

     * @param rs

     */

    public static void free(ResultSet rs) {

        try {

            if (rs != null) {

                rs.close();

            }

        } catch (SQLException err) {

            err.printStackTrace();

        }

    }



    /**

     * 释放【Statement】资源

     *

     * @param st

     */

    public static void free(Statement st) {

        try {

            if (st != null) {

                st.close();

            }

        } catch (SQLException err) {

            err.printStackTrace();

        }

    }



    /**

     * 释放【Connection】资源

     *

     * @param conn

     */

    public static void free(Connection conn) {

        try {

            if (conn != null) {

                conn.close();

            }

        } catch (SQLException err) {

            err.printStackTrace();

        }

    }



    /**

     * 释放所有数据资源

     *

     * @param rs

     * @param st

     * @param conn

     */

    public static void free(ResultSet rs, Statement st, Connection conn) {

        free(rs);

        free(st);

        free(conn);

    }

}