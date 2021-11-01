package org.peut.herdenk.model.dto;


import org.peut.herdenk.model.Grave;
import java.util.Date;

public class GraveSummaryDto {

    private Long    graveId;
    private String  occupantFullName;
    private Date    creationDate;
    private String  access;


    public static GraveSummaryDto from(Grave grave, String access){
            GraveSummaryDto graveSummaryDto = new GraveSummaryDto();
            graveSummaryDto.setGraveId( grave.getGraveId() );
            graveSummaryDto.setOccupantFullName( grave.getOccupantFullName());
            graveSummaryDto.setCreationDate( grave.getCreationDate());
            graveSummaryDto.setAccess( access  );

            return graveSummaryDto;
        }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Long getGraveId() {
        return graveId;
    }

    public void setGraveId(Long graveId) {
        this.graveId = graveId;
    }

    public String getOccupantFullName() {
        return occupantFullName;
    }

    public void setOccupantFullName(String occupantFullName) {
        this.occupantFullName = occupantFullName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
