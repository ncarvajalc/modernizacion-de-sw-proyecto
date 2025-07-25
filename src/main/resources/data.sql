-- Data seeding for Vacuandes application
-- This file will be automatically executed by Spring Boot after schema.sql

-- Helper procedure to insert data only if it doesn't exist
CREATE OR REPLACE PROCEDURE insert_if_not_exists(table_name IN VARCHAR2, check_condition IN VARCHAR2, insert_sql IN CLOB)
IS
  record_count INTEGER;
  sql_query VARCHAR2(4000);
BEGIN
  sql_query := 'SELECT COUNT(*) FROM ' || table_name || ' WHERE ' || check_condition;
  EXECUTE IMMEDIATE sql_query INTO record_count;
  
  IF record_count = 0 THEN
    EXECUTE IMMEDIATE insert_sql;
  END IF;
END;
/

-- Insert OficinaRegionalEPS data
BEGIN
  insert_if_not_exists('OFICINAREGIONALEPS', 
    'NOMBREEPS = ''Compensar'' AND REGIONEPS = 12',
    'INSERT INTO OFICINAREGIONALEPS (NOMBREEPS, REGIONEPS) VALUES (''Compensar'', 12)');
END;
/

BEGIN
  insert_if_not_exists('OFICINAREGIONALEPS', 
    'NOMBREEPS = ''Sanitas'' AND REGIONEPS = 10',
    'INSERT INTO OFICINAREGIONALEPS (NOMBREEPS, REGIONEPS) VALUES (''Sanitas'', 10)');
END;
/

-- Insert EstadoVacunacion data
BEGIN
  insert_if_not_exists('ESTADOVACUNACION', 
    'IDENTIFICADORESTADO = 1',
    'INSERT INTO ESTADOVACUNACION (IDENTIFICADORESTADO, DESCRIPCIONESTADO) VALUES (1, ''No vacunado'')');
END;
/

BEGIN
  insert_if_not_exists('ESTADOVACUNACION', 
    'IDENTIFICADORESTADO = 2',
    'INSERT INTO ESTADOVACUNACION (IDENTIFICADORESTADO, DESCRIPCIONESTADO) VALUES (2, ''Primera dosis aplicada'')');
END;
/

BEGIN
  insert_if_not_exists('ESTADOVACUNACION', 
    'IDENTIFICADORESTADO = 3',
    'INSERT INTO ESTADOVACUNACION (IDENTIFICADORESTADO, DESCRIPCIONESTADO) VALUES (3, ''Segunda dosis aplicada'')');
END;
/

-- Insert Etapa data
BEGIN
  insert_if_not_exists('ETAPA', 
    'NUMERODEETAPA = 1',
    'INSERT INTO ETAPA (NUMERODEETAPA, DESCRIPCIONETAPA) VALUES (1, ''Fase 1- Etapa 1'')');
END;
/

BEGIN
  insert_if_not_exists('ETAPA', 
    'NUMERODEETAPA = 2',
    'INSERT INTO ETAPA (NUMERODEETAPA, DESCRIPCIONETAPA) VALUES (2, ''Fase 1- Etapa 2'')');
END;
/

BEGIN
  insert_if_not_exists('ETAPA', 
    'NUMERODEETAPA = 3',
    'INSERT INTO ETAPA (NUMERODEETAPA, DESCRIPCIONETAPA) VALUES (3, ''Fase 1- Etapa 3'')');
END;
/

BEGIN
  insert_if_not_exists('ETAPA', 
    'NUMERODEETAPA = 4',
    'INSERT INTO ETAPA (NUMERODEETAPA, DESCRIPCIONETAPA) VALUES (4, ''Fase 2- Etapa 4'')');
END;
/

BEGIN
  insert_if_not_exists('ETAPA', 
    'NUMERODEETAPA = 5',
    'INSERT INTO ETAPA (NUMERODEETAPA, DESCRIPCIONETAPA) VALUES (5, ''Fase 2- Etapa 5'')');
END;
/

-- Insert PuntoVacunacion data
BEGIN
  insert_if_not_exists('PUNTOVACUNACION', 
    'DIRECCIONPUNTOVACUNACION = ''CR 7A #122-09''',
    'INSERT INTO PUNTOVACUNACION (DIRECCIONPUNTOVACUNACION, NOMBREPUNTOVACUNACION, CAPACIDADSIMULTANEA, CAPACIDADDIARIA, DISPONIBILIDADDEDOSIS, NOMBREOFICINA, REGIONOFICINA, SOLOMAYORES, SOLOSALUD, ESTAHABILITADO) VALUES (''CR 7A #122-09'', ''Centro atención Compensar'', 200, 100, 10000, ''Compensar'', 12, ''0'', ''0'', ''1'')');
END;
/

BEGIN
  insert_if_not_exists('PUNTOVACUNACION', 
    'DIRECCIONPUNTOVACUNACION = ''CR 8H #123-02''',
    'INSERT INTO PUNTOVACUNACION (DIRECCIONPUNTOVACUNACION, NOMBREPUNTOVACUNACION, CAPACIDADSIMULTANEA, CAPACIDADDIARIA, DISPONIBILIDADDEDOSIS, NOMBREOFICINA, REGIONOFICINA, SOLOMAYORES, SOLOSALUD, ESTAHABILITADO) VALUES (''CR 8H #123-02'', ''Centro vacunación Sanitas'', 300, 200, 10000, ''Sanitas'', 10, ''0'', ''0'', ''1'')');
END;
/

-- Insert LoteVacuna data
BEGIN
  insert_if_not_exists('LOTEVACUNA', 
    'IDENTIFICADORLOTE = 1',
    'INSERT INTO LOTEVACUNA (IDENTIFICADORLOTE, NOMBREOFICINA, REGIONOFICINA) VALUES (1, ''Compensar'', 12)');
END;
/

BEGIN
  insert_if_not_exists('LOTEVACUNA', 
    'IDENTIFICADORLOTE = 2',
    'INSERT INTO LOTEVACUNA (IDENTIFICADORLOTE, NOMBREOFICINA, REGIONOFICINA) VALUES (2, ''Sanitas'', 10)');
END;
/

-- Insert Infraestructura data
BEGIN
  insert_if_not_exists('INFRAESTRUCTURA', 
    'IDENTIFICADORINFRAESTRUCTURA = 3',
    'INSERT INTO INFRAESTRUCTURA (IDENTIFICADORINFRAESTRUCTURA, DESCRIPCIONINFRAESTRUCTURA) VALUES (3, ''Refrigeración'')');
END;
/

BEGIN
  insert_if_not_exists('INFRAESTRUCTURA', 
    'IDENTIFICADORINFRAESTRUCTURA = 4',
    'INSERT INTO INFRAESTRUCTURA (IDENTIFICADORINFRAESTRUCTURA, DESCRIPCIONINFRAESTRUCTURA) VALUES (4, ''Almacenamiento al clima'')');
END;
/

-- Insert CondicionPreservacion data
BEGIN
  insert_if_not_exists('CONDICIONPRESERVACION', 
    'IDENTIFICADORCONDPRESERVACION = 5',
    'INSERT INTO CONDICIONPRESERVACION (IDENTIFICADORCONDPRESERVACION, DESCRIPCIONPRESERVACION) VALUES (5, ''Refrigeración mínimo 10 dias'')');
END;
/

BEGIN
  insert_if_not_exists('CONDICIONPRESERVACION', 
    'IDENTIFICADORCONDPRESERVACION = 6',
    'INSERT INTO CONDICIONPRESERVACION (IDENTIFICADORCONDPRESERVACION, DESCRIPCIONPRESERVACION) VALUES (6, ''Refrigeración a -70C'')');
END;
/

-- Insert Condicion data
BEGIN
  insert_if_not_exists('CONDICION', 
    'IDENTIFICADORCONDICION = 2 AND DESCRIPCIONCONDICION = ''Practicante de medicina''',
    'INSERT INTO CONDICION (IDENTIFICADORCONDICION, DESCRIPCIONCONDICION) VALUES (2, ''Practicante de medicina'')');
END;
/

BEGIN
  insert_if_not_exists('CONDICION', 
    'IDENTIFICADORCONDICION = 3 AND DESCRIPCIONCONDICION = ''Paciente con cancer de hígado''',
    'INSERT INTO CONDICION (IDENTIFICADORCONDICION, DESCRIPCIONCONDICION) VALUES (3, ''Paciente con cancer de hígado'')');
END;
/

-- Insert Ciudadano data
BEGIN
  insert_if_not_exists('CIUDADANO', 
    'TIPODEIDENTIFICACION = ''CC'' AND IDENTIFICACIONCIUDADANO = ''986294''',
    'INSERT INTO CIUDADANO (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, NOMBRECIUDADANO, APELLIDOCIUDADANO, ESVACUNABLE, FECHADENACIMIENTO, TELEFONODECONTACTO, ESTADOVACUNACION, ETAPA, PUNTOVACUNACION, NOMBREOFICINA, REGIONOFICINA, GENERO, ROL, CIUDAD, LOCALIDAD) VALUES (''CC'', ''986294'', ''Robert'', ''Yang'', ''1'', TO_DATE(''1998/11/1'', ''YYYY/MM/DD''), 23875496, 1, 4, ''CR 7A #122-09'', ''Compensar'', 12, ''Masculino'', ''Ciudadano'', ''Bogotá'', ''Centro'')');
END;
/

BEGIN
  insert_if_not_exists('CIUDADANO', 
    'TIPODEIDENTIFICACION = ''TI'' AND IDENTIFICACIONCIUDADANO = ''1000471065''',
    'INSERT INTO CIUDADANO (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, NOMBRECIUDADANO, APELLIDOCIUDADANO, ESVACUNABLE, FECHADENACIMIENTO, TELEFONODECONTACTO, ESTADOVACUNACION, ETAPA, PUNTOVACUNACION, NOMBREOFICINA, REGIONOFICINA, GENERO, ROL, CIUDAD, LOCALIDAD) VALUES (''TI'', ''1000471065'', ''Juan'', ''Jimenez'', ''1'', TO_DATE(''2000/12/1'', ''YYYY/MM/DD''), 324789, 1, 4, ''CR 8H #123-02'', ''Sanitas'', 10, ''Masculino'', ''Ciudadano'', ''Bogotá'', ''Centro'')');
END;
/

BEGIN
  insert_if_not_exists('CIUDADANO', 
    'TIPODEIDENTIFICACION = ''CC'' AND IDENTIFICACIONCIUDADANO = ''7490543''',
    'INSERT INTO CIUDADANO (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, NOMBRECIUDADANO, APELLIDOCIUDADANO, ESVACUNABLE, FECHADENACIMIENTO, TELEFONODECONTACTO, ESTADOVACUNACION, ETAPA, PUNTOVACUNACION, NOMBREOFICINA, REGIONOFICINA, GENERO, ROL, CIUDAD, LOCALIDAD) VALUES (''CC'', ''7490543'', ''Manuel'', ''Sarmiento'', ''1'', TO_DATE(''1956/12/1'', ''YYYY/MM/DD''), 3526897, 3, 2, ''CR 8H #123-02'', ''Sanitas'', 10, ''Masculino'', ''Doctor'', ''Bogotá'', ''Centro'')');
END;
/

-- Insert Vacuna data
BEGIN
  insert_if_not_exists('VACUNA', 
    'IDENTIFICADORVACUNA = 7',
    'INSERT INTO VACUNA (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO, TECNOLOGIA) VALUES (7, ''0'', 1, ''CR 7A #122-09'', ''CC'', ''986294'', ''ARNm'')');
END;
/

BEGIN
  insert_if_not_exists('VACUNA', 
    'IDENTIFICADORVACUNA = 8',
    'INSERT INTO VACUNA (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO, TECNOLOGIA) VALUES (8, ''0'', 2, ''CR 8H #123-02'', ''TI'', ''1000471065'', ''ARNm'')');
END;
/

BEGIN
  insert_if_not_exists('VACUNA', 
    'IDENTIFICADORVACUNA = 9',
    'INSERT INTO VACUNA (IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO, TECNOLOGIA) VALUES (9, ''1'', 2, ''CR 8H #123-02'', ''CC'', ''7490543'', ''ARNm'')');
END;
/

-- Insert TieneCondicionPreservacion data
BEGIN
  insert_if_not_exists('TIENECONDICIONPRESERVACION', 
    'IDENTIFICADORCONDICION = 5 AND IDENTIFICADORVACUNA = 8',
    'INSERT INTO TIENECONDICIONPRESERVACION (IDENTIFICADORCONDICION, IDENTIFICADORVACUNA) VALUES (5, 8)');
END;
/

BEGIN
  insert_if_not_exists('TIENECONDICIONPRESERVACION', 
    'IDENTIFICADORCONDICION = 6 AND IDENTIFICADORVACUNA = 7',
    'INSERT INTO TIENECONDICIONPRESERVACION (IDENTIFICADORCONDICION, IDENTIFICADORVACUNA) VALUES (6, 7)');
END;
/

-- Insert PuntoVacunacionVirtual data
BEGIN
  insert_if_not_exists('PUNTOVACUNACIONVIRTUAL', 
    'DIRPUNTOVACUNACIONVIRTUAL = ''CR 9 #155-09''',
    'INSERT INTO PUNTOVACUNACIONVIRTUAL (DIRPUNTOVACUNACIONVIRTUAL, NOMBREOFICINA, REGIONOFICINA, VACUNAASIGNADA) VALUES (''CR 9 #155-09'', ''Sanitas'', 10, 7)');
END;
/

BEGIN
  insert_if_not_exists('PUNTOVACUNACIONVIRTUAL', 
    'DIRPUNTOVACUNACIONVIRTUAL = ''CR 19B #35-09''',
    'INSERT INTO PUNTOVACUNACIONVIRTUAL (DIRPUNTOVACUNACIONVIRTUAL, NOMBREOFICINA, REGIONOFICINA, VACUNAASIGNADA) VALUES (''CR 19B #35-09'', ''Sanitas'', 10, 8)');
END;
/

-- Insert TalentoHumano data
BEGIN
  insert_if_not_exists('TALENTOHUMANO', 
    'TIPODEIDENTIFICACION = ''CC'' AND IDENTIFICACIONCIUDADANO = ''7490543''',
    'INSERT INTO TALENTOHUMANO (TIPODEIDENTIFICACION, IDENTIFICACIONCIUDADANO, FUNCIONTALENTOHUMANO) VALUES (''CC'', ''7490543'', ''Doctor'')');
END;
/

-- Insert Cita data
BEGIN
  insert_if_not_exists('CITA', 
    'IDENTIFICADORCITA = 23',
    'INSERT INTO CITA (IDENTIFICADORCITA, FECHACITA, TIPOIDCIUDADANO, IDCIUDADANO) VALUES (23, TO_TIMESTAMP(''10-SEP-21 14:10:10'', ''DD-MON-RR HH24:MI:SS''), ''CC'', ''986294'')');
END;
/

BEGIN
  insert_if_not_exists('CITA', 
    'IDENTIFICADORCITA = 24',
    'INSERT INTO CITA (IDENTIFICADORCITA, FECHACITA, TIPOIDCIUDADANO, IDCIUDADANO) VALUES (24, TO_TIMESTAMP(''1-JUN-21 16:10:10'', ''DD-MON-RR HH24:MI:SS''), ''TI'', ''1000471065'')');
END;
/

BEGIN
  insert_if_not_exists('CITA', 
    'IDENTIFICADORCITA = 25',
    'INSERT INTO CITA (IDENTIFICADORCITA, FECHACITA, TIPOIDCIUDADANO, IDCIUDADANO) VALUES (25, TO_TIMESTAMP(''8-MAY-21 08:30:00'', ''DD-MON-RR HH24:MI:SS''), ''CC'', ''7490543'')');
END;
/

-- Insert TrabajaEn data
BEGIN
  insert_if_not_exists('TRABAJAEN', 
    'TIPOIDTALENTOHUMANO = ''CC'' AND IDTALENTOHUMANO = ''7490543'' AND DIRECCIONPUNTOVACUNACION = ''CR 8H #123-02''',
    'INSERT INTO TRABAJAEN (TIPOIDTALENTOHUMANO, IDTALENTOHUMANO, DIRECCIONPUNTOVACUNACION) VALUES (''CC'', ''7490543'', ''CR 8H #123-02'')');
END;
/

-- Insert TieneInfraestructura data
BEGIN
  insert_if_not_exists('TIENEINFRAESTRUCTURA', 
    'IDENTIFICADORINFRAESTRUCTURA = 3 AND DIRECCIONPUNTOVACUNACION = ''CR 7A #122-09''',
    'INSERT INTO TIENEINFRAESTRUCTURA (IDENTIFICADORINFRAESTRUCTURA, DIRECCIONPUNTOVACUNACION) VALUES (3, ''CR 7A #122-09'')');
END;
/

BEGIN
  insert_if_not_exists('TIENEINFRAESTRUCTURA', 
    'IDENTIFICADORINFRAESTRUCTURA = 4 AND DIRECCIONPUNTOVACUNACION = ''CR 8H #123-02''',
    'INSERT INTO TIENEINFRAESTRUCTURA (IDENTIFICADORINFRAESTRUCTURA, DIRECCIONPUNTOVACUNACION) VALUES (4, ''CR 8H #123-02'')');
END;
/

-- Insert PerteneceA data
BEGIN
  insert_if_not_exists('PERTENECEA', 
    'TIPOIDCIUDADANO = ''CC'' AND IDCIUDADANO = ''7490543'' AND IDENTIFICADORCONDICION = 2',
    'INSERT INTO PERTENECEA (TIPOIDCIUDADANO, IDCIUDADANO, IDENTIFICADORCONDICION, DESCRIPCIONCONDICION) VALUES (''CC'', ''7490543'', 2, ''Practicante de medicina'')');
END;
/

-- Drop the helper procedure
DROP PROCEDURE insert_if_not_exists;

-- Commit all changes
COMMIT; 