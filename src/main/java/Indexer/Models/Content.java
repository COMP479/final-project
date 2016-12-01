package Indexer.Models;

 
public class Content {
	//Title of the article (unused)
	private String title;
	//Main body of article used for index
	private String body;
	
	public Content() {
	}

	public Content(String title, String body) {
		super();
		this.title = title;
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
