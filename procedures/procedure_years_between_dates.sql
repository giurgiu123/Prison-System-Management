CREATE DEFINER=`root`@`localhost` PROCEDURE `years_between_dates`(IN sta DATETIME, IN fin DATETIME, OUT yea INTEGER)
BEGIN
	SELECT TIMESTAMPDIFF(year , sta, fin) into yea;
END