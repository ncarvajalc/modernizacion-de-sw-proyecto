--Ejemplos puntuales sacar índices
--Para región
--Número de vacunados por región determinada en rango especifico
SELECT COUNT(distinct c.identificacionciudadano)
FROM Vacuna v, Ciudadano c, Oficinaregionaleps o, Cita cit
WHERE v.idciudadano = c.identificacionciudadano AND
      v.tipoidciudadano = c.tipodeidentificacion AND
      c.regionoficina = o.regioneps AND
      c.nombreoficina = o.nombreeps AND
      c.tipodeidentificacion = cit.tipoidciudadano AND
      c.identificacionciudadano = cit.idciudadano AND
      v.hasidoaplicado = '1' AND
      o.regioneps = 10 AND
      cit.fechacita BETWEEN TO_DATE('2020/12/31', 'YY/MM/DD') AND TO_DATE('2021/06/25', 'YY/MM/DD');

--Habilitados por region
SELECT COUNT(distinct c.identificacionciudadano)
FROM Ciudadano c, Oficinaregionaleps o, Cita cit
WHERE c.regionoficina = o.regioneps AND
      c.nombreoficina = o.nombreeps AND
      c.tipodeidentificacion = cit.tipoidciudadano AND
      c.identificacionciudadano = cit.idciudadano AND
      o.regioneps = 10 AND
      cit.fechacita BETWEEN TO_DATE('2020/12/31', 'YY/MM/DD') AND TO_DATE('2021/05/20', 'YY/MM/DD');

--Para estado
--Número de vacunados por estado determinado en rango especifico
SELECT COUNT(distinct c.identificacionciudadano)
FROM Vacuna v, Ciudadano c, Cita cit
WHERE v.idciudadano = c.identificacionciudadano AND
      v.tipoidciudadano = c.tipodeidentificacion AND
      c.tipodeidentificacion = cit.tipoidciudadano AND
      c.identificacionciudadano = cit.idciudadano AND
      v.hasidoaplicado = '1' AND
      c.estadovacunacion = 3 AND
      cit.fechacita BETWEEN TO_DATE('2020/12/31', 'YY/MM/DD') AND TO_DATE('2022/06/25', 'YY/MM/DD');

--Habilitados por estado
SELECT COUNT(distinct c.identificacionciudadano)
FROM Ciudadano c, Cita cit
WHERE c.tipodeidentificacion = cit.tipoidciudadano AND
      c.identificacionciudadano = cit.idciudadano AND
      c.estadovacunacion = 3 AND
      cit.fechacita BETWEEN TO_DATE('2020/12/31', 'YY/MM/DD') AND TO_DATE('2022/06/25', 'YY/MM/DD');


--Para etapa
--Número de vacunados por estado determinado en rango especifico
SELECT COUNT(distinct c.identificacionciudadano)
FROM Vacuna v, Ciudadano c, Cita cit
WHERE v.idciudadano = c.identificacionciudadano AND
      v.tipoidciudadano = c.tipodeidentificacion AND
      c.tipodeidentificacion = cit.tipoidciudadano AND
      c.identificacionciudadano = cit.idciudadano AND
      v.hasidoaplicado = '1' AND
      c.etapa = 2 AND
      cit.fechacita BETWEEN TO_DATE('2020/12/31', 'YY/MM/DD') AND TO_DATE('2022/06/25', 'YY/MM/DD');

--Habilitados por estado
SELECT COUNT(distinct c.identificacionciudadano)
FROM Ciudadano c, Cita cit
WHERE c.tipodeidentificacion = cit.tipoidciudadano AND
      c.identificacionciudadano = cit.idciudadano AND
      c.etapa = 2 AND
      cit.fechacita BETWEEN TO_DATE('2020/12/31', 'YY/MM/DD') AND TO_DATE('2022/06/25', 'YY/MM/DD');

--Por EPS
SELECT COUNT(distinct c.identificacionciudadano)
FROM Vacuna v, Ciudadano c, Oficinaregionaleps o, Cita cit
WHERE v.idciudadano = c.identificacionciudadano AND
      v.tipoidciudadano = c.tipodeidentificacion AND
      c.regionoficina = o.regioneps AND
      c.nombreoficina = o.nombreeps AND
      c.tipodeidentificacion = cit.tipoidciudadano AND
      c.identificacionciudadano = cit.idciudadano AND
      v.hasidoaplicado = '1' AND
      o.nombreeps = 'Sanitas' AND
      cit.fechacita BETWEEN TO_DATE('2020/12/31', 'YY/MM/DD') AND TO_DATE('2022/01/01', 'YY/MM/DD');

SELECT COUNT(distinct c.identificacionciudadano)
FROM Ciudadano c, Oficinaregionaleps o, Cita cit
WHERE c.regionoficina = o.regioneps AND
      c.nombreoficina = o.nombreeps AND
      c.tipodeidentificacion = cit.tipoidciudadano AND
      c.identificacionciudadano = cit.idciudadano AND
      o.nombreeps = 'Sanitas' AND
      cit.fechacita BETWEEN TO_DATE('2020/12/31', 'YY/MM/DD') AND TO_DATE('2022/01/01', 'YY/MM/DD');
select * from oficinaregionaleps;
