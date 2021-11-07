package org.peut.herdenk.model.dto;


import lombok.Data;
import org.peut.herdenk.model.Grave;

import java.util.Date;

@Data
public class GraveRegisterDto {


    private String  occupantFullName;
    private Boolean publicAccess;


    public static GraveRegisterDto from(Grave grave, Boolean access){
            GraveRegisterDto graveRegisterDto = new GraveRegisterDto();
            graveRegisterDto.setOccupantFullName( grave.getOccupantFullName());
            graveRegisterDto.setPublicAccess( access  );

            return graveRegisterDto;
        }

}
