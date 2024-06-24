package com.example.application.system;

import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class I18NProviderImpl implements I18NProvider {

    private final MessageSource messageSource;

    public I18NProviderImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    @Override
    public List<Locale> getProvidedLocales() {
        return Arrays.asList(Locale.ENGLISH, new Locale("es"));
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        return messageSource.getMessage(key, params, locale);
    }

}
