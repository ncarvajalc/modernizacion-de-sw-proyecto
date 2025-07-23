-- Foreign Key constraints and additional constraints
-- This runs after schema.sql

-- Helper procedure to add constraint only if it doesn't exist
CREATE OR REPLACE PROCEDURE add_constraint_if_not_exists(table_name IN VARCHAR2, constraint_name IN VARCHAR2, constraint_sql IN CLOB)
IS
  constraint_count INTEGER;
BEGIN
  SELECT COUNT(*) INTO constraint_count 
  FROM user_constraints 
  WHERE table_name = UPPER(table_name) AND constraint_name = UPPER(constraint_name);
  
  IF constraint_count = 0 THEN
    EXECUTE IMMEDIATE constraint_sql;
  END IF;
END;
/

-- Foreign Keys for PuntoVacunacionVirtual
BEGIN
  add_constraint_if_not_exists('PuntoVacunacionVirtual', 'OfEPS_FK_PVVirtual',
    'ALTER TABLE PuntoVacunacionVirtual ADD CONSTRAINT OfEPS_FK_PVVirtual FOREIGN KEY (NombreOficina,RegionOficina) REFERENCES OficinaRegionalEPS(NombreEPS,RegionEPS)');
END;
/

BEGIN
  add_constraint_if_not_exists('PuntoVacunacionVirtual', 'VacunaAsignada_FK',
    'ALTER TABLE PuntoVacunacionVirtual ADD CONSTRAINT VacunaAsignada_FK FOREIGN KEY (VacunaAsignada) REFERENCES Vacuna(IdentificadorVacuna)');
END;
/

-- Foreign Keys for LoteVacuna
BEGIN
  add_constraint_if_not_exists('LoteVacuna', 'OfEPS_FK_LoteVacuna',
    'ALTER TABLE LoteVacuna ADD CONSTRAINT OfEPS_FK_LoteVacuna FOREIGN KEY (NombreOficina,RegionOficina) REFERENCES OficinaRegionalEPS(NombreEPS,RegionEPS)');
END;
/

-- Foreign Keys and Primary Keys for TieneCondicionPreservacion
BEGIN
  add_constraint_if_not_exists('TieneCondicionPreservacion', 'FK_CondPreservacion',
    'ALTER TABLE TieneCondicionPreservacion ADD CONSTRAINT FK_CondPreservacion FOREIGN KEY (IdentificadorCondicion) REFERENCES CondicionPreservacion(IdentificadorCondPreservacion)');
END;
/

BEGIN
  add_constraint_if_not_exists('TieneCondicionPreservacion', 'FK_IdVacunaCP',
    'ALTER TABLE TieneCondicionPreservacion ADD CONSTRAINT FK_IdVacunaCP FOREIGN KEY (IdentificadorVacuna) REFERENCES Vacuna(IdentificadorVacuna)');
END;
/

BEGIN
  add_constraint_if_not_exists('TieneCondicionPreservacion', 'PK_TieneCondPreservacion',
    'ALTER TABLE TieneCondicionPreservacion ADD CONSTRAINT PK_TieneCondPreservacion PRIMARY KEY (IdentificadorCondicion,IdentificadorVacuna)');
END;
/

-- Foreign Keys for Vacuna
BEGIN
  add_constraint_if_not_exists('Vacuna', 'FK_LoteVacuna',
    'ALTER TABLE Vacuna ADD CONSTRAINT FK_LoteVacuna FOREIGN KEY (LoteVacuna) REFERENCES LoteVacuna(IdentificadorLote)');
END;
/

BEGIN
  add_constraint_if_not_exists('Vacuna', 'FK_SegundaDosis',
    'ALTER TABLE Vacuna ADD CONSTRAINT FK_SegundaDosis FOREIGN KEY (SegundaDosis) REFERENCES Vacuna(IdentificadorVacuna)');
END;
/

BEGIN
  add_constraint_if_not_exists('Vacuna', 'FK_PuntoVacunacion',
    'ALTER TABLE Vacuna ADD CONSTRAINT FK_PuntoVacunacion FOREIGN KEY (PuntoVacunacion) REFERENCES PuntoVacunacion(DireccionPuntoVacunacion)');
END;
/

BEGIN
  add_constraint_if_not_exists('Vacuna', 'FK_Ciudadano',
    'ALTER TABLE Vacuna ADD CONSTRAINT FK_Ciudadano FOREIGN KEY (TipoIdCiudadano,IdCiudadano) REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano)');
END;
/

-- Foreign Keys for PuntoVacunacion
BEGIN
  add_constraint_if_not_exists('PuntoVacunacion', 'OfEPS_FK_PuntoVac',
    'ALTER TABLE PuntoVacunacion ADD CONSTRAINT OfEPS_FK_PuntoVac FOREIGN KEY (NombreOficina,RegionOficina) REFERENCES OficinaRegionalEPS(NombreEPS,RegionEPS)');
END;
/

-- Foreign Keys and Primary Keys for TrabajaEn
BEGIN
  add_constraint_if_not_exists('TrabajaEn', 'FK_TalentoHumano',
    'ALTER TABLE TrabajaEn ADD CONSTRAINT FK_TalentoHumano FOREIGN KEY (TipoIdTalentoHumano,IdTalentoHumano) REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano)');
END;
/

BEGIN
  add_constraint_if_not_exists('TrabajaEn', 'FK_PuntoVac_TE',
    'ALTER TABLE TrabajaEn ADD CONSTRAINT FK_PuntoVac_TE FOREIGN KEY (DireccionPuntoVacunacion) REFERENCES PuntoVacunacion(DireccionPuntoVacunacion)');
END;
/

BEGIN
  add_constraint_if_not_exists('TrabajaEn', 'PK_TrabajaEn',
    'ALTER TABLE TrabajaEn ADD CONSTRAINT PK_TrabajaEn PRIMARY KEY (TipoIdTalentoHumano,IdTalentoHumano,DireccionPuntoVacunacion)');
END;
/

-- Foreign Keys for Ciudadano
BEGIN
  add_constraint_if_not_exists('Ciudadano', 'FK_EstadoVacunacion',
    'ALTER TABLE Ciudadano ADD CONSTRAINT FK_EstadoVacunacion FOREIGN KEY (EstadoVacunacion) REFERENCES EstadoVacunacion(IdentificadorEstado)');
END;
/

BEGIN
  add_constraint_if_not_exists('Ciudadano', 'FK_Etapa',
    'ALTER TABLE Ciudadano ADD CONSTRAINT FK_Etapa FOREIGN KEY (Etapa) REFERENCES Etapa(NumeroDeEtapa)');
END;
/

BEGIN
  add_constraint_if_not_exists('Ciudadano', 'FK_PuntoVac_Ciudadano',
    'ALTER TABLE Ciudadano ADD CONSTRAINT FK_PuntoVac_Ciudadano FOREIGN KEY (PuntoVacunacion) REFERENCES PuntoVacunacion(DireccionPuntoVacunacion)');
END;
/

BEGIN
  add_constraint_if_not_exists('Ciudadano', 'OfEPS_FK_Ciudadano',
    'ALTER TABLE Ciudadano ADD CONSTRAINT OfEPS_FK_Ciudadano FOREIGN KEY (NombreOficina,RegionOficina) REFERENCES OficinaRegionalEPS(NombreEPS,RegionEPS)');
END;
/

-- Foreign Keys and Primary Key for TalentoHumano
BEGIN
  add_constraint_if_not_exists('TalentoHumano', 'FK_TalentoHumano2',
    'ALTER TABLE TalentoHumano ADD CONSTRAINT FK_TalentoHumano2 FOREIGN KEY (TipoDeIdentificacion, IdentificacionCiudadano) REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano)');
END;
/

BEGIN
  add_constraint_if_not_exists('TalentoHumano', 'PK_TalentoHumano',
    'ALTER TABLE TalentoHumano ADD CONSTRAINT PK_TalentoHumano PRIMARY KEY (TipoDeIdentificacion, IdentificacionCiudadano)');
END;
/

-- Foreign Keys for Cita
BEGIN
  add_constraint_if_not_exists('Cita', 'FK_CiudadanoCitado',
    'ALTER TABLE Cita ADD CONSTRAINT FK_CiudadanoCitado FOREIGN KEY (TipoIdCiudadano, IdCiudadano) REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano)');
END;
/

-- Foreign Keys and Primary Keys for TieneInfraestructura
BEGIN
  add_constraint_if_not_exists('TieneInfraestructura', 'FK_IdInfraestructura',
    'ALTER TABLE TieneInfraestructura ADD CONSTRAINT FK_IdInfraestructura FOREIGN KEY (IdentificadorInfraestructura) REFERENCES Infraestructura(IdentificadorInfraestructura)');
END;
/

BEGIN
  add_constraint_if_not_exists('TieneInfraestructura', 'FK_PuntoVac_TI',
    'ALTER TABLE TieneInfraestructura ADD CONSTRAINT FK_PuntoVac_TI FOREIGN KEY (DireccionPuntoVacunacion) REFERENCES PuntoVacunacion(DireccionPuntoVacunacion)');
END;
/

BEGIN
  add_constraint_if_not_exists('TieneInfraestructura', 'PK_TieneInfraestructura',
    'ALTER TABLE TieneInfraestructura ADD CONSTRAINT PK_TieneInfraestructura PRIMARY KEY (IdentificadorInfraestructura,DireccionPuntoVacunacion)');
END;
/

-- Foreign Keys and Primary Keys for PerteneceA
BEGIN
  add_constraint_if_not_exists('PerteneceA', 'FK_CondicionCiudadano',
    'ALTER TABLE PerteneceA ADD CONSTRAINT FK_CondicionCiudadano FOREIGN KEY (TipoIdCiudadano,IdCiudadano) REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano)');
END;
/

BEGIN
  add_constraint_if_not_exists('PerteneceA', 'FK_IdCondicion',
    'ALTER TABLE PerteneceA ADD CONSTRAINT FK_IdCondicion FOREIGN KEY (IdentificadorCondicion, DescripcionCondicion) REFERENCES Condicion(IdentificadorCondicion, DescripcionCondicion)');
END;
/

BEGIN
  add_constraint_if_not_exists('PerteneceA', 'PK_PerteneceA',
    'ALTER TABLE PerteneceA ADD CONSTRAINT PK_PerteneceA PRIMARY KEY (TipoIdCiudadano,IdCiudadano,IdentificadorCondicion, DescripcionCondicion)');
END;
/

-- Drop the helper procedure
DROP PROCEDURE add_constraint_if_not_exists; 