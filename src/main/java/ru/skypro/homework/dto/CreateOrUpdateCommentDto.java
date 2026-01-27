package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.util.OpenApiConstant;

@Data
@Builder
@ApiModel(value = "CreateOrUpdateComment", description = "Модель для создания или обновления комментария")
public class CreateOrUpdateCommentDto {

    @NotBlank
    @Size(min = 8, max = 64)
    @ApiModelProperty(value = "Текст комментария", example = OpenApiConstant.TEXT)
    private String text;

    @JsonCreator
    public CreateOrUpdateCommentDto(@JsonProperty("text") String text) {
        this.text = text;
    }

    // Аннотация @JsonCreator из библиотеки Jackson обозначает конструктор или статический фабричный метод, который
    // будет использоваться для создания объекта при десериализации JSON. Это особенно полезно для:
    // - классов с неизменяемыми полями без конструктора по умолчанию;
    // - точного связывания полей JSON с параметрами конструктора или метода;
    // - тонкой настройки процесса десериализации при сложных или нестандартных структурах данных.
    // Чаще всего вместе с @JsonCreator используют аннотацию @JsonProperty для указания, какое поле JSON соответствует
    // какому параметру конструктора. Это позволяет Jackson правильно распарсить JSON и создать объект с нужными значениями.

    // Пример использования:
    // public class User {
    //    private final String name;
    //    private final int age;
    //
    //    @JsonCreator
    //    public User(@JsonProperty("name") String name, @JsonProperty("age") int age) {
    //        this.name = name;
    //        this.age = age;
    //    }
    //
    //    // геттеры
    // }
    // При десериализации Jackson вызовет этот конструктор, передав значения из JSON по именам полей name и age.
    // Таким образом, @JsonCreator указывает Jackson, какой конструктор или фабричный метод использовать для создания
    // экземпляра класса из JSON, обеспечивая контролируемую и корректную десериализацию.
}
