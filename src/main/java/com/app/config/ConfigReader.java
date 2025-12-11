package com.app.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для чтения конфигурационных параметров из app.properties.
 */
public class ConfigReader {
    private final Properties properties = new Properties();

    /**
     * Конструктор, загружающий файл конфигурации.
     * @param path Путь к файлу app.properties.
     * @throws IOException Если файл не найден или ошибка чтения.
     */
    public ConfigReader(String path) throws IOException {
        try (InputStream input = new FileInputStream(path)) {
            properties.load(input);
        }
    }

    /**
     * Получает строковое значение параметра.
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * Получает целочисленное значение параметра.
     */
    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
}