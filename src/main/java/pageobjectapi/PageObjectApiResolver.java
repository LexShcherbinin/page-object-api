package pageobjectapi;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.HashMap;

public class PageObjectApiResolver implements ParameterResolver {
    private final Class<? extends PageObjectApi<?>> pageObjectClass;

    public PageObjectApiResolver(Class<? extends PageObjectApi<?>> pageObjectClass) {
        this.pageObjectClass = pageObjectClass;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(pageObjectClass);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new PageObjectApiFactory().createPageObject(
                new HashMap<>(),
                (Class<? extends PageObjectApi>) parameterContext.getParameter().getType()
        );
    }
}