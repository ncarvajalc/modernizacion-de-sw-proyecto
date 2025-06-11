SELECT * 
FROM (SELECT  p.direccionpuntovacunacion, p.nombrepuntovacunacion, p.capacidadsimultanea, p.capacidaddiaria, p.disponibilidaddedosis, p.nombreoficina, p.regionoficina
	             FROM PUNTOVACUNACION p, CITA ct, CIUDADANO c, VACUNA v
	             WHERE ct.tipoidciudadano = c.tipodeidentificacion AND
	             ct.idciudadano = c.identificacionciudadano AND 
	             c.puntovacunacion = p.direccionpuntovacunacion AND 
	             v.tipoidciudadano = c.tipodeidentificacion AND
	             v.idciudadano = c.identificacionciudadano AND
	             v.hasidoaplicado = 0 AND
	             ct.fechacita BETWEEN ? AND ?
	             GROUP BY p.direccionpuntovacunacion, p.nombrepuntovacunacion, p.capacidadsimultanea, p.capacidaddiaria, p.disponibilidaddedosis, p.nombreoficina, p.regionoficina
                 ORDER BY COUNT(ct.identificadorcita) DESC)
WHERE ROWNUM <= 20