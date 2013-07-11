package com.imaginea.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class FileIndexer {
	public static void main(String[] args) throws Exception {
		   
		File indexDir = new File("/home/ashwin/Desktop/LuceneIndex/");
		File dataDir = new File("/home/ashwin/Desktop/Work/");
		String[] suffix = {"docx", "doc", "pdf"};
		deleteFolder(indexDir);
		FileIndexer indexer = new FileIndexer();
		
		int numIndex = indexer.index(indexDir, dataDir, suffix);
		
		System.out.println("Total files indexed " + numIndex);
		
	}
	
	private static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { 
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	private int index(File indexDir, File dataDir, String[] suffix) throws Exception {
		IndexWriterConfig	indexConfig = new IndexWriterConfig(Version.LUCENE_43,
				new StandardAnalyzer(Version.LUCENE_43));
		IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexDir),indexConfig);
		
		indexDirectory(indexWriter, dataDir, suffix);
		
		int numIndexed = indexWriter.maxDoc();
		indexWriter.close();
		
		return numIndexed;
	
	}
		
	private void indexDirectory(IndexWriter indexWriter, File dataDir,
		String[] suffix) throws IOException {
		
		File[] files = dataDir.listFiles();
		for (File f: files) {
			if (f.isDirectory()) {
				indexDirectory(indexWriter, f, suffix);
			}
			else {
				indexFileWithIndexWriter(indexWriter, f, suffix);
			}
		}
	
	}
		
	private void indexFileWithIndexWriter(IndexWriter indexWriter, File f,
		String[] suffix) throws IOException {
		long millisec = f.lastModified();
		Date lastModified = new Date(millisec);
		if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) {
			return;
		}
		boolean extnMatch = false;
		for(String s: suffix){
			if(f.getName().endsWith(s)){
				extnMatch = true;
				break;
			}
		}
		if (!extnMatch) {
			return;
		}
		String fullFilePath = f.getCanonicalPath();
		String fileName = fullFilePath.substring(fullFilePath.lastIndexOf("/")+1);
		System.out.println("Indexing file " + fileName);
		DocumentExtractorFactory factory = new DocumentExtractorFactory();
		Document doc = new Document();
		String content = factory.getDocExtractor(fullFilePath).getTextContent();
		doc.add(new TextField("contents", content, Field.Store.YES));
		doc.add(new StringField("filename", fileName, Field.Store.YES));
		indexWriter.addDocument(doc);	
	}

}
