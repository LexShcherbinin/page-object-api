package pageobjectapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PageObjectApiFactory {
    private final Map<Class<? extends PageObjectApi<?>>, PageObjectApi<? extends PageObjectApi<?>>> pageObjectCache = new HashMap<>();

    public <T extends PageObjectApi<T>> T createPageObject(Map<String, Object> map, Class<T> pageObjectClass) {
        PageObjectApi<? extends PageObjectApi<?>> cachePageObject = pageObjectCache.get(pageObjectClass);

        if (cachePageObject != null) {
            return pageObjectClass.cast(cachePageObject);

        } else {
            try {
                Class<PageObjectApi<?>> superClass = (Class<PageObjectApi<?>>) pageObjectClass.getSuperclass();
                Constructor<T> constructor = pageObjectClass.getDeclaredConstructor(superClass);
                constructor.setAccessible(true);
                T instance = constructor.newInstance();

                Field field = superClass.getDeclaredField("map");
                field.setAccessible(true);
                field.set(instance, new HashMap<>(map));

                pageObjectCache.put(pageObjectClass, instance);
                return instance;

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
                e.printStackTrace();

                throw new PageNotInitializedException("Не удалось инициализировать " + pageObjectClass.getClass().getName());
            }
        }
    }

    static class PageNotInitializedException extends RuntimeException {
        PageNotInitializedException(String message) {
            super(message);
        }
    }
}