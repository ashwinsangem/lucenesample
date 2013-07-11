package com.imaginea.lucene;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentExtractorFactory {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	public DocumentExtractor getDocExtractor(String filePath) {
		DocumentExtractor documentExtractor = null;
		boolean supported = Boolean.TRUE;
		do{
			if (filePath.endsWith("doc")) {
				documentExtractor = new WordDocumentExtractor(filePath);
			} else if (filePath.endsWith("docx")) {
				documentExtractor = new DocXExtractor(filePath);
			} else if (filePath.endsWith("pdf")) {
				documentExtractor = new PdfDocumentExtractor(filePath);
			} else {
				LOGGER.log(Level.INFO, "The Application supports only doc, docx and pdf extensions");
				supported = Boolean.FALSE;
			}
		}while(!supported);
		return documentExtractor;
	}
}
