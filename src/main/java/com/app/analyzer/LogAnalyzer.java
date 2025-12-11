package com.app.analyzer;

import com.app.model.LogEntry;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс, содержащий основную логику анализа логов Nginx.
 */
public class LogAnalyzer {
    private final List<LogEntry> logs;

    /**
     * Конструктор LogAnalyzer.
     * @param logs Список разобранных записей логов.
     */
    public LogAnalyzer(List<LogEntry> logs) {
        this.logs = logs;
    }

    /**
     * Задание 1: Находит N IP-адресов, с которых было совершено больше всего запросов.
     * @param topN Количество IP-адресов для вывода в топ.
     * @return Map<String, Long>, где ключ - IP, значение - количество запросов.
     */
    public Map<String, Long> findTopNIps(int topN) {
        // Группируем по IP-адресу и считаем количество
        return logs.stream()
                .collect(Collectors.groupingBy(LogEntry::getIpAddress, Collectors.counting()))
                // Сортируем в порядке убывания
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                // Ограничиваем топом N
                .limit(topN)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, Collectors.LinkedHashMap::new));
    }

    /**
     * Задание 2: Подсчитывает общее количество запросов по кодам ответа.
     * @return Map<Integer, Long>, где ключ - код ответа (200, 404), значение - количество запросов.
     */
    public Map<Integer, Long> countStatusCodes() {
        return logs.stream()
                .collect(Collectors.groupingBy(LogEntry::getStatusCode, Collectors.counting()));
    }

    /**
     * Задание 3: Находит все запросы, пришедшие от определенного User-Agent.
     * @param userAgent Искомый User-Agent.
     * @return Количество запросов, соответствующих User-Agent.
     */
    public long filterByUserAgent(String userAgent) {
        return logs.stream()
                .filter(entry -> entry.getUserAgent().contains(userAgent))
                .count();
    }
}