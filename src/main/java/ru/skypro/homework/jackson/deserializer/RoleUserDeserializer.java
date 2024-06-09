package ru.skypro.homework.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.skypro.homework.dto.Role;

import java.io.IOException;

/**
 * Десериализатор для {@link Role} чтобы при неверном значении получать null, а не error, для последующей обработки валидатором
 */
public class RoleUserDeserializer extends StdDeserializer<Role> {

    public RoleUserDeserializer() {
        this(null);
    }

    public RoleUserDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Role deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Role role;
        try {
            role = Role.valueOf(jsonParser.getValueAsString());
        } catch (IllegalArgumentException e) {
            return null;
        }
        return role;
    }

}