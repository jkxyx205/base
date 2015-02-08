package com.base.office;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ExcelClient {
 
    /**
     * @param args
     */
    public static final String DBDRIVER = "oracle.jdbc.driver.OracleDriver";
    //连接地址是由各个数据库生产商单独提供的，所以需要单独记住
    public static final String DBURL = "jdbc:oracle:thin:@172.18.50.139:1521:movitechOcl";
    //连接数据库的用户名
    public static final String DBUSER = "space_sm";
    //连接数据库的密码
    public static final String DBPASS = "space_sm";
 	
	public static void main(String[] args) throws Exception {
        Connection con = null; //表示数据库的连接对象
        PreparedStatement pstmt = null; //表示数据库更新操作

        String sql = "SELECT * from sm_scenario t where t.id = :id";

        Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序


        con = DriverManager.getConnection(DBURL, DBUSER, DBPASS); //2、连接数据库
        pstmt = con.prepareStatement(sql); //使用预处理的方式创建对象
        pstmt.setString(1,"1419328510900");

        ResultSet rs = pstmt.executeQuery();
        if(rs.next()) {
            System.out.println(rs.getString(1));

        }

        pstmt.close();
        con.close(); // 4、关闭数据库
    }
}
