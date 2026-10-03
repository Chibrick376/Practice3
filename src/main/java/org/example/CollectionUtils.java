package org.example;

import java.util.List;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> void copy(
            List<? extends T> source,
            List<? super T> target) {
        for (T item : source) {
            target.add(item);
        }
    }
}