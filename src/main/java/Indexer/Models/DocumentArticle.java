package Indexer.Models;


public class DocumentArticle {
	private int id;
	private Content content;
	
	public DocumentArticle() {
		this.id = 0;
	}
	
	public DocumentArticle(int id, Content content) {
		super();
		this.id = id;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
}
