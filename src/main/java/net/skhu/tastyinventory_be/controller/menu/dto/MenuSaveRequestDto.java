package net.skhu.tastyinventory_be.controller.menu.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuSaveRequestDto {

    private String name;
    private Long price;

    @Builder
    public MenuSaveRequestDto(String name, Long price) {
        this.name = name;
        this.price = price;
    }

}