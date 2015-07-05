package org.pasut.android.remotecontrol.services;

/**
 * Created by marcelo on 24/03/14.
 */
public interface PreferencesService {
    <T> T get(String key, Class<T> clazz);
    <T> void put(String key, T value);
    boolean contain(String key);
    void delete(String key);
}
