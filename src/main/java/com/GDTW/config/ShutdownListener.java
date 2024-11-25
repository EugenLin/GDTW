package com.GDTW.config;

import com.GDTW.general.service.RedisService;
import com.GDTW.general.service.ScheduledDailyStatisticService;
import com.GDTW.imgshare.model.ImgShareService;
import com.GDTW.shorturl.model.ShortUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownListener.class);

    private final ScheduledDailyStatisticService scheduledTaskService;
    private final RedisService redisService;
    private final ShortUrlService shortUrlService;
    private final ImgShareService imgShareService;

    public ShutdownListener(ScheduledDailyStatisticService scheduledTaskService, ShortUrlService shortUrlService, RedisService redisService, ImgShareService imgShareService) {
        this.scheduledTaskService = scheduledTaskService;
        this.shortUrlService = shortUrlService;
        this.redisService = redisService;
        this.imgShareService = imgShareService;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            logger.info("Starting shutdown process...");

            // Save daily statistics
            scheduledTaskService.saveStatistics();

            // Sync Redis 'Short URL' usage data to MySQL
            shortUrlService.syncUsageToMySQL();

            // Sync Redis 'Share Image' usage data to MySQL
            imgShareService.syncSiaUsageToMySQL();
            imgShareService.syncSiUsageToMySQL();

            // Clear Redis data
            redisService.clearRedis();

            logger.info("All Redis statistic data are saved into MySQL. Shutdown process completed successfully.");
        } catch (Exception e) {
            logger.error("Error during saving Redis Statistic Data before shutdown process: " + e.getMessage(), e);
        }
    }
}

