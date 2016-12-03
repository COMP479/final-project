package Scraper;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Created by pyoung on 2016-11-29.
 */
public class ScraperController {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "src/data/crawl/root";

//        when we want to accept params uncomment this
//        if (args.length != 2) {
//            System.out.print("Needed parameters: ");
//            System.out.print("rootFolder (it will contain intermediate crawl data)");
//            System.out.print("numberOfPages (number of concurrent threads)");
//            return;
//        }

        // the max number of pages to fetch can be changed later to be a arg
        //        String crawlStorageFolder = args[0];
        //       maxPagesToFetch  = Integer.parseInt(args[1]);
        int maxPagesToFetch = 1000;
        int numberOfCrawlers = 7;
//        int maxDepthOfCrawling = 1;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxPagesToFetch(maxPagesToFetch);
//        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setIncludeBinaryContentInCrawling(false);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://www.concordia.ca/artsci/biology.html");
        controller.addSeed("http://www.concordia.ca/artsci/chemistry.html");
        controller.addSeed("http://www.concordia.ca/artsci/exercise-science.html");
        controller.addSeed("http://www.concordia.ca/artsci/geography-planning-environment.html");
        controller.addSeed("http://www.concordia.ca/artsci/math-stats.html");
        controller.addSeed("http://www.concordia.ca/artsci/physics.html");
        controller.addSeed("http://www.concordia.ca/artsci/psychology.html");
        controller.addSeed("http://www.concordia.ca/artsci/science-college.html");

        // the mystery page
        controller.addSeed("http://www.concordia.ca/artsci/science-college/about/life-at-the-college.html");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
    }
}
