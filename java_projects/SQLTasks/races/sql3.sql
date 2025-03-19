SELECT
    ca.name,
    ca.class,
    AVG(r.position) AS avg_position,
    COUNT(r.race) AS races_count,
    cl.country,
    (SELECT COUNT(DISTINCT r2.race)
     FROM Cars c2
              JOIN Results r2 ON c2.name = r2.car
     WHERE c2.class = ca.class) AS total_races
FROM Cars ca
         JOIN Results r ON ca.name = r.car
         JOIN Classes cl ON ca.class = cl.class
GROUP BY ca.name, ca.class, cl.country
HAVING avg_position = (
    SELECT MIN(avg_pos)
    FROM (
             SELECT c.class, AVG(r.position) AS avg_pos
             FROM Cars c
                      JOIN Results r ON c.name = r.car
             GROUP BY c.class
         ) AS subquery
)
ORDER BY avg_position;
