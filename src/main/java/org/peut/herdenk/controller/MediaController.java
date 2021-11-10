package org.peut.herdenk.controller;

import org.apache.tika.Tika;
import org.peut.herdenk.exceptions.FileNotFoundException;
import org.peut.herdenk.config.HerdenkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping( path = "/media")
@CrossOrigin(origins = "*")
public class MediaController {

    @Autowired
    private HerdenkConfig herdenkConfig;

    @Autowired
    public MediaController() {
    }

    public Resource loadFileAsResource(String fileName) {
        System.out.println("Finding " + fileName);
        try {
            Resource resource = new FileSystemResource(fileName);
            if( resource.exists() ) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (Exception ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    @GetMapping( path="/{graveId}/{reactionId}/{fileName}" )
    public ResponseEntity<Resource> downloadMedia(@PathVariable("graveId") Integer graveId,
                                                  @PathVariable("reactionId") Integer reactionId,
                                                  @PathVariable("fileName") String fileName){

        Resource resource = null;

        if(fileName !=null && !fileName.isEmpty()) {
            Path directory = Paths.get( herdenkConfig.getUploads() +  graveId + "/" + reactionId + "/").toAbsolutePath().normalize();
            Path filePath  = directory.resolve(fileName).normalize();
            try {
                resource = loadFileAsResource(filePath.toString() );
            } catch (Exception e) {
                throw new FileNotFoundException( "File could not be found");
            }
            String contentType = null;
            try {
                Tika tika = new Tika();
                contentType = tika.detect( resource.getFile() );
            } catch (Exception e) {
                //System.out.println("Could not determine file type.");
            }
            // Fallback to the default content type if type could not be determined
            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .cacheControl(CacheControl.maxAge( 1800, TimeUnit.SECONDS))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
