WITH HotelCategories AS (
    SELECT
        h.ID_hotel,
        h.name AS hotel_name,
        CASE
            WHEN AVG(r.price) < 175 THEN 'Дешевый'
            WHEN AVG(r.price) BETWEEN 175 AND 300 THEN 'Средний'
            ELSE 'Дорогой'
            END AS hotel_category
    FROM Hotel h
             JOIN Room r ON h.ID_hotel = r.ID_hotel
    GROUP BY h.ID_hotel, h.name
),
     CustomerHotelPreferences AS (
         SELECT
             c.ID_customer,
             c.name,
             MAX(CASE
                     WHEN hc.hotel_category = 'Дорогой' THEN 3
                     WHEN hc.hotel_category = 'Средний' THEN 2
                     ELSE 1
                 END) AS priority,
             GROUP_CONCAT(DISTINCT hc.hotel_name SEPARATOR ', ') AS visited_hotels
         FROM Customer c
                  JOIN Booking b ON c.ID_customer = b.ID_customer
                  JOIN Room r ON b.ID_room = r.ID_room
                  JOIN HotelCategories hc ON r.ID_hotel = hc.ID_hotel
         GROUP BY c.ID_customer, c.name
     )
SELECT
    ID_customer,
    name,
    CASE
        WHEN priority = 3 THEN 'Дорогой'
        WHEN priority = 2 THEN 'Средний'
        ELSE 'Дешевый'
        END AS preferred_hotel_type,
    visited_hotels
FROM CustomerHotelPreferences
ORDER BY priority ASC;