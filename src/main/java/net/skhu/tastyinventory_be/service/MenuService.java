package net.skhu.tastyinventory_be.service;

import lombok.RequiredArgsConstructor;
import net.skhu.tastyinventory_be.controller.inventory.dto.response.InventoryResponseDto;
import net.skhu.tastyinventory_be.controller.menu.dto.request.MenuRequestDto;
import net.skhu.tastyinventory_be.controller.menu.dto.response.MenuResponseDto;
import net.skhu.tastyinventory_be.domain.inventory.InventoryRepository;
import net.skhu.tastyinventory_be.domain.menu.Menu;
import net.skhu.tastyinventory_be.domain.menu.MenuRepository;
import net.skhu.tastyinventory_be.domain.recipe.Recipe;
import net.skhu.tastyinventory_be.domain.recipe.RecipeRepository;
import net.skhu.tastyinventory_be.exception.ErrorCode;
import net.skhu.tastyinventory_be.exception.model.NotFoundException;
import net.skhu.tastyinventory_be.external.client.aws.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final RecipeRepository recipeRepository;
    private final InventoryRepository inventoryRepository;
    private final S3Service s3Service;

    @Transactional
    public void createMenu(MultipartFile image, MenuRequestDto requestDto) {
        String imageUrl = s3Service.uploadImage(image, "menu");

        Menu menu = Menu.builder().imageUrl(imageUrl).name(requestDto.getName()).build();

        menuRepository.save(menu);

        List<Recipe> recipes = requestDto.getRelatedInventory().stream()
                .map(
                        r -> Recipe.builder()
                                .usage(r.getInventoryUsage())
                                .menu(menu)
                                .inventory(inventoryRepository.findById(
                                        r.getInventoryId()).orElseThrow(
                                                () -> new NotFoundException(
                                                        ErrorCode.NOT_FOUND_INVENTORY_EXCEPTION,
                                                        ErrorCode.NOT_FOUND_INVENTORY_EXCEPTION.getMessage()))
                                ).build()).toList();


        recipes.stream().map(recipeRepository::save).close();
    }

    public List<MenuResponseDto> findAllMenu() {
        List<Menu> menuList = menuRepository.findAll();
        List<MenuResponseDto> result = new ArrayList<>();
        for (Menu menu : menuList) {
            result.add(
                    MenuResponseDto.of(
                            menu.getName(),
                            menu.getImageUrl(),
                            recipeRepository.findAllByMenu(menu).stream()
                                    .map(
                                            recipe -> InventoryResponseDto.from(
                                                    recipe.getInventory()))
                                    .collect(Collectors.toList())));
        }
        return result;
    }

    public MenuResponseDto findOneMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new NotFoundException(
                        ErrorCode.NOT_FOUND_MENU_EXCEPTION,
                        ErrorCode.NOT_FOUND_MENU_EXCEPTION.getMessage()
                ));

        return MenuResponseDto.of(
                menu.getName(),
                menu.getImageUrl(),
                recipeRepository.findAllByMenu(menu).stream().map(
                        recipe -> InventoryResponseDto.from(
                                recipe.getInventory()))
                        .collect(Collectors.toList()));
    }

}
