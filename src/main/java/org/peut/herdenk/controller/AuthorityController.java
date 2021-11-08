package org.peut.herdenk.controller;

import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.model.Authority;
import org.peut.herdenk.model.AuthorityKey;
import org.peut.herdenk.model.dto.AuthorityDto;
import org.peut.herdenk.service.AuthorityService;
import org.peut.herdenk.utility.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping( path = "/api/v1/authorities")
public class AuthorityController {

    private final AuthorityService authorityService;

    @Autowired
    public AuthorityController(AuthorityService authorityService ) {
        this.authorityService = authorityService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<AuthorityDto>> getAuthorities() {

        List<Authority>  authorities = authorityService.getAuthorities();
        List<AuthorityDto>  authorityDtos = authorities.stream().map( (AuthorityDto::from) ).collect( Collectors.toList());

        return new ResponseEntity<>( authorityDtos, HttpStatus.OK);
    }

    @GetMapping( path="/{userId}/{graveId}" )
    public ResponseEntity<AuthorityDto> getOneAuthority(
            @PathVariable("userId") Long userId,
            @PathVariable("graveId") Long graveId )
    {

        AuthorityKey authorityKey = new AuthorityKey( userId, graveId);
        Authority authority = authorityService.getAuthority( authorityKey );
        AuthorityDto authorityDto = AuthorityDto.from( authority );

        return new ResponseEntity<>(authorityDto,HttpStatus.OK);
    }

    @GetMapping( path="/user/{userId}" )
    public ResponseEntity<List<AuthorityDto>> getAuthoritiesForUser(
            @PathVariable("userId") Long userId  )
    {

        List<Authority> authorities = authorityService.getAuthoritiesByUser( userId );
        List<AuthorityDto> authorityDtos = authorities.stream().map( (AuthorityDto::from) ).collect( Collectors.toList());
        return new ResponseEntity<>(authorityDtos,HttpStatus.OK);
    }

    @GetMapping( path="/grave/{graveId}" )
    public ResponseEntity<List<AuthorityDto>> getAuthoritiesForGrave(
            @PathVariable("graveId") Long graveId  )
    {

        List<Authority> authorities = authorityService.getAuthoritiesByGrave( graveId );
        List<AuthorityDto> authorityDtos = authorities.stream().map( (AuthorityDto::from) ).collect( Collectors.toList());
        return new ResponseEntity<>(authorityDtos,HttpStatus.OK);

    }

    @PostMapping(path = "/grave/{graveId}/{userId}/{access}" )
    public ResponseEntity<AuthorityDto> registerAuthority(
            @PathVariable("userId") Long userId,
            @PathVariable("graveId") Long graveId,
            @PathVariable("access") String access
            )
    {
        if (!Access.isNameValid( access ) )throw new BadRequestException( "Access specifier must be NONE, READ,WRITE or OWNER");
        Authority authority = new Authority(userId, graveId,access);
        authority = authorityService.registerAuthority( authority );

        return new ResponseEntity<>(AuthorityDto.from( authority ), HttpStatus.OK);
    }

    @PutMapping(path = "/grave/{graveId}/{userId}/{access}" )
    public ResponseEntity<AuthorityDto> updateAuthority(
            @PathVariable("userId") Long userId,
            @PathVariable("graveId") Long graveId,
            @PathVariable("access") String access
            ) {

        Authority authority = new Authority( userId, graveId, access  );
        authority = authorityService.updateAuthority( authority );
        return new ResponseEntity<>( AuthorityDto.from( authority ), HttpStatus.OK);
    }

    @DeleteMapping( path="/{userId}/{graveId}" )
        public ResponseEntity<AuthorityDto> getAuthority(
                @PathVariable("userId") Long userId,
                @PathVariable("graveId") Long graveId ) {

        AuthorityKey authorityKey  = new AuthorityKey(userId, graveId);

        Authority authority = authorityService.deleteAuthority( authorityKey );
        return new ResponseEntity<>( AuthorityDto.from( authority ), HttpStatus.OK);
    }

}
