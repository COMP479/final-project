package Indexer.Models;

import java.util.List;


public class Collection {
	//List of articles extracted directly from XML
    private List<DocumentArticle> documents = null;
 
    public List<DocumentArticle> getDocuments() {
        return documents;
    }
 
    public void setDocuments(List<DocumentArticle> documents) {
        this.documents = documents;
    }
}
