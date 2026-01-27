package ru.skypro.homework.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageUploadService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Api(tags = "Объявления", value = "API для работы с объявлениями")
public class AdController {

    // Аннотация @CrossOrigin(value = "http://localhost:3000") применяется на уровне класса, разрешая кросс-доменные
    // (межсайтовые) запросы с http://localhost:3000 ко всем методам внутри данного контроллера.
    // На указанном адресе развернуто фронтенд-приложение, которое может делать запросы на бэкенд.
    // CORS (Cross-Origin Resource Sharing) — это механизм, использующий HTTP-заголовки, чтобы разрешить веб-приложению
    // из одного источника доступ к определенным ресурсам другого источника. По сути, это ослабление политики одинакового
    // источника, но в контролируемой и безопасной форме.
    // CORS позволяет серверам указывать:
    // Какие источники могут получать доступ к их ресурсам,
    // Какие HTTP-методы разрешены,
    // Какие заголовки можно включать в запросы.
    // Это обеспечивает безопасное выполнение легитимных межсайтовых запросов, защищая от вредоносных атак.
    // Возможна также глобальная конфигурация CORS с WebMvcConfigurer. В классе CorsConfig определена глобальная
    // конфигурация на URL "/image/**"

    // Необходимо попробовать создать глобальный контроллер для обработки ошибок и исключений и попытаться поймать
    // исключение, выброшенное в связи с отсутствием у пользователя прав на совершение определенных действий, например
    // удаление объявления, которое он не создавал. Если у пользователя нет необходимых ролей или прав, метод не будет
    // выполнен, и будет выброшено исключение AccessDeniedException.

    private final AdService adsService;
    private final ImageUploadService imageUploadService;


    @ApiOperation(value = "Получение всех объявлений",
            notes = "Возвращает общее количество и список всех объявлений",
            response = AdsDto.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK")
    })
    @GetMapping("/ads")
    public AdsDto getAds() {
        return adsService.getAds();
    }


    @ApiOperation(value = "Добавление объявления",
            notes = "Позволяет добавить объявление",
            response = AdDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized")
    })
    @PostMapping(value = "/ads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAd(
            @Valid @RequestPart("properties") CreateOrUpdateAdDto createAd,
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.addAd(createAd, image));
    }


    @ApiOperation(value = "Получение полного объявления",
            notes = "Позволяет получить полное объявление по его идентификатору",
            response = ExtendedAdDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @GetMapping("/ads/{id}")
    public ExtendedAdDto getExtendedAd(@PathVariable Long id) {
        return adsService.getExtendedAd(id);
    }


    @ApiOperation(value = "Удаление объявления",
            notes = "Позволяет удалить объявление по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "No Content"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @DeleteMapping("/ads/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Long id) throws IOException {
        try {
            adsService.removeAd(id);
        } catch (AccessDeniedException e) {
            throw new ForbiddenException("Отсутствуют права на удаление запрошенного ресурса");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Обновление информации об объявлении",
            notes = "Позволяет обновить информацию в объявлении",
            response = AdDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @PatchMapping("/ads/{id}")
    public AdDto updateAd(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateAdDto ad) {
        return adsService.updateAd(id, ad);
    }

    @ApiOperation(value = "Получение объявлений авторизованного пользователя",
            notes = "Возвращает список объявлений авторизованного пользователя",
            response = AdsDto.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized")
    })
    @GetMapping("/ads/me")
    public AdsDto getAdsByAuthenticatedUser() {
        return adsService.getAdsByAuthenticatedUser();
    }


    @ApiOperation(value = "Обновление картинки в объявлении",
            notes = "Позволяет обновить картинку в объявлении",
            response = AdDto.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized"),
            @ApiResponse(
                    code = 403,
                    message = "Forbidden"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @PatchMapping(value = "/ads/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdDto updateImageAd(@PathVariable Long id, @RequestPart("image") MultipartFile file) {
        return adsService.updateImageAd(id, file);
    }


    // После загрузки картинки необходимо обновить страницу на frontend. После этого картинка отобразится.
    @ApiOperation(value = "Выгрузка картинки из файловой системы в объявление",
            notes = "Позволяет выгрузить картинку из файловой системы")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK"),
            @ApiResponse(
                    code = 404,
                    message = "Not found")
    })
    @GetMapping(value = "/image/{filePath}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filePath) {
        Resource resource = imageUploadService.getImageForUserOrAd2(filePath);
        // filePath, извлеченный из Ad, содержит только имя файла (без имени папки и "/")
        // в методе сервиса к нему добавляется имя папки и "/"

        if (resource.exists()) {
            String contentType = "application/octet-stream";
            // default
            if (filePath.toLowerCase().endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (filePath.toLowerCase().endsWith(".jpeg") || filePath.toLowerCase().endsWith(".jpg")) {
                contentType = "image/jpeg";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Для того чтобы получить картинку из файловой системы, фронтенд обращается по адресу: localhost:8080/image/{filePath},
    // где в качестве переменной пути получает имя файла картинки (путь к файлу без имени папки).
    // Мы удаляем имя папки из пути к файлу по той причине, чтобы при подставлении фронтендом значения "путь
    // к файлу" в путь localhost:8080/image/{filePath}, избежать получения эндпоинта: /image/images, которого в
    // действительности нет.
    // Заголовок Content-Disposition указывает браузеру, что файл нужно скачать, а не отображать.

}
