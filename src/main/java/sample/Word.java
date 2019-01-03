package sample;

import technology.tabula.TextElement;

public class Word{  
	
	public Word(TextElement startLetter, TextElement endLetter, String content) {
		this.startLetter = startLetter;
		this.endLetter = endLetter;
		this.content = content;
	}
	public Word(Word word) {
		super();
		this.startLetter = word.startLetter;
		this.endLetter = word.endLetter;
		this.content = word.content;
	}
	TextElement startLetter;
	TextElement endLetter;
	public String content;
	public Word(String str) {
		this.content = new String(str);
	}
	public Word() {
		this.content = new String("");
	}
	public Word(StringBuilder word) {
		this.content = new String(word);
	}
	@Override
	public String toString() {
		return this.content+ " Starts at : "+ startLetter.getX()+ ", and ends at :"+ (endLetter.getX()+endLetter.getWidth());}
	
}
