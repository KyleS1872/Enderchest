package dev.kyle.enderchest.config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author KyleS1872
 */
@SuppressWarnings("FieldCanBeLocal")
@Component("EnderchestConfig")
public class EnderchestConfig {

    private final Logger logger = LoggerFactory.getLogger(EnderchestConfig.class);

    @Getter
    private final String mapsFolder;

    @Getter
    private final String updateFolder;

    @Getter
    private final String updateScript;

    @Autowired
    public EnderchestConfig(@Value("${maps}") String mapsFolder, @Value("${update}") String updateFolder, @Value("${updateScript}") String updateScript) {
        logger.info("==========[ Loading Microservice Configuration ]==========");
        this.mapsFolder = mapsFolder;
        this.updateFolder = updateFolder;
        this.updateScript = updateScript;
        logger.info("Maps Folder: " + mapsFolder);
        logger.info("Update Folder: " + updateFolder);
        logger.info("Update Script: " + updateScript);
    }

}
