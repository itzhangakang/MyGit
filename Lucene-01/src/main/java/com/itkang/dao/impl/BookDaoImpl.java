package com.itkang.dao.impl;

import com.itkang.dao.IBookDao;
import com.itkang.domain.Book;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.kang
 * Created by - 林夕
 * @date 2018/8/31 21:20
 */
public class BookDaoImpl implements IBookDao {

    @Override
    public List<Book> findAll() {
        // 创建List 集合封装查询结果
        List<Book> bookList = new ArrayList<Book>();
        Connection connection = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        try {
            // 加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 创建数据库连接对象
            connection = DriverManager.getConnection("jdbc:mysql:///ssm87","root","123456");
            // 编写sql语句
            String sql = "select * from book";
            // 创建statement
            psmt = connection.prepareStatement(sql);
            // 执行查询
            rs= psmt.executeQuery();
            // 处理结果集
            while(rs.next()){
                // 创建Book 对象
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setBookName(rs.getString("bookname"));
                book.setPrice(rs.getDouble("price"));
                book.setPic(rs.getString("pic"));
                book.setBookDesc(rs.getString("bookdesc"));
                bookList.add(book);

            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            // 释放资源
            try{
                if (rs != null) rs.close();
                if(psmt != null) psmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bookList;
    }
}
