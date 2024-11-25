package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.listing.CreateOrUpdateListing;
import ru.skypro.homework.dto.listing.ExtendedListingDTO;
import ru.skypro.homework.dto.listing.ListingDTO;
import ru.skypro.homework.dto.listing.ListingsDTO;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * Интерфейс ListingMapper отвечает за преобразование объектов между
 * сущностями Listing и различными DTO (Data Transfer Objects) с использованием MapStruct.
 *
 * <p>Данный маппер упрощает преобразование данных между слоями приложения,
 * минимизируя объем повторяющегося кода и обеспечивая более чистую архитектуру.</p>
 *
 * <p>Методы:</p>
 * <ul>
 *     <li>{@link #listingDTOToListing(ListingDTO)} - преобразует объект
 *     ListingDTO в объект Listing.</li>
 *     <li>{@link #listingToListingDTO(Listing)} - преобразует объект
 *     Listing в объект ListingDTO.</li>
 *     <li>{@link #toExtendedListing(Listing)} - преобразует объект
 *     Listing в объект ExtendedListingDTO, включая информацию об авторе.</li>
 *     <li>{@link #createOrUpdateListingToListing(CreateOrUpdateListing)} -
 *     преобразует объект CreateOrUpdateListing в объект Listing.</li>
 *     <li>{@link #imageToString(Image)} - преобразует объект Image в строку,
 *     представляющую URL изображения.</li>
 *     <li>{@link #listingListToListings(List<Listing>)} - преобразует список объектов
 *     Listing в объект ListingsDTO, содержащий количество и результаты.</li>
 * </ul>
 *
 * <p>Примечания:</p>
 * <ul>
 *     <li>При преобразовании из ListingDTO в Listing игнорируются поля
 *     {@code image} и {@code author}.</li>
 *     <li>При преобразовании из Listing в ListingDTO используется поле
 *     {@code author.id} для установки автора, а также поле {@code image}
 *     для получения URL изображения.</li>
 *     <li>Метод {@code imageToString} возвращает строку по адресу изображения
 *     или {@code null}, если изображение отсутствует.</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface ListingMapper {

    String address = "/ads/image";

    @Mapping(target = "id", source = "pk")
    @Mapping(target = "author.id", source = "author")
    @Mapping(target = "image", ignore = true)
    Listing listingDTOToListing(ListingDTO dto);


    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageToString")
    ListingDTO listingToListingDTO(Listing entity);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageToString")
    ExtendedListingDTO toExtendedListing(Listing entity);



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    Listing createOrUpdateListingToListing(CreateOrUpdateListing dto);

    @Named("imageToString")
    default String imageToString(Image image) {
        if (image == null) {
            return null;
        }
        return address + image.getId();
    }

    default ListingsDTO listingListToListings(List<Listing> list) { //без обратного метода
        ListingsDTO listingsDTO = new ListingsDTO();
        listingsDTO.setCount(list.size());
        List<ListingDTO> listingDTOList = new ArrayList<>();
        for (Listing listing : list) {
            listingDTOList.add(listingToListingDTO(listing));
        }
        listingsDTO.setResults(listingDTOList);
        return listingsDTO;
    }
}
