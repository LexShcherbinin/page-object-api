package pageobjectapi;

import io.restassured.http.ContentType;
import org.awaitility.Awaitility;
import org.awaitility.core.ThrowingRunnable;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PageObjectApi<T extends PageObjectApi<T>> {
    protected Map<String, Object> map;

    protected Map<String, String> getHeaders() {
        return Map.of(
            "encoding", "UTF-8",
            "Content-Type", ContentType.JSON.toString()
        );
    }

    public final <D extends PageObjectApi<D>> D createRequest(final Class<D> pageClass) {
        assertNotEquals(this.getClass(), pageClass, "Вы уже в этом пейдже");
        return new PageObjectApiFactory().createPageObject(map, pageClass);
    }

    protected void saveValue(final String key, final Object value) {
        assertTrue(checkKey(key), "Ключ " + key + " уже задействован");
        map.put(key, value);
    }

    protected Object getValue(final String key) {
        assertTrue(checkKey(key), "Отсутствует значение с ключём " + key);
        return map.get(key);
    }

    private boolean checkKey(final String key) {
        return map.containsKey(key);
    }

    protected void untilAsserted(ThrowingRunnable assertion) {
        Awaitility.await()
            .atMost(Duration.ofMinutes(1))
            .pollInSameThread()
            .pollInterval(Duration.ofSeconds(10))
            .untilAsserted(assertion);
    }
}