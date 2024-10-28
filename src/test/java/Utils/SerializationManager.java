package Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SerializationManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // Método para serializar un objeto en un String JSON
    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar el objeto: " + e.getMessage(), e);
        }
    }
    // Método para deserializar un String JSON a un objeto de la clase especificada
    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error al deserializar el JSON: " + e.getMessage(), e);
        }
    }

    public static <T> List<T> deserializeList(String json, Class<T> clazz) {
        try {
            // Creando la TypeReference para una lista de objetos del tipo especificado
            TypeReference<List<T>> typeReference = new TypeReference<List<T>>() {};
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Error al deserializar la lista de objetos: " + e.getMessage(), e);
        }
    }

    public static String readJsonFileAsString(String filePath) {
        try {
            // Leer el archivo JSON y convertirlo en un Object genérico (Mapa o Lista dependiendo del JSON)
            Object jsonObject = objectMapper.readValue(new File(filePath), Object.class);
            // Convertir ese Object de nuevo a un String JSON
            return objectMapper.writeValueAsString(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    public static <T> List<T> deserializeList2(String json, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Error al deserializar la lista de objetos: " + e.getMessage(), e);
        }
    }

}
