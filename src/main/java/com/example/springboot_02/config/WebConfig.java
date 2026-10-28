package com.example.springboot_02.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration = a class that sets up the application; Spring finds it by scanning the package.
// WebMvcConfigurer = the hook for customising Spring MVC, like the options you pass while building
// the pipeline in Program.cs (AddControllers(...), app.UseXxx(...)).
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Opening http://localhost:8080/ sends the browser straight to Swagger UI, so Swagger is the
    // start page. In ASP.NET this would be: app.MapGet("/", () => Results.Redirect("/swagger"));
    // Do not also map "/" in a controller: a controller mapping wins over this redirect.
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui.html");
    }
}
