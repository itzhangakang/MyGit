package com.itkang.dao;

import com.itkang.domain.Book;

import java.util.List;

/**
 * @author Mr.kang
 * Created by - 林夕
 * @date 2018/8/31 21:19
 */
public interface IBookDao {

    // 查询全部图书
    List<Book> findAll();
}
