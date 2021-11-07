package org.peut.herdenk.controller;

import org.peut.herdenk.exceptions.FileNotFoundException;
import org.peut.herdenk.model.Reaction;
import org.peut.herdenk.model.dto.ReactionRequestDto;
import org.peut.herdenk.model.dto.ReactionResponseDto;
import org.peut.herdenk.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping( path = "/api/v1/reactions")
public class ReactionController {

    private final ReactionService reactionService;

    @Autowired
    public ReactionController(ReactionService reactionService ) {
        this.reactionService = reactionService;
    }

    @GetMapping
    public ResponseEntity<List<ReactionResponseDto>> getReactions() {

        return new ResponseEntity<>( reactionService.getReactions()
                .stream()
                .map( (ReactionResponseDto::from) )
                .collect( Collectors.toList()) ,
                HttpStatus.OK);
    }

    @GetMapping(path = "/{graveId}")
    public ResponseEntity<List<ReactionResponseDto>> getReactions( @PathVariable("graveId") Long graveId ){
        return new ResponseEntity<>( reactionService.getReactionsForGrave( graveId )
                .stream()
                .map( (ReactionResponseDto::from) )
                .collect( Collectors.toList()) ,
                HttpStatus.OK);
    }

    @PostMapping(path = "/{graveId}",  consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    public ResponseEntity<List<ReactionResponseDto>> saveReaction(
                 @PathVariable("graveId") Long graveId,
                 @RequestPart(value = "media",required = false) MultipartFile multipartFile,
                 @RequestPart(value = "reaction",required = false) ReactionRequestDto request
                 ) {

        Reaction reaction = Reaction.from( request );

        return new ResponseEntity<>( reactionService.registerReaction(
                reaction,
                multipartFile)
                .stream()
                .map( (ReactionResponseDto::from) )
                .collect( Collectors.toList()) ,
                HttpStatus.OK);
    }

    @DeleteMapping( path="/{graveId}/{reactionId}" )
    public ResponseEntity<List<ReactionResponseDto> > deleteReaction(
            @PathVariable("graveId") Long graveId,
            @PathVariable("reactionId") Long reactionId ){

        return new ResponseEntity<>(  reactionService.deleteReaction( reactionId )
                .stream()
                .map( (ReactionResponseDto::from) )
                .collect( Collectors.toList()) ,
                HttpStatus.OK);

    }

    @PostMapping(path = "/permission/{graveId}/{permission}" )
    public ResponseEntity<List<ReactionResponseDto>> askPermission(
            @PathVariable("graveId") Long graveId,
            @PathVariable("permission") String permission ){
        return new ResponseEntity<>( reactionService.askPermission( graveId, permission )
                .stream()
                .map( (ReactionResponseDto::from) )
                .collect( Collectors.toList()) ,
                HttpStatus.OK);
    }

    @GetMapping(path = "/permission/{graveId}" )
    public ResponseEntity<List<ReactionResponseDto>> getPermission(
            @PathVariable("graveId") Long graveId){
        return new ResponseEntity<>( reactionService.findPermissionQuestions( graveId )
                .stream()
                .map( (ReactionResponseDto::from) )
                .collect( Collectors.toList()) ,
                HttpStatus.OK);
    }


}
