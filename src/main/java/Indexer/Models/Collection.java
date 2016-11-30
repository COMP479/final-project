package Indexer.Models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
