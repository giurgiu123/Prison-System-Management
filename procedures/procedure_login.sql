CREATE DEFINER=`root`@`localhost` PROCEDURE `new_procedure`(IN username nvarchar(255), IN password nvarchar(255))
BEGIN
	SELECT 
        id_utilizator, 
        drept_access 
    FROM 
        penitenciar.utilizator 
    WHERE 
        utilizator.username = username AND utilizator.password = password;
END
