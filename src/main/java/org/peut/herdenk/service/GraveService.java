package org.peut.herdenk.service;

import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.exceptions.DuplicateException;
import org.peut.herdenk.model.Authority;
import org.peut.herdenk.model.Grave;
import org.peut.herdenk.model.dto.GraveSummaryDto;
import org.peut.herdenk.repository.GraveRepository;
import org.peut.herdenk.security.AccessBeans;
import org.peut.herdenk.security.RoleBeans;
import org.peut.herdenk.utility.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GraveService {

    private final GraveRepository graveRepository;
    private final AuthorityService authorityService;


    @Autowired
    public GraveService(
            GraveRepository graveRepository,
            AuthorityService authorityService)
    {
        this.graveRepository = graveRepository;
        this.authorityService = authorityService;

    }

    public List<Grave> getGraves(){
        return graveRepository.findAll();
    }

    public List<Grave> getAccessibleGraves(){
        List<Grave> graves = graveRepository.findAll();
        return graves
                .stream()
                .filter( grave -> authorityService.isGraveAccessibleByUser( grave.getGraveId() ))
                .collect( Collectors.toList());
    }

    public List<GraveSummaryDto> getGraveSummaries(){
        //List<Grave>  graves = getAccessibleGraves();
        List<Grave>  graves = graveRepository.findAll();
        List<GraveSummaryDto> graveSummaries = new ArrayList<>();



        for( Grave grave : graves ){
                graveSummaries.add( GraveSummaryDto.from( grave,
                        authorityService.getGraveUserAccess( grave.getGraveId())));
        }
        return( graveSummaries);
    }

    public GraveSummaryDto getGraveSummary( Long graveId ){

        Optional<Grave>  optionalGrave = graveRepository.findById( graveId );
        if ( optionalGrave.isEmpty() ) throw new BadRequestException("Graf nummer "+ graveId + "kon niet gevonden worden");

        Grave grave = optionalGrave.get();

        return GraveSummaryDto.from( grave, authorityService.getGraveUserAccess( grave.getGraveId() ) );
    }


    public Grave getGrave( Long graveId ){
        return graveRepository.findById( graveId ).orElseThrow( ()->new BadRequestException( String.format("Graf nummer %d bestaat niet", graveId)));
    }


    public Grave registerGrave( Grave grave, Boolean publicAccess){
        Optional<Grave> optionalGrave    = graveRepository.findGraveByOccupantFullName( grave.getOccupantFullName() );
        if ( optionalGrave.isPresent() ){
            throw new DuplicateException(String.format( "Graf nummer %s bestaat al", grave.getOccupantFullName() ));
        }

        grave = graveRepository.save( grave );

        authorityService.setGraveOwner( grave.getGraveId() );

        updateGravePublicAccess( grave.getGraveId(), publicAccess );
        grave.setAuthorities( authorityService.getAuthoritiesByGrave( grave.getGraveId() ) );

        return( grave );
    }

    @Transactional
    public Grave updateGrave( Grave newGrave, Long graveId ){
        Grave originalGrave = getGrave( graveId );

        if ( newGrave.getOccupantFullName() != null ) originalGrave.setOccupantFullName(newGrave.getOccupantFullName());

        originalGrave = graveRepository.save( originalGrave );
        return( originalGrave );
    }

    @Transactional
    public Grave updateGravePublicAccess( Long graveId, boolean publiclyAccessible ){
        Grave grave = getGrave( graveId );

        if ( authorityService.isGravePublic( graveId)  != publiclyAccessible ){
            authorityService.setGravePublic( graveId, publiclyAccessible);
        }
        return( grave );
    }


    public Grave deleteGrave( Long graveId ){

        Grave grave = getGrave( graveId );
        graveRepository.delete( grave );

        return ( grave );
    }

}
