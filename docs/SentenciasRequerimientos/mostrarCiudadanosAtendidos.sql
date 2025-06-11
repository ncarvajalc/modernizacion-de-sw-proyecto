SELECT c.tipodeidentificacion, c.identificacionciudadano, c.nombreCiudadano, c.apellidociudadano, c.esvacunable, c.fechadenacimiento, c.telefonodecontacto, c.estadovacunacion, c.etapa, c.puntovacunacion, c.nombreoficina, c.regionoficina 
				FROM Ciudadano c, Cita ct, Puntovacunacion p 
				WHERE ct.tipoidciudadano = c.tipodeidentificacion AND 
				      ct.idciudadano = c.identificacionciudadano AND 
				     p.direccionpuntovacunacion = c.puntovacunacion AND 
				      p.direccionpuntovacunacion = ? AND
				      ct.fechacita BETWEEN ? AND ?