package dev.kyle.enderchest.update;

import dev.kyle.enderchest.config.EnderchestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author KyleS1872
 */
public class Updater {

    private String _name;
    private String _hash;
    private String _updateFolder;
    private String _updateScript;

    private boolean updating = false;
    private final String[] arguments;

    private final Logger logger = LoggerFactory.getLogger(Updater.class);

    public Updater(String name, String[] arguments, EnderchestConfig enderchestConfig){
        this.arguments = arguments;

        if(new File("IgnoreUpdates.dat").exists()){
            logger.info("Auto Updater: Disabled");
            return;
        }

        _name = name;
        _hash = getHash(name + ".jar");
        _updateFolder = enderchestConfig.getUpdateFolder();
        _updateScript = enderchestConfig.getUpdateScript();

        if(_hash == null){
            logger.warn("Auto Updater: Disabled (Failed to get Jar Hash)");
            return;
        }

        logger.info("Auto Updater: Enabled");

        Runnable updateRunnable = this::checkForUpdates;

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(updateRunnable, 0, 1, TimeUnit.MINUTES);
    }

    private String getHash(String filePath){
        try{
            String path = new File(filePath).getAbsolutePath();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(path)));
            byte[] digest = md.digest();
            return Arrays.toString(digest);
        }catch(Exception e){
            return null;
        }
    }

    private void checkForUpdates(){
        File file = new File(_updateFolder + _name + ".jar");

        if (!file.exists())
            return;

        String hash = getHash(file.getAbsolutePath());
        if (hash != null && !hash.equals(_hash) && !updating)
            update();
    }

    private void update(){
        logger.info("Ohhh, a shiny new update was found!");
        logger.info("Attempting to update...");

        updating = true;

        //Delay 10 seconds in case jar is being uploaded and returns as a corrupt file
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(this::executeUpdate, 10, TimeUnit.SECONDS);
    }

    private void executeUpdate(){
        String cmd = _updateScript;
        String apiName = _name;

        ArrayList<String> args = new ArrayList<>();
        args.add("/bin/sh");
        args.add(cmd);
        args.add(apiName);

        StringBuilder inputArgs = new StringBuilder();
        for(String arg : arguments){
            if(inputArgs.toString().equalsIgnoreCase(""))
                inputArgs = new StringBuilder("\"" + arg + "\"");
            else
                inputArgs.append(" \"").append(arg).append("\"");
        }

        args.add(inputArgs.toString());

        ProcessBuilder _processBuilder = new ProcessBuilder(args);

        try{
            Process process = _processBuilder.start();
            process.waitFor();
        }catch(Exception e){
            logger.error("There was an error trying to update!");
            e.printStackTrace();
            logger.info("A manual update will be required for any new changes!");
        }
    }

}
