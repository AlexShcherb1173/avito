package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.response.AdResponse;
import ru.skypro.homework.dto.response.AdsResponse;
import ru.skypro.homework.model.Ad;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdsMapper {

    private final AdMapper adMapper;

    public AdsResponse toAdsResponse(List<Ad> ads) {
        List<AdResponse> adResponses = ads.stream().map(adMapper::mappingToDto).toList();
        AdsResponse adsResponse = new AdsResponse();
        adsResponse.setCount(adResponses.size());
        adsResponse.setResults(adResponses);
        return adsResponse;
    }
}
