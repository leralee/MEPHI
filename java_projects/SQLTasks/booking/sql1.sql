SELECT
    c.name AS "имя",
    c.email AS "электронная почта",
    c.phone AS "телефон",
    COUNT(b.ID_booking) AS "общее количество бронирований",
    GROUP_CONCAT(DISTINCT h.name SEPARATOR ', ') AS "список отелей",
    ROUND(AVG(DATEDIFF(b.check_out_date, b.check_in_date)), 1) AS "средняя длительность пребывания"
FROM Customer c
         JOIN Booking b ON c.ID_customer = b.ID_customer
         JOIN Room r ON b.ID_room = r.ID_room
         JOIN Hotel h ON r.ID_hotel = h.ID_hotel
GROUP BY c.ID_customer, c.name, c.email, c.phone
HAVING
    COUNT(b.ID_booking) > 2 -- Более двух бронирований
   AND COUNT(DISTINCT h.ID_hotel) >= 2 -- В разных отелях (минимум два)
ORDER BY COUNT(b.ID_booking) DESC;
