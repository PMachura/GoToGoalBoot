package gotogoal.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import gotogoal.rest.jsonSerializer.nutrition.NutritionDayMixIn;
import java.sql.Date;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.UrlPathHelper;
import gotogoal.model.nutrition.NutritionDay;
import gotogoal.model.nutrition.Meal;
import gotogoal.model.user.User;
import gotogoal.rest.jsonSerializer.nutrition.FoodProductResourceMixIn;
import gotogoal.rest.jsonSerializer.nutrition.NutritionDayMixIn;
import gotogoal.rest.jsonSerializer.nutrition.NutritionDayResourceMixIn;
import gotogoal.rest.jsonSerializer.nutrition.MealMixIn;
import gotogoal.rest.jsonSerializer.nutrition.MealResourceMixIn;
import gotogoal.rest.jsonSerializer.user.UserMixIn;
import gotogoal.rest.jsonSerializer.user.UserResourceMixIn;
import gotogoal.rest.resource.nutrition.FoodProductResource;
import gotogoal.rest.resource.nutrition.NutritionDayResource;
import gotogoal.rest.resource.nutrition.MealResource;
import gotogoal.rest.resource.user.UserResource;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Konfiguracja fabryki (buildera) klasy ObjectMapper Ustawiona serializacja
     * typu DateTime jako ciągu znaków, a nie tablicy wartości
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToDisable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        builder.mixIn(NutritionDay.class, NutritionDayMixIn.class);
        builder.mixIn(Meal.class, MealMixIn.class);
        builder.mixIn(NutritionDayResource.class, NutritionDayResourceMixIn.class);
        builder.mixIn(FoodProductResource.class, FoodProductResourceMixIn.class);
        builder.mixIn(MealResource.class, MealResourceMixIn.class);
        builder.mixIn(User.class, UserMixIn.class);
        builder.mixIn(UserResource.class, UserResourceMixIn.class);
        return builder;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
        configurer.setUseRegisteredSuffixPatternMatch(Boolean.TRUE);
    }

    @Bean
    public AttributeConverter<LocalDate, Date> getConverter() {
        return new LocalDateAttributeConverter();
    }

    /**
     * Zmienia formatowanie czasu dla aplikacji
     * Np. jeżeli parametrem żądania jest obiekt LocalDate musi być podany w formacie zgodny z tym zdefiniowanym w metodzie
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDate.class, new LocalDateFormatter());
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Bean do obsługi wyjątków, które nie są bezpośrednio zgłaszane przez
     * kontroler
     *
     * @return
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> container.addErrorPages(new ErrorPage(MultipartException.class, "/user/uploadError"),
                new ErrorPage(NestedServletException.class, "/user/uploadError"),
                new ErrorPage(IllegalStateException.class, "/user/uploadError"));
    }

}
