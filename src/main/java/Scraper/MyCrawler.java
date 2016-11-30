package Scraper;

import com.uwyn.jhighlight.tools.FileUtils;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import javax.swing.text.html.HTML;
import java.io.*;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pyoung on 2016-11-29.
 */
public class MyCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|jpeg|png|mp3|mp3|zip|gz))$");

    // For now lets restrict crawling to artsci pages
    private final static String DOMAIN = "http://www.concordia.ca/artsci/";
    private final static String HTML_FOLDER = "src/html/";
    private final static String FILE_NAME_PATTERN_STRING = "(?:[^/][\\d\\w\\.]+)$(?<=\\.\\w{3,4})";
    private final static String FOLDER_NAME_PATTERN_STRING = "http(?:s?):\\/\\/([\\w]+\\.{1}[\\w]+\\.?[\\w]+)+\\/artsci\\/(\\w+)";
    private final static Pattern FILE_NAME_PATTERN = Pattern.compile(FILE_NAME_PATTERN_STRING);
    private final static Pattern FOLDER_NAME_PATTERN = Pattern.compile(FOLDER_NAME_PATTERN_STRING);

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.concordia.ca/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith(DOMAIN);
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            // lets save our html pages for now
            Matcher file = FILE_NAME_PATTERN.matcher(url);
            Matcher folder = FOLDER_NAME_PATTERN.matcher(url);
            file.find();
            folder.find();
            String folderName = HTML_FOLDER + folder.group(2);
            makeDirectory(folderName);
            File htmlFile = new File(folderName + "/" + file.group());

            try {
                FileWriter htmlWriter = new FileWriter(htmlFile, false);
                htmlWriter.write(html);
                htmlWriter.flush();
                htmlWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());
        }
    }

    private void makeDirectory(String folderName) {
        File dir = new File(folderName);

        if (!dir.exists()) {

            try{
                dir.mkdir();
            }
            catch(SecurityException se){
                //handle it
            }
        }
    }
}
