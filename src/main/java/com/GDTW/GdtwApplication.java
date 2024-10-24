package com.GDTW;

import com.GDTW.general.service.RedisService;
import com.GDTW.general.service.ScheduledTaskService;
import com.GDTW.general.service.SitemapService;
import com.GDTW.shorturl.model.ShortUrlService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class GdtwApplication {

    private static final Logger logger = LoggerFactory.getLogger(GdtwApplication.class);

    private final SitemapService sitemapService;
    private final RedisService redisService;
    private final ScheduledTaskService scheduledTaskService;
    private final ShortUrlService shortUrlService;

    public GdtwApplication(SitemapService sitemapService, RedisService redisService, ScheduledTaskService scheduledTaskService, ShortUrlService shortUrlService) {
        this.sitemapService = sitemapService;
        this.redisService = redisService;
        this.scheduledTaskService = scheduledTaskService;
        this.shortUrlService = shortUrlService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GdtwApplication.class, args);
        logger.info("Web version: " + WebVersion.getWebVersion() + " and built at " + WebVersion.getBuildDate());

    }

    // Generate sitemap on application startup
    @PostConstruct
    public void onStartup() {
        try {
            sitemapService.generateSitemap();
            redisService.clearRedis();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
