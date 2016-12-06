package com.yuikibis.mutescheduler.common;

import java.util.HashMap;
import java.util.Map;

public class ModelLocator {
    public enum Tag {
    }

    private static Map<Tag, Object> models = new HashMap<>();

    private ModelLocator() {
    }

    public static void register(Tag tag, Object object) {
        models.put(tag, object);
    }

    public static Object get(Tag tag) {
        return models.get(tag);
    }
}
