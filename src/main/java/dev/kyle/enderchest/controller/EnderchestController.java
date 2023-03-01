package dev.kyle.enderchest.controller;

import dev.kyle.enderchest.config.EnderchestConfig;
import dev.kyle.enderchest.config.ResourceUrls;
import dev.kyle.enderchest.util.MapType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * @author KyleS1872
 */
@RestController
@SuppressWarnings("ResultOfMethodCallIgnored")
@RequestMapping(ResourceUrls.BASE_RESOURCE_URI)
public class EnderchestController {

    private final EnderchestConfig enderchestConfig;

    @Autowired
    public EnderchestController(EnderchestConfig enderchestConfig) {
        this.enderchestConfig = enderchestConfig;
    }


    @GetMapping(value = ResourceUrls.ENDERCHEST_NEXT_URI)
    public ResponseEntity<Resource> getMap(@PathVariable String mapType) throws IOException {
        MapType type = MapType.getEnum(mapType);

        //Create directory if it does not exist
        File dir = new File(new File(enderchestConfig.getMapsFolder()).getPath() + File.separator + type.getFolderName());
        dir.mkdirs();

        //Check if there are maps
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        //Pick a random map
        Random rand = new Random();
        File file = files[rand.nextInt(files.length)];

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @PostMapping(value = ResourceUrls.ENDERCHEST_UPLOAD_URI)
    public ResponseEntity<String> postMap(HttpServletRequest request, @PathVariable String mapType, @RequestParam String name) {
        MapType type = MapType.getEnum(mapType);

        //Create directory if it does not exist
        File dir = new File(new File(enderchestConfig.getMapsFolder()).getPath() + File.separator + type.getFolderName());
        dir.mkdirs();

        //Check if the file already exists
        File temp = new File(new File(enderchestConfig.getMapsFolder()).getPath() + File.separator + type.getFolderName() + File.separator + name);
        if (temp.exists())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        try {
            Files.copy(request.getInputStream(), Paths.get(new File(enderchestConfig.getMapsFolder()).getPath() + File.separator + type.getFolderName() + File.separator + name));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        //File was accepted and copied
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

}
