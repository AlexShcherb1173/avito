package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.model.AdModel;

import java.util.List;

@Component
public class AdMapper extends MapperAbstract{
    public Ad makeAd(AdModel adModel) {
        Ad result = new Ad();

        result.setImage(getFullImageUrl(adModel.getImage()));
        result.setAuthor(adModel.getAuthor().getId());
        result.setTitle(adModel.getTitle());
        result.setPrice(adModel.getPrice());
        result.setPk(adModel.getId());

        return result;
    }

    public Ads makeAds(List<AdModel> ads) {
        Ads result = new Ads();
        for (AdModel adModel : ads) {
            result.addAd(makeAd(adModel));
        }

        return result;
    }

}
