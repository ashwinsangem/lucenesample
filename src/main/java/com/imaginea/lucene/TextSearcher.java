package com.imaginea.lucene;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TextSearcher {
	public static void main(String[] args) throws Exception {
        
		File indexDir = new File("/home/ashwin/Desktop/LuceneIndex/");
		String query = (args.length>0)?args[0]:"java";
		int hits = 100;
		TextSearcher searcher = new TextSearcher();
		searcher.searchIndex(indexDir, query, hits);
	}

	private void searchIndex(File indexDir, String queryStr, int maxHits)
	throws Exception {
		FSDirectory directory = FSDirectory.open(indexDir);
		IndexReader ir = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(ir);
		QueryParser parser = new QueryParser(Version.LUCENE_43,
		"contents", new StandardAnalyzer(Version.LUCENE_43));
		Query query = parser.parse(queryStr);
		TopDocs topDocs = searcher.search(query, maxHits);
		ScoreDoc[] hits = topDocs.scoreDocs;
		PrintWriter out = new PrintWriter(new FileWriter("/home/ashwin/Desktop/outputfile.txt"));
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			out.println(d.get("filename"));
		}
		out.close();
		System.out.println("Found " + hits.length);
	}

}
