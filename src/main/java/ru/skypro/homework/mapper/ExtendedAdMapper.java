package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.UserModel;

@Component
public class ExtendedAdMapper extends MapperAbstract {
    public ExtendedAd makeExtendedAd(AdModel adModel) {
        ExtendedAd result = new ExtendedAd();

        result.setImage(getFullImageUrl(adModel.getImage()));
        result.setTitle(adModel.getTitle());
        result.setPrice(adModel.getPrice());
        result.setDescription(adModel.getDescription());
        result.setPk(adModel.getId());

        UserModel author = adModel.getAuthor();
        result.setEmail(author.getEmail());
        result.setPhone(author.getPhone());
        result.setAuthorFirstName(author.getFirstName());
        result.setAuthorLastName(author.getLastName());

        return result;
    }
}
