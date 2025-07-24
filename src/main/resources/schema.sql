-- Schema creation for Vacuandes application
-- This file will be automatically executed by Spring Boot on startup

-- Check if sequence exists before creating
DECLARE
  seq_count INTEGER;
BEGIN
  SELECT COUNT(*) INTO seq_count FROM user_sequences WHERE sequence_name = 'VACUANDES_SEQUENCE';
  IF seq_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE SEQUENCE VACUANDES_SEQUENCE START WITH 100';
  END IF;
END;
/

-- Helper procedure to create table only if it doesn't exist
CREATE OR REPLACE PROCEDURE create_table_if_not_exists(table_name IN VARCHAR2, create_ddl IN CLOB)
IS
  table_count INTEGER;
BEGIN
  SELECT COUNT(*) INTO table_count FROM user_tables WHERE table_name = UPPER(table_name);
  IF table_count = 0 THEN
    EXECUTE IMMEDIATE create_ddl;
  END IF;
END;
/

-- Create OficinaRegionalEPS table
BEGIN
  create_table_if_not_exists('OficinaRegionalEPS', 
    'CREATE TABLE OficinaRegionalEPS (
      NombreEPS VARCHAR2(255) NOT NULL,
      RegionEPS NUMBER(2),
      CONSTRAINT OficinaRegionalEPS_PK PRIMARY KEY (NombreEPS,RegionEPS)
    )');
END;
/

-- Create PuntoVacunacionVirtual table
BEGIN
  create_table_if_not_exists('PuntoVacunacionVirtual',
    'CREATE TABLE PuntoVacunacionVirtual (
      DirPuntoVacunacionVirtual VARCHAR2(255) NOT NULL,
      NombreOficina VARCHAR2(255) NOT NULL,
      RegionOficina NUMBER(2) NOT NULL,
      VacunaAsignada NUMBER NOT NULL,
      CONSTRAINT PuntoVacunacionVirtual_PK PRIMARY KEY (DirPuntoVacunacionVirtual)
    )');
END;
/

-- Create LoteVacuna table
BEGIN
  create_table_if_not_exists('LoteVacuna',
    'CREATE TABLE LoteVacuna (
      IdentificadorLote NUMBER NOT NULL,
      NombreOficina VARCHAR2(255) NOT NULL,
      RegionOficina NUMBER(2) NOT NULL,
      CONSTRAINT LoteVacuna_PK PRIMARY KEY (IdentificadorLote)
    )');
END;
/

-- Create Infraestructura table
BEGIN
  create_table_if_not_exists('Infraestructura',
    'CREATE TABLE Infraestructura (
      IdentificadorInfraestructura NUMBER NOT NULL,
      DescripcionInfraestructura VARCHAR2(255) NOT NULL,
      CONSTRAINT Infraestructura_PK PRIMARY KEY (IdentificadorInfraestructura)
    )');
END;
/

-- Create CondicionPreservacion table
BEGIN
  create_table_if_not_exists('CondicionPreservacion',
    'CREATE TABLE CondicionPreservacion (
      IdentificadorCondPreservacion NUMBER NOT NULL,
      DescripcionPreservacion VARCHAR2(255) NOT NULL,
      CONSTRAINT CondicionPreservacion_PK PRIMARY KEY (IdentificadorCondPreservacion)
    )');
END;
/

-- Create EstadoVacunacion table
BEGIN
  create_table_if_not_exists('EstadoVacunacion',
    'CREATE TABLE EstadoVacunacion (
      IdentificadorEstado NUMBER NOT NULL,
      DescripcionEstado VARCHAR2(255) NOT NULL,
      CONSTRAINT Estado_PK PRIMARY KEY (IdentificadorEstado)
    )');
END;
/

-- Create Etapa table
BEGIN
  create_table_if_not_exists('Etapa',
    'CREATE TABLE Etapa (
      NumeroDeEtapa NUMBER(1) NOT NULL,
      DescripcionEtapa VARCHAR2(255) NOT NULL,
      CONSTRAINT Etapa_PK PRIMARY KEY (NumeroDeEtapa),
      CONSTRAINT CK_Etapa CHECK (NumeroDeEtapa >= 0 AND NumeroDeEtapa <= 5)
    )');
END;
/

-- Create Condicion table
BEGIN
  create_table_if_not_exists('Condicion',
    'CREATE TABLE Condicion (
      IdentificadorCondicion NUMBER NOT NULL,
      DescripcionCondicion VARCHAR2(255) NOT NULL,
      CONSTRAINT Condicion_PK PRIMARY KEY (IdentificadorCondicion,DescripcionCondicion)
    )');
END;
/

-- Create PuntoVacunacion table
BEGIN
  create_table_if_not_exists('PuntoVacunacion',
    'CREATE TABLE PuntoVacunacion (
      DireccionPuntoVacunacion VARCHAR2(255) NOT NULL,
      NombrePuntoVacunacion VARCHAR2(255) NOT NULL,
      CapacidadSimultanea NUMBER NOT NULL,
      CapacidadDiaria NUMBER NOT NULL,
      DisponibilidadDeDosis NUMBER NOT NULL,
      NombreOficina VARCHAR2(255) NOT NULL,
      RegionOficina NUMBER(2) NOT NULL,
      SoloMayores CHAR(1) DEFAULT ''0'' NOT NULL,
      SoloSalud CHAR(1) DEFAULT ''0'' NOT NULL,
      EstaHabilitado CHAR(1) DEFAULT ''1'' NOT NULL,
      CONSTRAINT PuntoVacunacion_PK PRIMARY KEY (DireccionPuntoVacunacion),
      CONSTRAINT CK_Cap1_Positiva CHECK (CapacidadSimultanea >= 0),
      CONSTRAINT CK_Cap2_Positiva CHECK (CapacidadDiaria > 0),
      CONSTRAINT CK_Cap3_Positiva CHECK (DisponibilidadDeDosis >= 0),
      CONSTRAINT CK_Bool5 CHECK (SoloMayores IN (''0'', ''1'')),
      CONSTRAINT CK_Bool6 CHECK (SoloSalud IN (''0'', ''1'')),
      CONSTRAINT CK_Bool7 CHECK (EstaHabilitado IN (''0'', ''1''))
    )');
END;
/

-- Create Ciudadano table
BEGIN
  create_table_if_not_exists('Ciudadano',
    'CREATE TABLE Ciudadano (
      TipoDeIdentificacion VARCHAR2(15) NOT NULL,
      IdentificacionCiudadano VARCHAR2(127) NOT NULL,
      NombreCiudadano VARCHAR2(255) NOT NULL,
      ApellidoCiudadano VARCHAR2(255) NOT NULL,
      EsVacunable Char(1) DEFAULT ''1'' NOT NULL,
      FechaDeNacimiento DATE NOT NULL,
      TelefonoDeContacto NUMBER NOT NULL,
      EstadoVacunacion NUMBER NOT NULL,
      Etapa NUMBER NOT NULL,
      PuntoVacunacion VARCHAR2(255),
      NombreOficina VARCHAR2(255) NOT NULL,
      RegionOficina NUMBER(2) NOT NULL,
      Genero VARCHAR2(10) DEFAULT ''Otro'' NOT NULL,
      Rol VARCHAR2(255) DEFAULT ''Ciudadano'' NOT NULL,
      Ciudad VARCHAR2(255) DEFAULT ''Bogotá'' NOT NULL,
      Localidad VARCHAR(255) DEFAULT ''Centro'' NOT NULL,
      CONSTRAINT Ciudadano_PK PRIMARY KEY (TipoDeIdentificacion, IdentificacionCiudadano),
      CONSTRAINT CK_TiposID CHECK (TipoDeIdentificacion IN (''TI'', ''CC'', ''PermisoEspecial'')),
      CONSTRAINT CK_Bool2 CHECK (EsVacunable IN (''0'', ''1'')),
      CONSTRAINT CK_Genero CHECK (Genero IN (''Masculino'', ''Femenino'', ''Otro''))
    )');
END;
/

-- Create Vacuna table
BEGIN
  create_table_if_not_exists('Vacuna',
    'CREATE TABLE Vacuna (
      IdentificadorVacuna NUMBER NOT NULL,
      HaSidoAplicado CHAR(1) DEFAULT ''0'' NOT NULL,
      LoteVacuna NUMBER NOT NULL,
      SegundaDosis NUMBER,
      PuntoVacunacion VARCHAR2(255) NOT NULL,
      TipoIdCiudadano VARCHAR2(15),
      IdCiudadano VARCHAR2(127),
      Tecnologia VARCHAR2(255) DEFAULT ''ARNm'' NOT NULL,
      CONSTRAINT Vacuna_PK PRIMARY KEY (IdentificadorVacuna),
      CONSTRAINT CK_Bool CHECK (HaSidoAplicado IN (''0'', ''1''))
    )');
END;
/

-- Create TalentoHumano table
BEGIN
  create_table_if_not_exists('TalentoHumano',
    'CREATE TABLE TalentoHumano (
      TipoDeIdentificacion VARCHAR2(15) NOT NULL,
      IdentificacionCiudadano VARCHAR2(127) NOT NULL,
      FuncionTalentoHumano VARCHAR2(15) NOT NULL,
      CONSTRAINT CK_RolesTalento CHECK (FuncionTalentoHumano IN (''Doctor'', ''Enfermero''))
    )');
END;
/

-- Create Cita table
BEGIN
  create_table_if_not_exists('Cita',
    'CREATE TABLE Cita (
      IdentificadorCita NUMBER NOT NULL,
      FechaCita TIMESTAMP NOT NULL,
      TipoIdCiudadano VARCHAR2(15) NOT NULL,
      IdCiudadano VARCHAR2(127) NOT NULL,
      CONSTRAINT Cita_PK PRIMARY KEY (IdentificadorCita)
    )');
END;
/

-- Create relationship tables
BEGIN
  create_table_if_not_exists('TieneCondicionPreservacion',
    'CREATE TABLE TieneCondicionPreservacion (
      IdentificadorCondicion NUMBER NOT NULL,
      IdentificadorVacuna NUMBER NOT NULL
    )');
END;
/

BEGIN
  create_table_if_not_exists('TrabajaEn',
    'CREATE TABLE TrabajaEn (
      TipoIdTalentoHumano VARCHAR2(15),
      IdTalentoHumano VARCHAR2(127),
      DireccionPuntoVacunacion VARCHAR2(255)
    )');
END;
/

BEGIN
  create_table_if_not_exists('TieneInfraestructura',
    'CREATE TABLE TieneInfraestructura (
      IdentificadorInfraestructura NUMBER NOT NULL,
      DireccionPuntoVacunacion VARCHAR2(255) NOT NULL
    )');
END;
/

BEGIN
  create_table_if_not_exists('PerteneceA',
    'CREATE TABLE PerteneceA (
      TipoIdCiudadano VARCHAR2(15),
      IdCiudadano VARCHAR2(127),
      IdentificadorCondicion NUMBER NOT NULL,
      DescripcionCondicion VARCHAR2(255) NOT NULL
    )');
END;
/

-- Drop the helper procedure
DROP PROCEDURE create_table_if_not_exists; 