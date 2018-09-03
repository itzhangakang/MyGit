package com.itkang;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @author Mr.kang
 * Created by - 林夕
 * @date 2018/9/2 20:45
 */
public class Test02 {

    // 根据term 分词删除索引
    @Test
    public void delectIndexByTerm() throws Exception {
        // 创建分析器对象（Analyzer），用于分词
        Analyzer analyzer = new IKAnalyzer();
        // 创建索引库配置对象（IndexWriterConfig），配置索引库
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        // 创建索引库目录对象（Directory），指定索引库的位置
        Directory directory = FSDirectory.open(new File("C:\\康work\\Lucene 分词\\index2"));
        // 创建索引库操作对象（IndexWriter），操作索引库
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        // 创建条件对象（Term）
        Term term = new Term("bookName", "java");
        // 使用 IndexWriter 对象，执行删除
        indexWriter.deleteDocuments(term);
        // 关闭资源
        indexWriter.close();
    }

    // 删除全部索引
    @Test
    public void deleteAll() throws Exception {
        // 创建分词器对象
        Analyzer analyzer = new IKAnalyzer();
        // 创建索引库配置对象
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        // 创建索引库目录对象
        Directory directory = FSDirectory.open(new File("C:\\康work\\Lucene 分词\\index2"));
        // 创建索引库操作对象，操作索引库
        IndexWriter indexWriter = new IndexWriter(directory, iwc);

        // 创建条件对象
        Term term = new Term("bppkName", "java");

        // 执行方法
        indexWriter.deleteDocuments(term);
        // 关闭资源
        indexWriter.close();
    }

    // 更新索引
    @Test
    public void updateTest() throws IOException {
        // 创建分词器对象
        Analyzer analyzer = new IKAnalyzer();
        // 创建索引库配置对象
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2,analyzer);
        // 创建索引库目录对象
        Directory directory = FSDirectory.open(new File("C:\\康work\\Lucene 分词\\index2"));
        // 创建索引库操作对象
        IndexWriter indexWriter = new IndexWriter(directory,iwc);

        // 创文档对象
        Document document = new Document();
        // 文档添加域
        document.add(new StringField("id","123456", Field.Store.YES));
        document.add(new TextField("name","who are you", Field.Store.YES));

        // 创建 条件对象
        Term term = new Term("name","lucene");

        // 执行更新操作
        indexWriter.updateDocument(term,document);

        // 提交事务
        indexWriter.commit();

        indexWriter.close();
    }

}
