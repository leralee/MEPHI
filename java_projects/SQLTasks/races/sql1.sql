SELECT class, car_name, avg_position, race_count
FROM (
         SELECT
             c.class,
             c.name AS car_name,
             AVG(r.position) AS avg_position,
             COUNT(r.race) AS race_count,
             RANK() OVER (PARTITION BY c.class ORDER BY AVG(r.position) ASC) AS rank
         FROM Results r
                  JOIN Cars c ON r.car = c.name
         GROUP BY c.class, c.name
     ) ranked
WHERE rank = 1
ORDER BY avg_position ASC;
