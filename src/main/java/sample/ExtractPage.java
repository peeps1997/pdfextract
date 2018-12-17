package sample;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;

public class ExtractPage {
	
	 public static Page getPage(String path, int pageNumber) throws IOException {
	        ObjectExtractor oe = null;
	        try {
	            PDDocument document = PDDocument
	                    .load(new File(path));
	            oe = new ObjectExtractor(document);
	            Page page = oe.extract(pageNumber);
	            return page;
	        } finally {
	            if (oe != null)
	                oe.close();
	        }
	    }
}
