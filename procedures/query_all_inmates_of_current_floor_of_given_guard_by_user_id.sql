SET @param := 2;
# all inmates of the fk_id_shift
# we give the warden
# login -> utilizator
# trebuie sa vad ce e utilizatorul
# trebuie sa vad ce shift are acum
select gardian.id as gardian_id, utilizator.username as gardian_name, shift.id_shift as tip_shift, detinut.id_detinut, detinut.nume, detinut.fk_id_utilizator, detinut.profesie, detinut.fk_id_celula from detinut, etaj 
						join celula on celula.fk_id_etaj = etaj.id_etaj 
						join shift on shift.fk_id_etaj = etaj.id_etaj
                        join gardian on gardian.fk_id_shift = shift.id_shift and gardian.fk_id_utilizator = @param
                        join utilizator on gardian.fk_id_utilizator = utilizator.id_utilizator
						where detinut.fk_id_celula = celula.id_celula
                        
                        