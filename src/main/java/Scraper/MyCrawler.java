package Scraper;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pyoung on 2016-11-29.
 */
public class MyCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|jpeg|png|mp3|mp3|zip|gz))$");

    // For now lets restrict crawling to artsci pages
    private final static String BIOLOGY = "http://www.concordia.ca/artsci/biology";
    private final static String CHEMISTRY = "http://www.concordia.ca/artsci/chemistry";
    private final static String EXERCISE_SCIENCE = "http://www.concordia.ca/artsci/exercise-science";
    private final static String GEOGRAPHY_PLANNING_ENVIRONMENT = "http://www.concordia.ca/artsci/geography-planning-environment";
    private final static String MATH_STATS = "http://www.concordia.ca/artsci/math-stats";
    private final static String PHYSICS = "http://www.concordia.ca/artsci/physics";
    private final static String PSYCHOLOGY = "http://www.concordia.ca/artsci/psychology";
    private final static String SCIENCE_COLLEGE = "http://www.concordia.ca/artsci/science-college";
    public final static String HTML_FOLDER = "src/html/";
    private final static String FILE_NAME_PATTERN_STRING = "((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)(:([^\\/]*))?((\\/[\\w\\/-]+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(\\?([^#]*))?(#(.*))?";
    private final static String FOLDER_NAME_PATTERN_STRING = "http(?:s?):\\/\\/([\\w]+\\.{1}[\\w]+\\.?[\\w]+)+\\/artsci\\/([\\w-]+)";
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
        return !FILTERS.matcher(href).matches() && (href.startsWith(BIOLOGY) || href.startsWith(CHEMISTRY) || href.startsWith(EXERCISE_SCIENCE) || href.startsWith(GEOGRAPHY_PLANNING_ENVIRONMENT) || href.startsWith(MATH_STATS)
        		|| href.startsWith(PHYSICS) || href.startsWith(PSYCHOLOGY) || href.startsWith(SCIENCE_COLLEGE));
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
//        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            saveHtmlFile(url, html);

//            System.out.println("Text length: " + text.length());
//            System.out.println("Html length: " + html.length());
//            System.out.println("Number of outgoing links: " + links.size());
        }
    }

    private void saveHtmlFile(String url, String html) {
        // lets save our html pages for now
        String folderName = makeHtmlSubDirectory(url);
        File htmlFile = makeHtmlFile(url, folderName);

        try {
            FileWriter htmlWriter = new FileWriter(htmlFile, false);
            htmlWriter.write(html);
            htmlWriter.flush();
            htmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File makeHtmlFile(String url, String folderName) {
        Matcher file = FILE_NAME_PATTERN.matcher(url);
        file.find();
        String filename = file.group(8);
        if(file.group(10) != null)
        {
            filename = filename.replaceAll(".html", "");
            filename = filename + "_" + file.group(10) + ".html";
            filename = filename.replaceAll("[^a-zA-Z0-9.-]", "--");
        }

        return new File(folderName + "/" + filename);
    }

    private String makeHtmlSubDirectory(String url) {
        Matcher folder = FOLDER_NAME_PATTERN.matcher(url);
        folder.find();
        String folderName = HTML_FOLDER + folder.group(2).replaceAll("[^a-zA-Z0-9.-]", "--");
        File dir = new File(folderName);

        if (!dir.exists()) {

            try{
                dir.mkdir();
            }
            catch(SecurityException se){
                //handle it
            }
        }

        return folderName;
    }
}
