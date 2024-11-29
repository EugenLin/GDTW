package com.GDTW.general.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import java.io.File;
import java.net.MalformedURLException;

@Service
public class SitemapService {

    @Value("${app.fullUrl}")
    private String fullUrl;

    public void generateSitemap() throws MalformedURLException {
        // Determine the base directory (current working directory)
        String baseDir = System.getProperty("user.dir");

        // Create the logs directory path
        String logsDirPath = baseDir + File.separator + "logs";

        // Create a File object for the logs directory
        File logsDir = new File(logsDirPath);

        // Check if the logs directory exists; if not, create it
        if (!logsDir.exists()) {
            logsDir.mkdirs();  // Create logs directory if it does not exist
        }

        // Build the sitemap generator with the logs directory as the output location
        WebSitemapGenerator sitemapGen = WebSitemapGenerator.builder(fullUrl, logsDir)
                .build();

        // Add URLs to the sitemap
        sitemapGen.addUrl("https://gdtw.org/");
        sitemapGen.addUrl("https://gdtw.org/index");
        sitemapGen.addUrl("https://gdtw.org/short_url");
        sitemapGen.addUrl("https://gdtw.org/short_url_redirection");
        sitemapGen.addUrl("https://gdtw.org/terms_of_service");
        sitemapGen.addUrl("https://gdtw.org/about_us");
        sitemapGen.addUrl("https://gdtw.org/contact_us");
        sitemapGen.addUrl("https://gdtw.org/error");
        sitemapGen.addUrl("https://gdtw.org/image_view.html");
        sitemapGen.addUrl("https://gdtw.org/image_share.html");
        sitemapGen.addUrl("https://gdtw.org/show_statistics.html");

        // Write the sitemap.xml file to the logs directory
        sitemapGen.write();
    }
}
