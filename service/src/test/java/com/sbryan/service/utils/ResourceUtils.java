package com.sbryan.service.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

public class ResourceUtils {

    @SneakyThrows
    public static String getStringFromResource(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    public static String getResourceAsString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
