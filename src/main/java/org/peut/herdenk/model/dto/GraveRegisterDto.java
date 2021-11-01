package org.peut.herdenk.model.dto;


import org.peut.herdenk.model.Grave;

import java.util.Date;

public class GraveRegisterDto {


    private String  occupantFullName;
    private Boolean publicAccess;


    public static GraveRegisterDto from(Grave grave, Boolean access){
            GraveRegisterDto graveRegisterDto = new GraveRegisterDto();
            graveRegisterDto.setOccupantFullName( grave.getOccupantFullName());
            graveRegisterDto.setPublicAccess( access  );

            return graveRegisterDto;
        }

    public String getOccupantFullName() {
        return occupantFullName;
    }

    public void setOccupantFullName(String occupantFullName) {
        this.occupantFullName = occupantFullName;
    }

    public Boolean getPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(Boolean publicAccess) {
        this.publicAccess = publicAccess;
    }
}
