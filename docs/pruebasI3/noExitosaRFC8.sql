--En caso de querer limpiar tablas
DELETE FROM cita;
DELETE FROM talentohumano;
DELETE FROM pertenecea;
DELETE FROM condicion;
DELETE FROM trabajaen;
DELETE FROM tienecondicionpreservacion;
DELETE FROM condicionpreservacion;
DELETE FROM tieneinfraestructura;
DELETE FROM infraestructura;
DELETE FROM puntovacunacionvirtual;
DELETE FROM vacuna;
DELETE FROM ciudadano;
DELETE FROM puntovacunacion;
DELETE FROM lotevacuna;
DELETE FROM oficinaregionaleps;
DELETE FROM estadovacunacion;
DELETE FROM etapa;

--Datos oficinaRegional
--Punto inicial
INSERT INTO "OFICINAREGIONALEPS" (NOMBREEPS, REGIONEPS)
VALUES ('Compensar', 1);

--Datos PuntoVacunacion
--Punto inicial
INSERT INTO "PUNTOVACUNACION" (DIRECCIONPUNTOVACUNACION, NOMBREPUNTOVACUNACION,CAPACIDADSIMULTANEA,CAPACIDADDIARIA,DISPONIBILIDADDEDOSIS, NOMBREOFICINA,REGIONOFICINA, SoloMayores, SoloSalud, EstaHabilitado)
VALUES ('Cll 1','Centro atención Compensar 1',6,2,2,'Compensar',1, 0, 0, 1);

--Estado vacunacion
INSERT INTO "ESTADOVACUNACION" (IDENTIFICADORESTADO, DESCRIPCIONESTADO )
VALUES (1,'No vacunado');

--Etapa
INSERT INTO "ETAPA" (NUMERODEETAPA, DESCRIPCIONETAPA )
VALUES (5,'Etapa 5');

INSERT INTO "ETAPA" (NUMERODEETAPA, DESCRIPCIONETAPA )
VALUES (3,'Etapa 3');

--Condicion
INSERT INTO "CONDICION" (IDENTIFICADORCONDICION, DESCRIPCIONCONDICION)
VALUES(3, 'Paciente con cancer');

--CIUDADANO
--Asignacion a puntos
--Datos Ciudadanos
INSERT INTO "CIUDADANO" (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, NOMBRECIUDADANO, APELLIDOCIUDADANO, ESVACUNABLE, FECHADENACIMIENTO, TELEFONODECONTACTO, ESTADOVACUNACION, ETAPA, PUNTOVACUNACION, NOMBREOFICINA, REGIONOFICINA, Genero, Rol, Ciudad, Localidad)
VALUES ('CC','1', 'Juan','Perez', '1' , TO_DATE('1998/11/1', 'YY/MM/DD'), 23875496 , 1, 5,'Cll 1', 'Compensar', 1 , 'Masculino', 'Ciudadano', 'Bogotá', 'Suba');

INSERT INTO "PERTENECEA" (TIPOIDCIUDADANO, IDCIUDADANO, IDENTIFICADORCONDICION, DESCRIPCIONCONDICION)
VALUES('CC','1', 3, 'Paciente con cancer');

INSERT INTO "CIUDADANO" (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, NOMBRECIUDADANO, APELLIDOCIUDADANO, ESVACUNABLE, FECHADENACIMIENTO, TELEFONODECONTACTO, ESTADOVACUNACION, ETAPA, PUNTOVACUNACION, NOMBREOFICINA, REGIONOFICINA, Genero, Rol, Ciudad, Localidad)
VALUES ('CC','2', 'Rafael','Perez', '1' , TO_DATE('1998/11/1', 'YY/MM/DD'), 23875496 , 1, 5,'Cll 1', 'Compensar', 1 , 'Masculino', 'Ciudadano', 'Bogotá', 'Suba');

INSERT INTO "CIUDADANO" (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, NOMBRECIUDADANO, APELLIDOCIUDADANO, ESVACUNABLE, FECHADENACIMIENTO, TELEFONODECONTACTO, ESTADOVACUNACION, ETAPA, PUNTOVACUNACION, NOMBREOFICINA, REGIONOFICINA, Genero, Rol, Ciudad, Localidad)
VALUES ('CC','3', 'Sol','Perez', '1' , TO_DATE('1998/11/1', 'YY/MM/DD'), 23875496 , 1, 5,'Cll 1', 'Compensar', 1 , 'Femenino', 'Ciudadano', 'Bogotá', 'Suba');

INSERT INTO "CIUDADANO" (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, NOMBRECIUDADANO, APELLIDOCIUDADANO, ESVACUNABLE, FECHADENACIMIENTO, TELEFONODECONTACTO, ESTADOVACUNACION, ETAPA, PUNTOVACUNACION, NOMBREOFICINA, REGIONOFICINA, Genero, Rol, Ciudad, Localidad)
VALUES ('CC','4', 'Irene','Perez', '1' , TO_DATE('1960/11/1', 'YY/MM/DD'), 23875496 , 1, 5,'Cll 1', 'Compensar', 1 , 'Femenino', 'Ciudadano', 'Medellín', 'Med');

INSERT INTO "CIUDADANO" (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, NOMBRECIUDADANO, APELLIDOCIUDADANO, ESVACUNABLE, FECHADENACIMIENTO, TELEFONODECONTACTO, ESTADOVACUNACION, ETAPA, PUNTOVACUNACION, NOMBREOFICINA, REGIONOFICINA, Genero, Rol, Ciudad, Localidad)
VALUES ('CC','5', 'María','Perez', '1' , TO_DATE('1960/11/1', 'YY/MM/DD'), 23875496 , 1, 5,'Cll 1', 'Compensar', 1 , 'Femenino', 'Ciudadano', 'Medellín', 'Med');

INSERT INTO "CIUDADANO" (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, NOMBRECIUDADANO, APELLIDOCIUDADANO, ESVACUNABLE, FECHADENACIMIENTO, TELEFONODECONTACTO, ESTADOVACUNACION, ETAPA, PUNTOVACUNACION, NOMBREOFICINA, REGIONOFICINA, Genero, Rol, Ciudad, Localidad)
VALUES ('CC','6', 'Olga','Perez', '1' , TO_DATE('1960/11/1', 'YY/MM/DD'), 23875496 , 1, 5,'Cll 1', 'Compensar', 1 , 'Otro', 'Ciudadano', 'Medellín', 'Med');

--Datos Lote Vacunas
INSERT INTO "LOTEVACUNA" (IDENTIFICADORLOTE, NOMBREOFICINA,REGIONOFICINA)
VALUES (1,'Compensar', 1);

--Vacunas
INSERT INTO "VACUNA" (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO, Tecnologia )
VALUES (1,'1',1,'Cll 1','CC','1', 'ARN');
INSERT INTO "VACUNA" (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO, Tecnologia )
VALUES (2,'1',1,'Cll 1','CC','1', 'ARN');

INSERT INTO "VACUNA" (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO , Tecnologia)
VALUES (3,'1',1,'Cll 1','CC','2', 'ARN');
INSERT INTO "VACUNA" (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO , Tecnologia)
VALUES (4,'1',1,'Cll 1','CC','2', 'ARN');

INSERT INTO "VACUNA" (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO , Tecnologia)
VALUES (5,'1',1,'Cll 1','CC','3', 'ARN');
INSERT INTO "VACUNA" (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO , Tecnologia)
VALUES (6,'1',1,'Cll 1','CC','3', 'ARN');

INSERT INTO "VACUNA" (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO , Tecnologia)
VALUES (7,'1',1,'Cll 1','CC','4', 'ADN');
INSERT INTO "VACUNA" (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO , Tecnologia)
VALUES (8,'1',1,'Cll 1','CC','5', 'ADN');
INSERT INTO "VACUNA" (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO , Tecnologia)
VALUES (9,'1',1,'Cll 1','CC','6', 'ADN');

COMMIT;

--JOIN básico
SELECT distinct c.*
FROM ciudadano c LEFT JOIN pertenecea pe ON
     (c.tipodeidentificacion = pe.tipoidciudadano AND
     c.identificacionciudadano = pe.idciudadano),
     oficinaregionaleps o, puntovacunacion pu, vacuna v
WHERE c.nombreoficina = o.nombreeps AND
      c.regionoficina = o.regioneps AND
      c.puntovacunacion = pu.direccionpuntovacunacion AND
      c.tipodeidentificacion = v.tipoidciudadano AND
      c.identificacionciudadano = v.idciudadano;

--CONDICION FECHA
--Especifica
(SELECT TRUNC(TO_NUMBER(SYSDATE - c.fechadenacimiento) / 365.25) FROM DUAL) = 22

--RANGO
(SELECT TRUNC(TO_NUMBER(SYSDATE - c.fechadenacimiento) / 365.25) FROM DUAL) BETWEEN 22 AND 60

--CONDICION Genero
c.genero = 'Masculino'

-- Condicion Condicion
pe.descripcioncondicion = 'Paciente con cancer'

--Condicion ciudad
c.ciudad = 'Bogotá'

--Condicion Localidad
c.localidad = 'Suba'

--Condicion EPS
o.nombreeps = 'Compensar' AND
o.regioneps = 1
--Condicion puntovacunacion
pu.direccionpuntovacunacion = 'Cll 1'

--Condicion dosis aplicadas
(c.tipodeidentificacion, c.identificacionciudadano) IN
      (SELECT c.tipodeidentificacion, c.identificacionciudadano
        FROM ciudadano c, vacuna v
        WHERE c.identificacionciudadano = v.idciudadano
        GROUP BY c.tipodeidentificacion, c.identificacionciudadano
        HAVING COUNT(*) = 2)

--Condicion tecnologia
v.tecnologia = 'ARN'

--Join de todas las condiciones (solo debería mandar a Juan)
SELECT distinct c.*
FROM ciudadano c LEFT JOIN pertenecea pe ON
     (c.tipodeidentificacion = pe.tipoidciudadano AND
     c.identificacionciudadano = pe.idciudadano),
     oficinaregionaleps o, puntovacunacion pu, vacuna v
WHERE c.nombreoficina = o.nombreeps AND
      c.regionoficina = o.regioneps AND
      c.puntovacunacion = pu.direccionpuntovacunacion AND
      c.tipodeidentificacion = v.tipoidciudadano AND
      c.identificacionciudadano = v.idciudadano AND
      (SELECT TRUNC(TO_NUMBER(SYSDATE - c.fechadenacimiento) / 365.25) FROM DUAL) = 23 AND
      c.genero = 'Masculino' AND
      pe.descripcioncondicion = 'Paciente con cancer' AND
      c.ciudad = 'Bogotá' AND
      c.localidad = 'Suba' AND
      o.nombreeps = 'Compensar' AND
      o.regioneps = 1 AND
      pu.direccionpuntovacunacion = 'Cll 1' AND
      (c.tipodeidentificacion, c.identificacionciudadano) IN
            (SELECT c.tipodeidentificacion, c.identificacionciudadano
              FROM ciudadano c, vacuna v
              WHERE c.identificacionciudadano = v.idciudadano
              GROUP BY c.tipodeidentificacion, c.identificacionciudadano
              HAVING COUNT(*) = 2) AND
      v.tecnologia = 'ARN';
