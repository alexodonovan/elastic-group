package com.oddsocks.elastic.documents;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.oddsocks.elastic.config.ElasticConfig;

@Service
public class DocumentRepository {

	private static Logger LOGGER = LoggerFactory.getLogger(DocumentRepository.class);

	@Autowired
	private Analyzer standardAnalyzer;

	@Autowired
	private IndexWriter indexWriter;

	public void addDocument(String title, String isbn) {
		addDocument(createDocument(title, isbn));
	}

	public Document createDocument(String title, String isbn) {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));

		// use a string field for isbn because we don't want it tokenized
		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		return doc;
	}

	public void addDocument(Document doc) {
		try {
			indexWriter.addDocument(doc);
			indexWriter.commit();
		} catch (IOException ex) {
			LOGGER.error("Unable to access index directory {}", ex);
		}
	}

	public List<BookVO> findBook(String field, String value) {
		QueryBuilder builder = new QueryBuilder(standardAnalyzer);
		Query query = builder.createBooleanQuery(field, value, BooleanClause.Occur.MUST);
		return toBookVOs(doSearch(query, 100));
	}

	private List<BookVO> toBookVOs(List<Document> results) {
		return results	.stream()
						.map(this::getBookVO)
						.collect(Collectors.toList());
	}

	public IndexSearcher indexSearcher() {
		return ElasticConfig.newIndexSearcher();
	}

	public List<Document> doSearch(Query query, int count) {
		try {
			List<Document> docs = Lists.newArrayList();
			IndexSearcher indexSearcher = indexSearcher();
			ScoreDoc[] hits = indexSearcher.search(query, count).scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				docs.add(indexSearcher.doc(hits[i].doc));
			}
			return docs;
		} catch (IOException e) {
			LOGGER.error("Search failed", e);
			return Lists.newArrayList();
		}
	}

	private BookVO getBookVO(Document doc) {
		return new BookVO(doc.get("title"), doc.get("isbn"));
	}

}
