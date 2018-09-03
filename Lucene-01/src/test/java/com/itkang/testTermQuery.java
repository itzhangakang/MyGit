package com.itkang;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @author Mr.kang
 * Created by - 林夕
 * @date 2018/9/2 23:10
 *
 */
public class testTermQuery {

    // TermQuery 关键词查询
    // 需求： 查询图书名称域中包含的java的图书
    @Test
    public void testTermQuery() throws IOException {
        // 创建查询对象
        TermQuery q = new TermQuery(new Term("bookName","java"));
        // 执行搜索
        search(q);
    }

    /**
     *  NumericRangeQuery 数值范围查询
     * 需求：查询图书价格在 80 到 100 之间的图书
     */
    @Test
    public void testNumericRangeQuery() throws Exception{

        // 不包含边界条件
        //Query q = NumericRangeQuery.newDoubleRange("bookPrice",80d, 100d, false, false);

        // 包含边界值得搜索
        Query q  = NumericRangeQuery.newDoubleRange("bookPrice",80d,100d,true,true);
        // 执行搜索
        search(q);

    }

    /**
     *  BooleanQuery 布尔查询
     * 需求：查询图书名称域中包含有 java 的图书，并且价格在 80 到 100 之间（包含边界值）
     * @throws IOException
     */
    @Test
    public void testBooleanQuery() throws IOException {
        // 条件一
        TermQuery termQuery = new TermQuery(new Term("bookName","java"));
        // 条件二
        Query query = NumericRangeQuery.newDoubleRange("bookPrice",80d,100d,true,true);

        BooleanQuery bo = new BooleanQuery();
        bo.add(termQuery,BooleanClause.Occur.MUST);
        bo.add(query,BooleanClause.Occur.MUST);

        // 执行搜索
        search(bo); // 执行的搜索语句条件  +bookName:java +bookPrice:[80.0 TO 100.0]
    }

    /**
     * 使用 QueryParser
     * 需求：查询图书名称域中包含有 java ，并且图书名称域中包含有 lucene 的图书
     * @throws IOException
     */
    @Test
    public void testQueryParser() throws Exception {
        // 创建分词器对象
        Analyzer analyzer = new IKAnalyzer();
        // 创建   QueryParser解析器对象
        QueryParser queryParser = new QueryParser("bppkName",analyzer);
        // 搜索条件
        Query query = queryParser.parse("bookName:java AND bookName:lucene");

        // 执行搜索
        search(query);

    }

    // 搜索的方法
    private void search(Query query) throws IOException {
        // 查询语句
        System.out.println("查询的语句是：" + query);

        // 创建索引库存储目录
        Directory directory = FSDirectory.open(new File("C:\\康work\\Lucene 分词\\index2"));
        // 创建IndexReader,读取搜索索引库对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建IndexSearcher,执行搜索索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        TopDocs topDocs = indexSearcher.search(query,10);
        // 处理结果集
        System.out.println("总命中的记录数：" + topDocs.totalHits);
        // 获取搜索的文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        // ScoreDoc 对象 只对文档id和分值信息
        for (ScoreDoc scoreDoc : scoreDocs){
            System.out.println("===================");
            System.out.println("文档id ：" + scoreDoc.doc + "\t文档分值：" + scoreDoc.score);
            // 根据文档id获取指定的文档
            Document document = indexSearcher.doc(scoreDoc.doc);

            System.out.println("图书Id：" + document.get("id"));
            System.out.println("图书名称：" + document.get("bookName"));
            System.out.println("图书价格：" + document.get("bookPrice"));
            System.out.println("图书图片：" + document.get("bookpic"));
            System.out.println("图书描述：" + document.get("bookDesc"));
        }
        // 释放资源
        indexReader.close();
    }
}
