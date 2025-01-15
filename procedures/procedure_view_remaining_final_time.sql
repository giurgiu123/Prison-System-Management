CREATE DEFINER=`root`@`localhost` PROCEDURE `view_remaining_final_time`(IN start_time DATETIME, IN end_time DATETIME, OUT remaining_time INTEGER,
 OUT total_time INTEGER)
BEGIN
		call years_between_dates(start_time, end_time, total_time) ; 
        call years_between_dates(NOW(), end_time, remaining_time);
END