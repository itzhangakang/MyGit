package com.itkang.domain;

import org.junit.Test;

/**
 * @author Mr.kang
 * Created by - 林夕
 * @date 2018/8/31 21:14
 */
public class Book {
    private int id;
    private String bookName;
    private Double price;
    private String pic;
    private String bookDesc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", price=" + price +
                ", pic='" + pic + '\'' +
                ", bookDesc='" + bookDesc + '\'' +
                '}';
    }

}
