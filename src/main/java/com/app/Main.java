package com.app;

import com.app.analyzer.LogAnalyzer;
import com.app.config.ConfigReader;
import com.app.model.LogEntry;
import com.app.parser.NginxLogParser;
import com.app.report.ReportGenerator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java -jar <jarname>.jar <path/to/app.properties>");
            return;
        }

        try {
            // 1. Чтение конфигурации
            ConfigReader config = new ConfigReader(args[0]);
            String logFilePath = config.getString("log.file.path");
            int topN = config.getInt("report.top.ip.count");
            String filterAgent = config.getString("filter.user.agent");

            // 2. Чтение логов и парсинг
            List<LogEntry> logs = Files.lines(Paths.get(logFilePath))
                    .map(NginxLogParser::parse)
                    .collect(Collectors.toList());

            // 3. Анализ логов
            LogAnalyzer analyzer = new LogAnalyzer(logs);

            Map<String, Long> topIps = analyzer.findTopNIps(topN);
            Map<Integer, Long> statusCodes = analyzer.countStatusCodes();
            long agentHits = analyzer.filterByUserAgent(filterAgent);

            // 4. Генерация отчета
            String report = ReportGenerator.generateJsonReport(topIps, statusCodes, agentHits, filterAgent);

            // 5. Вывод
            if ("STDOUT".equalsIgnoreCase(config.getString("output.target"))) {
                System.out.println(report);
            }

        } catch (IOException e) {
            System.err.println("Error reading file or configuration: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred during analysis: " + e.getMessage());
        }
    }
}