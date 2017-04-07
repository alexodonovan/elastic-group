
package com.oddsocks.elastic.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

	private static Logger LOGGER = LoggerFactory.getLogger(ElasticConfig.class);

	private static String indexDir = "/tmp/lucene";

	@Bean
	public IndexWriter indexWriter() throws Exception {
		Directory dir = FSDirectory.open(indexPath());

		IndexWriterConfig iwc = writerConfig();
		iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

		return new IndexWriter(dir, iwc);
	}

	private static IndexWriterConfig writerConfig() {
		return new IndexWriterConfig(standardAnalyzer());
	}

	private static Path indexPath() {
		return Paths.get(indexDir);
	}

	@Bean
	public static Analyzer standardAnalyzer() {
		return new StandardAnalyzer();
	}

	public static IndexSearcher newIndexSearcher() {
		return new IndexSearcher(indexReader());
	}

	private static DirectoryReader indexReader() {
		try {
			Directory dir = FSDirectory.open(indexPath());
			return DirectoryReader.open(dir);
		} catch (IOException ex) {
			LOGGER.error("Can't open index {}", ex);
			throw new RuntimeException(ex);
		}
	}

}
