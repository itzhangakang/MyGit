package com.itkang;

import com.itkang.dao.IBookDao;
import com.itkang.dao.impl.BookDaoImpl;
import com.itkang.domain.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.kang
 * Created by - 林夕
 * @date 2018/9/1 18:52
 */
public class Test01 {

    //  索引流程实现代码
    @Test
    public void createIndex() throws Exception {
        // 采集数据
        IBookDao bookDao = new BookDaoImpl();
        List<Book> bookList = bookDao.findAll();

        // 创建文档集合对象
        List<Document> documents = new ArrayList<>();
        for (Book book : bookList) {
            // 创建文档对象
            Document doc = new Document();
            // 图书ID
            /**
             * 不需要分词，需要索引，需要存储
             *  -StringField
             */
            doc.add(new StringField("id", book.getId() + "", Field.Store.YES));
            // 图书名称
            /**
             * 需要分词。需要索引，需要存储
             *  TextField
             */
            doc.add(new TextField("bookName", book.getBookName() + "", Field.Store.YES));
            // 图书价格
            /**
             * 需要分词 (数值型的Filed lucene 使用内部分词)
             * 需要索引，需要存储
             *  TextField
             */
            doc.add(new DoubleField("bookPrice", book.getPrice(), Field.Store.YES));
            // 图书图片
            /**
             * 不需要分词
             * 不需要索引 需要存储
             */
            doc.add(new StoredField("bookPic", book.getPic()));
            // 图书描述
            /**
             * 需要分词
             * 需要索引
             * 不需要存储
             */
            doc.add(new TextField("bookDesc", book.getBookDesc() + "", Field.Store.NO));
            documents.add(doc);
        }
        // 创建分词器
        // Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();  // 指定使用 IK 分词器分词
        // 创建索引库配置对象，用于配置索引库
        IndexWriterConfig indexWriterConfig =
                new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        // 创建索引库目录对象，用于指定索引库存储位置
        Directory directory = FSDirectory.open(new File("C:\\康work\\Lucene 分词\\index2"));
        // 创建索引库操作对象，用于把文档写入索引库
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        // 循环写入索引库
        for (Document document : documents) {
            indexWriter.addDocument(document);
            // 提交事务
            indexWriter.commit();
        }
        // 释放资源
        indexWriter.close();
    }

    // 搜索索引库
    @Test
    public void searchIndex() throws Exception {

        // 创建分析器对象，用于分词
        // Analyzer analyzer = new StandardAnalyzer();

        Analyzer analyzer = new IKAnalyzer();
        // 创建查询解析器对象  bookDesc:表示你想查询的内容
        QueryParser queryParser = new QueryParser("bookName", analyzer);
        // 解释查询字符串，得到查询对象
        Query query = queryParser.parse("bookName:java");
        // 创建索引库存储目录
        Directory directory = FSDirectory.open(new File("C:\\康work\\Lucene 分词\\index2"));
        // 创建IndexReader读取索引库对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建IndexSearcher 执行搜索索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        /**
         * seach 方法： 执行搜索
         * 参数一：查询对象
         * 参数二：指定搜索结果排序后的前n 个显示
         *
         */
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 处理结果集
        System.out.println("总结果数：" + topDocs.totalHits);
        // 搜索得到的文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // ScoreDoc 对象： 只有文档id和分值信息
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档id ：" + scoreDoc.doc + "文档分值：" + scoreDoc.score);
            // 根据文档id获取指定的文档
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("图书ID" + doc.get("id"));
            System.out.println("图书名称" + doc.get("bookName"));
            System.out.println("图书价格" + doc.get("bookPrice"));
            System.out.println("图书图片" + doc.get("bookPic"));
            System.out.println("图书描述" + doc.get("bookDesc"));
        }
        // 释放资源
        indexReader.close();
    }
}
