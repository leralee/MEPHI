WITH CarAvg AS (
    SELECT
        car,
        AVG(position) AS avg_pos,
        COUNT(race) AS races_participated
    FROM Results
    GROUP BY car
    HAVING AVG(position) > 3.0
),
     ClassCounts AS (
         SELECT
             c.class,
             COUNT(*) AS num_low_avg_cars
         FROM CarAvg
                  JOIN Cars c ON CarAvg.car = c.name
         GROUP BY c.class
     ),
     MaxCount AS (
         SELECT MAX(num_low_avg_cars) AS max_count
         FROM ClassCounts
     ),
     TopClasses AS (
         SELECT class
         FROM ClassCounts
         WHERE num_low_avg_cars = (SELECT max_count FROM MaxCount)
     )
SELECT
    c.name AS "имя",
    c.class AS "класс",
    ca.avg_pos AS "средняя позиция",
    ca.races_participated AS "количество гонок",
    cl.country AS "страна производства",
    (SELECT COUNT(DISTINCT r.race)
     FROM Results r
              JOIN Cars car ON r.car = car.name
     WHERE car.class = c.class) AS "всего гонок для класса"
FROM Cars c
         JOIN CarAvg ca ON c.name = ca.car
         JOIN Classes cl ON c.class = cl.class
WHERE c.class IN (SELECT class FROM TopClasses)
ORDER BY
    (SELECT num_low_avg_cars FROM ClassCounts WHERE ClassCounts.class = c.class) DESC, "количество гонок";
