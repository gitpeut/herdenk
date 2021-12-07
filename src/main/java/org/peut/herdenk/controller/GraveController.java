package org.peut.herdenk.controller;

import org.peut.herdenk.model.Grave;
import org.peut.herdenk.model.dto.GraveDto;
import org.peut.herdenk.model.dto.GraveRegisterDto;
import org.peut.herdenk.model.dto.GraveSummaryDto;
import org.peut.herdenk.service.GraveService;
import org.peut.herdenk.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping( path = "/api/v1/graves")
@CrossOrigin(origins = "*")
public class GraveController {


    private final GraveService graveService;
    private final ReactionService reactionService;

    @Autowired
    public GraveController(GraveService graveService, ReactionService reactionService) {
        this.graveService = graveService;
        this.reactionService = reactionService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<GraveDto>> getGraves() {

        List<Grave>  graves = graveService.getGraves();
        List<GraveDto> graveDtos = graves.stream().map( (GraveDto::from) ).collect( Collectors.toList());

        return new ResponseEntity<>(graveDtos,HttpStatus.OK);
    }

    @GetMapping(path = "/summary" )
    public ResponseEntity<List<GraveSummaryDto>> getSummaries() {
        List<GraveSummaryDto> graveSummaryDtos = graveService.getGraveSummaries();
        return new ResponseEntity<>(graveSummaryDtos,HttpStatus.OK);
    }

    @GetMapping(path = "/summary/{graveId}" )
    public ResponseEntity<GraveSummaryDto> getSummary(@PathVariable("graveId") Long graveId) {
        GraveSummaryDto graveSummaryDto = graveService.getGraveSummary( graveId );
        return new ResponseEntity<>(graveSummaryDto,HttpStatus.OK);
    }


    @GetMapping( path="/{graveId}" )
    public ResponseEntity<GraveDto> getGrave( @PathVariable("graveId") Long graveId) {

        Grave grave = graveService.getGrave( graveId );
        GraveDto graveDto = GraveDto.from( grave );

        return new ResponseEntity<>(graveDto,HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<GraveDto> registerGrave(@RequestBody final GraveRegisterDto graveRegisterDto){

           Grave grave = Grave.from( graveRegisterDto);
           grave = graveService.registerGrave( grave, graveRegisterDto.getPublicAccess() );

           return new ResponseEntity<>(GraveDto.from( grave ), HttpStatus.OK);
    }

    @PutMapping( path="/{graveId}" )
    public ResponseEntity<GraveDto> updateGrave( @RequestBody final GraveDto graveDto, @PathVariable("graveId") Long graveId ){

        Grave grave = graveService.updateGrave( Grave.from( graveDto ), graveId );
        return new ResponseEntity<>( GraveDto.from( grave ), HttpStatus.OK);
    }

    @DeleteMapping( path="/{graveId}" )
    public ResponseEntity<GraveDto> deleteGrave( @PathVariable("graveId") Long graveId ){
        Grave grave = graveService.deleteGrave( graveId );
        return new ResponseEntity<>( GraveDto.from( grave ), HttpStatus.OK);
    }


}
