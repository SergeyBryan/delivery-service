package com.sbryan.service.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//1. swagger flux библиотека -> поднимает swagger если спринг на реактивщине,
// >>>это причина почему он может не запуститься<<<
//2. если спринг >3.x.x, то нужна другая библиотека для работы с swagger
//классическая библиотека была переименована в новую для spring 3.x.x
//3. Это конфиг, который позволяет спарсить OpenAPI из yaml файла.
// Yaml файлы генерируют код-заглушку, поэтому здесь 2 в 1.
//4. Swagger parser: обрати внимание, что для разных версий используется разный пареср.
@Configuration
@Slf4j
public class ApiDoc {
    @Bean
    public OpenAPI apiInfo() {
        var result = new OpenAPIV3Parser().read("./api/aggregated-api.yaml");
        if (result == null) {
            return new OpenAPI();
        }
        return result;
    }
}

