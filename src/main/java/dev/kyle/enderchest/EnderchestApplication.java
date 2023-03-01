package dev.kyle.enderchest;

import dev.kyle.enderchest.config.EnderchestConfig;
import dev.kyle.enderchest.update.Updater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KyleS1872
 */
@SpringBootApplication(scanBasePackages = {"dev.kyle.enderchest"})
public class EnderchestApplication {

    private static String[] args;
    private final EnderchestConfig enderchestConfig;

    @Autowired
    public EnderchestApplication(EnderchestConfig enderchestConfig){
        this.enderchestConfig = enderchestConfig;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EnderchestApplication.class);

        EnderchestApplication.args = args;

        Map<String, Object> properties = new HashMap<>();
        properties.put("server.port", "8010");
        properties.put("maps", (System.getProperty("os.name").startsWith("Windows") ? "C:" : File.separator + "home" + File.separator + "mineplex") +  File.separator + "update" + File.separator + "maps" + File.separator);
        properties.put("update", (System.getProperty("os.name").startsWith("Windows") ? "C:" : File.separator + "home" + File.separator + "mineplex") + File.separator + "update" + File.separator);
        properties.put("updateScript", (System.getProperty("os.name").startsWith("Windows") ? "C:" : File.separator + "home" + File.separator + "mineplex") + File.separator + "updateMicroservice.sh");

        app.setDefaultProperties(properties);
        app.run(args);
    }

    @PostConstruct
    private void autoUpdate() {
        String path = EnderchestApplication.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
                .replaceFirst("(?s)(.*)" + ".jar!/BOOT-INF/classes!/", "$1");

        new Updater(path.substring(path.lastIndexOf("/") + 1), args, enderchestConfig);
    }

}
