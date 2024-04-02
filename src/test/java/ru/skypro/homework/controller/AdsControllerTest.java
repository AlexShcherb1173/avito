package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.pro.packaged.S;
import org.apache.logging.log4j.ThreadContext;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.assertj.core.util.diff.DiffUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.filter.MyUserDetails;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import javax.transaction.Transactional;

import static java.nio.file.Files.delete;
import static liquibase.repackaged.net.sf.jsqlparser.util.validation.metadata.NamedObject.user;
import static org.apache.logging.log4j.ThreadContext.get;
import static org.assertj.core.util.diff.DiffUtils.patch;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureJsonTesters
public class AdsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private UserRepository repositoryUsers;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private MyUserDetails myUserDetailsManager;
    @Autowired
    private ImageRepository repositoryImage;

    private UsernamePasswordAuthenticationToken authentication;
    private final MockPart imageFile
            = new MockPart("image", "image", "image".getBytes());
    private final User user = new User();
    private final CreateOrUpdateAdDto createAds = new CreateOrUpdateAdDto();
    private final Ads ads = new Ads();
    private final Image image = new Image();


    @BeforeEach
    void setUp() {
        user.setRole(Role.USER);
        user.setFirstName("test FirstName");
        user.setLastName("test LastName");
        user.setPhone("+7123456789");
        user.setUsername("test@test.test1");
        user.setPassword(encoder.encode("test1234"));
        user.setEnabled(true);
        repositoryUsers.save(user);

        UserDetails userDetails = myUserDetailsManager
                .getUsername(user.getUsername());
        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());

        ads.setTitle("Test test");
        ads.setDescription("Test test");
        ads.setPrice(150);
        ads.setUsers(user);
        adsRepository.save(ads);
    }


    @AfterEach
    void cleanUp() {
        adsRepository.delete(ads);
        repositoryUsers.delete(user);
    }

    @Test
    public void testGetAllAds() throws Exception {
        ResultActions $ = mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }


    @Test
    public void testCreateAd() throws Exception {
        createAds.setTitle("Test test test");
        createAds.setDescription("Add Test test");
        createAds.setPrice(150);

        MockPart created = new MockPart("properties",
                objectMapper.writeValueAsBytes(createAds));

        MvcResult result = mockMvc.perform(multipart("/ads")
                        .part(imageFile)
                        .part(created)
                        .with(authentication(authentication)))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.pk").isNotEmpty())
                .andExpect((ResultMatcher) jsonPath("$.pk").isNumber())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AdsDto createdAd = objectMapper.readValue(responseBody, AdsDto.class);
        AdsRepository.deleteById(createdAd.getPk());
    }



    @Test
    public void testDeleteAd() throws Exception {
        Ads adsD = new Ads();
        adsD.setTitle("Test del");
        adsD.setDescription("Test test del");
        adsD.setPrice(250);
        adsD.setUsers(users);
        adsRepository.save(adsD);

        ResultActions resultActions = mockMvc.perform(delete("/ads/{id}", adsD.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isNoContent());
    }


    @Test
    public void updateInfoAd() throws Exception {
        String newTitle = "Ads test";
        String newDesc = "Test test2";
        Integer newPrice = 1555;
        ads.setTitle(newTitle);
        ads.setDescription(newDesc);
        ads.setPrice(newPrice);
        repositoryAds.save(ads);

        ResultActions resultActions = mockMvc.perform(patch("/ads/{id}",
                        ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ads))
                        .with((authentication(authentication))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(newTitle));


    }

    @Test
    public void tesGetAdsByCurrentUser() throws Exception {
        mockMvc.perform(get("/ads/me")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray());
    }



    @Test
    @Transactional
    public void updateImageAd() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch("/ads/{id}/image", ads.getId())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(request -> {
                            request.addPart(imageFile);
                            return request;
                        })
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andReturn();

        ads.setAdImage(null);
        repositoryImage.deleteAllByBytes("image".getBytes());
    }
}


