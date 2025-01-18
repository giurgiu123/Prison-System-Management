# sa vad shift-ul fiecarui gardian
# select: name, everything about the shift
select utilizator.username, gardian.fk_id_utilizator,  shift.id_shift, shift.fk_id_etaj, shift.tip_shift from gardian join shift on shift.id_shift = gardian.fk_id_shift 
										join utilizator on gardian.fk_id_utilizator = utilizator.id_utilizator


# toti detinutii de la un anumit gardian
							