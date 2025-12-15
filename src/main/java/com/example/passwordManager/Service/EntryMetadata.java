package com.example.passwordManager.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.example.passwordManager.Model.Entry;

public class EntryMetadata {
    private final Map<String, BiConsumer<Entry, String>> fieldSetters = new LinkedHashMap<>();
    private final Map<String, Function<Entry, String>> fieldGetters = new LinkedHashMap<>();

    public EntryMetadata() {
        fieldSetters.put("Service Name", Entry::setServiceName);
        fieldSetters.put("Username", Entry::setUsername);
        fieldSetters.put("Password", Entry::setPassword);
        fieldSetters.put("Notes", Entry::setNotes);
        fieldSetters.put("URL", Entry::setUrl);

        fieldGetters.put("Service Name", Entry::getServiceName);
        fieldGetters.put("Username", Entry::getUsername);
        fieldGetters.put("Password", Entry::getPassword);
        fieldGetters.put("Notes", Entry::getNotes);
        fieldGetters.put("URL", Entry::getUrl);
    }

    public String getFieldValue(Entry entry, String fieldName){
        Function<Entry, String> getter = fieldGetters.get(fieldName);
        return getter != null ? getter.apply(entry) : "";
    }

    public Set<String> getFieldNames(){
        return fieldGetters.keySet();
    }

    public boolean updateFieldValue(Entry entry, String fieldName, String value){
        BiConsumer<Entry, String> setter = fieldSetters.get(fieldName);
        if (setter == null) {
            return false;
        } else {
            setter.accept(entry, value);
            return true;
        }
    }
}
