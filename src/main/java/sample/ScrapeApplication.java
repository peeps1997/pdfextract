package sample;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.Rectangle;
import technology.tabula.Ruling;
import technology.tabula.TextChunk;
import technology.tabula.TextElement;
import technology.tabula.detectors.NurminenDetectionAlgorithm;
import sample.Word;
public class ScrapeApplication {
	static boolean isNewLine=false;
	static boolean isNewWord=false;
	static List<Word> wordList = new ArrayList<Word>();
	static List<Word> tableList = new ArrayList<Word>();
	public static void main(String...strings) {
		String newLine = new String("");
		String path = "src/main/java/sample/Legacy.pdf";
		boolean newWord=true;
		DocumentBuilder builder;
		File file = new File(path); 
		StringBuilder str = new StringBuilder();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();		
		NurminenDetectionAlgorithm detectionAlgorithm = new NurminenDetectionAlgorithm();
		try {
			
			PDDocument document = PDDocument.load(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			int pageNumber = 1;
			pageNumber =  getPhrasePageNumber("Portfolio Overview",document);
			Rectangle rect = new Rectangle (0f,0f,1000f,1000f); 		// Cover the whole page
//			Rectangle rect = new Rectangle( 424.193f,54.315f,585.99f,274.635f);// PortFolio Overview
//			Rectangle rect = new Rectangle(230,50,260,60);			//Acitivity Summary
//			 Rectangle(float top, float left, float width, float height) 
			Page page = ExtractPage.getPage(path, pageNumber);
			TextElement prevText = page.getArea(rect).getText().get(0);
//			Rectangle boundRect = page.getArea(rect).getTextBounds();
			List<Ruling> allRuling = page.getRulings();
			List<TextChunk>textChunkList;
			int i=0;
			TextElement startLetter=page.getArea(rect).getText().get(0);
			TextElement endLetter;
			for(TextElement text:page.getArea(rect).getText()) {
				
//				System.out.println(i++);
//				TextElement nextText = page.getArea(rect).getText().get(text.)
				if(text.getHeight()>4) {
//				if(text.getY()-prevText.getY()>10){
//					System.out.println("");
//					setIsNewLine(true);
//					 }
				if(newWord==true) {
					 startLetter = prevText;
					
//					System.out.println("first letter:"+prevText.getText());
					newWord=false;
				}
				str.append(prevText.getText());
//				System.out.println(str.content);
				if(getIsNewWord(prevText,text)) {
					if(str.length()>1) {
						str.deleteCharAt(0);
					}
//					System.out.println(str);
					 endLetter = prevText;
//					System.out.println("first letter:"+startLetter.getText());
					wordList.add(new Word(startLetter, endLetter, str.toString()));
					str.replace(0, str.length()==0?0:str.length()-1, "");
//					System.out.println("ending letter:"+endLetter.getText());
					newWord=true;
					}
//				if(getIsNewLine()&&getIsNewWord(prevText,text)) {
//					System.out.print("\t");
//				}
//				System.out.println(text);
//				System.out.print(text.getText());
				prevText = text;
				setIsNewLine(false);
//				System.out.println("Rectangular bounds:"+ boundRect.toString());
//				System.out.println(textChunk.getText());
				}
			}
			Word sampleWord = new Word();
			for(Word wordRead:wordList) {
			if(wordRead.content.equals("Portfolio")) {
				sampleWord = wordRead;
			}}
			Ruling nearestRuling = getNearestRuling(page,sampleWord);
			List<Ruling> verticalRuling = new ArrayList<Ruling>();
			for(Ruling ruling :allRuling) 
				if(ruling.vertical()) 
					verticalRuling.add(ruling);
//			allRuling.removeAll(verticalRuling);		                                                            		
//			ITERATE WORLD LIST
//			for(Ruling ruling :allRuling) {
//				if(ruling.horizontal())
//				System.out.println(""+ruling);}
			System.out.println("NEAREST RULING TO :\""+sampleWord.content+"\" is :"+nearestRuling);
			System.out.println("********");
			System.out.println("RULING WIDTH :"+nearestRuling.length());
			double tableWidth = nearestRuling.length();
			Rectangle tableRect = new Rectangle (sampleWord.startLetter.getTop(),(float)sampleWord.startLetter.getX(), (float)nearestRuling.length(),300f);
			for(TextElement text:page.getArea(tableRect).getText()) {
			
				if(text.getHeight()>4) {
				if(text.getY()-prevText.getY()>10){
					System.out.println("");
					setIsNewLine(true);
					 }
				if(newWord==true) {
					 startLetter = prevText;
//					System.out.println("first letter:"+prevText.getText());
					newWord=false;
				}
				str.append(prevText.getText());
//				System.out.println(str.content);
				if(getIsNewWord(prevText,text)) {
					if(str.length()>1) {
						str.deleteCharAt(0);
					}
//					System.out.println(str);
					 endLetter = prevText;
//					System.out.println("first letter:"+startLetter.getText());
					tableList.add(new Word(startLetter, endLetter, str.toString()));
					str.replace(0, str.length()==0?0:str.length()-1, "");
//					System.out.println("ending letter:"+endLetter.getText());
					newWord=true;
					}
				prevText = text;
				setIsNewLine(false);
				}
			}
//					 Rectangle(float top, float left, float width, float height) 
			for(Word wordRead:tableList) {
//				System.out.print(wordRead.startLetter.getX()+"\t");
//				System.out.print(wordRead.startLetter.getY()+"\t");
//				System.out.print(wordRead.startLetter.getBottom()+"\t");
				System.out.println(wordRead.content);
//				System.out.println("\t"+wordRead.endLetter.getRight());
			}
			
			
			
			
//			System.out.println(stream.toString());
//			do {
//				stream = 
//				System.out.println(stream.toString());
//				System.out.println("NEWLINE");
//				System.out.println("");
//			}while(!stream.toString().isEmpty());
//			System.out.println(text);
//			document.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	static boolean getIsNewLine() {
		return isNewLine;
	}
	static void setIsNewLine(boolean bool) {
		isNewLine = bool;
	}
	static boolean getIsNewWord(TextElement prevText, TextElement currText) {
		if((currText.getX()-prevText.getX()-prevText.getWidth()<1)&&(currText.getX()-prevText.getX()-prevText.getWidth()>-1)) {
//			System.out.print(prevText.getX()+prevText.getWidth()+"prev\t");
//			System.out.print(currText.getX()+"curr\t");
			isNewWord=false;
		}
		else if(prevText.getX()==currText.getX() && prevText.getY()==currText.getY() ) {
//			System.out.println("false");
			isNewWord= false;}
		else {
//			System.out.println(prevText.getX()+prevText.getWidth()+"prev");
//			System.out.println(currText.getX()+"curr");
//			System.out.println(prevText.getX()+prevText.getWidth()-currText.getX()+"diff");
			isNewWord= true;}
		return isNewWord;
	}
	
	static int getPhrasePageNumber(String phrase,PDDocument document) {
		int pageNumber=-1;
		int totalPages = document.getNumberOfPages();
		PDPage page;
		try {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			for(int i=1;i<=totalPages;i++) {
				page = 	document.getPage(i);
				pdfStripper.setStartPage(i);
				pdfStripper.setEndPage(i);
				if(page.hasContents()) {
					String temp = pdfStripper.getText(document);
					if(temp.contains(phrase.trim())) {
					pageNumber = i;
					break;
				}}
				}
			}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageNumber;
	}
	public static double distance(double x1, double x2,double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
	public static Ruling getNearestRuling(Page page, Word word) {
		
		List<Ruling> allRuling = page.getRulings();
		Ruling nearestRuling = allRuling.get(0);
		double wordBaseY = word.startLetter.getBottom();
		double wordBaseX = word.startLetter.getX();
		double leastDistance = distance(wordBaseX, nearestRuling.getX1(),wordBaseY,nearestRuling.getY1());
		for(Ruling ruling :allRuling) 
			{if(ruling.horizontal()) {
				if(leastDistance>distance(wordBaseX, ruling.getX1(),wordBaseY,ruling.getY1())) {
					nearestRuling = ruling;
					leastDistance=distance(wordBaseX, ruling.getX1(),wordBaseY,ruling.getY1());
				}
			} 
			}
		System.out.println("Distance btw the Coords:"+ leastDistance);
		return nearestRuling;
	}
	
	

}
