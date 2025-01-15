CREATE DEFINER=`root`@`localhost` PROCEDURE `remaining_time_based_on_id_inmate`(IN idPrizonier int, out sentinta_ramasa nvarchar(155))
BEGIN
	set sentinta_ramasa = (select (
		if(sum(timestampdiff(year, sentinta.start_time, sentinta.end_time)) > 2, 
        Concat(sum(timestampdiff(year, sentinta.start_time, sentinta.end_time)), " years"),
        
		if(Sum(TIMESTAMPDIFF(month, sentinta.start_time, sentinta.end_time)) > 2,
		Concat(Sum(TIMESTAMPDIFF(month, sentinta.start_time, sentinta.end_time)), 
            ' months') ,
		if ( Sum(TIMESTAMPDIFF(day, sentinta.start_time, sentinta.end_time)) > 1, 
        Concat(Sum(TIMESTAMPDIFF(day, sentinta.start_time, sentinta.end_time)), ' days')
        , Concat(Sum(TIMESTAMPDIFF(hour, sentinta.start_time, sentinta.end_time)), ' hour')))
        ))
	from sentinta where sentinta.fk_id_detinut = idPrizonier);
														
END
