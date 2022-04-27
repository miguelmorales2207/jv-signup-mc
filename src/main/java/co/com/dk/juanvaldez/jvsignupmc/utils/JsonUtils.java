package co.com.dk.juanvaldez.jvsignupmc.utils;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtils {

    private JsonUtils() {

    }

    public static String convertToJson(@NotNull final Object data) {
        if (data instanceof Object[]) {
            Object[] dataArray = (Object[]) data;
            List<String> jsonList = new ArrayList<>();

            for (Object dataToConvert : dataArray) {
                jsonList.add(convertToJsonInnerData(dataToConvert));
            }

            return String.format("[%1$s]", String.join(", ", jsonList));
        }

        return convertToJsonInnerData(data);
    }

    private static String convertToJsonInnerData(Object data) {
        try {
            return ObjectMapperUtils.getObjectMapper().writeValueAsString(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.trace(e.getMessage(), e);
        }

        return String.format("{%1$s}", data.toString());
    }

}
