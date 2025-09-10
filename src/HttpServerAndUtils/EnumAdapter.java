package HttpServerAndUtils;

import com.google.gson.*;

import java.lang.reflect.Type;

public class EnumAdapter<T extends Enum<T>> implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name());
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            @SuppressWarnings("unchecked")
            T result = (T) Enum.valueOf((Class<T>) typeOfT, json.getAsString());
            return result;
        } catch (IllegalArgumentException e) {
            throw new JsonParseException(e);
        }
    }
}