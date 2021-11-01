package org.peut.herdenk.model.dto;


import lombok.Data;
import org.peut.herdenk.model.Authority;
import org.peut.herdenk.model.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class UserPostDto {
    private String  fullName;
    private String  email;
    private boolean enabled;
    private String  password;


    public static ItemDto from(Item item){
        ItemDto itemDto = new ItemDto();
        itemDto.setId( item.getId() );
        itemDto.setSerialNumber( item.getSerialNumber());
        if (Objects.nonNull( item.getCart())){
            itemDto.setPlainCartDto(PlainCartDto.from(item.getCart()));
        }

        return itemDto;
    }
}
