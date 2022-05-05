package co.com.dk.juanvaldez.jvsignupmc.utils;

import net.logstash.logback.encoder.org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Sort;

public final  class PageableBuildUriUtils {

    /*private PageableBuildUriUtils() {
    }

    public static String buildUri(String filter, Long companyId, Integer page,
        Integer size, Sort sort, String filterName, String companyIdName) {

        StringBuilder result = new StringBuilder();
        final String SORTING_SEPARATOR = ": ";

        if (companyId != null && companyId>0) {
            result.append("&".concat(companyIdName).concat("=")).append(companyId);
        }
        if (filter != null) {
            result.append("&".concat(filterName).concat("=")).append(filter);
        }

        if (page != null) {
            result.append("&page=").append(page);
        }

        if (size != null) {
            result.append("&size=").append(size);
        }

        result.append("&sort=");
        String[] sortParams = sort.toString().split(SORTING_SEPARATOR);

        for (int i = 0; i < sortParams.length; i++) {

            if (i % NumberUtils.INTEGER_TWO != 0) {
                result.append(",");
            }

            result.append(sortParams[i]);
        }

        return result.toString();
    }*/

}
