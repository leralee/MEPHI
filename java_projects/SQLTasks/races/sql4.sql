SELECT
    ca.name,
    ca.class,
    AVG(r.position) AS avg_position,
    COUNT(r.race) AS races_participated,
    cl.country
FROM Cars ca
         JOIN Results r ON ca.name = r.car
         JOIN Classes cl ON ca.class = cl.class
WHERE ca.class IN (
    SELECT c.class
    FROM Cars c
             JOIN Results r2 ON c.name = r2.car
    GROUP BY c.class
    HAVING COUNT(DISTINCT c.name) > 1
)
GROUP BY ca.name, ca.class, cl.country
HAVING AVG(r.position) < (
    SELECT AVG(r3.position)
    FROM Cars c3
             JOIN Results r3 ON c3.name = r3.car
    WHERE c3.class = ca.class
)
ORDER BY ca.class, avg_position;
