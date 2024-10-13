#!/bin/bash
# Используем df для мониторинга дискового пространства, сортируем и форматируем вывод с помощью awk, затем записываем в файл
(df -h | head -n 1; df -h | tail -n +2 | sort -k 5 -nr | awk '{print $5, $6}') > "disk_space_usage.txt"
