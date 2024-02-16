package io.nwdaf.eventsubscription.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nwdaf.eventsubscription.customModel.Identifiable;
import io.nwdaf.eventsubscription.repository.TableEntity;

import java.util.ArrayList;
import java.util.List;

public class ServiceUtils {
    public static <T extends TableEntity, S extends Identifiable> List<S> parseTables(List<T> tables, Class<S> type, ObjectMapper objectMapper) throws JsonProcessingException {
        List<S> res = new ArrayList<>();
        for (T table : tables) {
            S sub = objectMapper.readValue(objectMapper.writeValueAsString(table.getData()), type);
            sub.setId(table.getId());
            res.add(sub);
        }
        return res;
    }
}
