-- Foreign Key constraints for DataNucleus/JDO compatibility
-- Uses standard Oracle DDL without PL/SQL blocks

-- Foreign Keys for PuntoVacunacionVirtual
ALTER TABLE PuntoVacunacionVirtual 
ADD CONSTRAINT OfEPS_FK_PVVirtual 
FOREIGN KEY (NombreOficina,RegionOficina) 
REFERENCES OficinaRegionalEPS(NombreEPS,RegionEPS);

ALTER TABLE PuntoVacunacionVirtual 
ADD CONSTRAINT VacunaAsignada_FK 
FOREIGN KEY (VacunaAsignada) 
REFERENCES Vacuna(IdentificadorVacuna);

-- Foreign Keys for LoteVacuna
ALTER TABLE LoteVacuna 
ADD CONSTRAINT OfEPS_FK_LoteVacuna 
FOREIGN KEY (NombreOficina,RegionOficina) 
REFERENCES OficinaRegionalEPS(NombreEPS,RegionEPS);

-- Foreign Keys and Primary Keys for TieneCondicionPreservacion
ALTER TABLE TieneCondicionPreservacion 
ADD CONSTRAINT FK_CondPreservacion 
FOREIGN KEY (IdentificadorCondicion) 
REFERENCES CondicionPreservacion(IdentificadorCondPreservacion);

ALTER TABLE TieneCondicionPreservacion 
ADD CONSTRAINT FK_IdVacunaCP 
FOREIGN KEY (IdentificadorVacuna) 
REFERENCES Vacuna(IdentificadorVacuna);

ALTER TABLE TieneCondicionPreservacion 
ADD CONSTRAINT PK_TieneCondPreservacion 
PRIMARY KEY (IdentificadorCondicion,IdentificadorVacuna);

-- Foreign Keys for Vacuna
ALTER TABLE Vacuna 
ADD CONSTRAINT FK_LoteVacuna 
FOREIGN KEY (LoteVacuna) 
REFERENCES LoteVacuna(IdentificadorLote);

ALTER TABLE Vacuna 
ADD CONSTRAINT FK_SegundaDosis 
FOREIGN KEY (SegundaDosis) 
REFERENCES Vacuna(IdentificadorVacuna);

ALTER TABLE Vacuna 
ADD CONSTRAINT FK_PuntoVacunacion 
FOREIGN KEY (PuntoVacunacion) 
REFERENCES PuntoVacunacion(DireccionPuntoVacunacion);

ALTER TABLE Vacuna 
ADD CONSTRAINT FK_Ciudadano 
FOREIGN KEY (TipoIdCiudadano,IdCiudadano) 
REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano);

-- Foreign Keys for PuntoVacunacion
ALTER TABLE PuntoVacunacion 
ADD CONSTRAINT OfEPS_FK_PuntoVac 
FOREIGN KEY (NombreOficina,RegionOficina) 
REFERENCES OficinaRegionalEPS(NombreEPS,RegionEPS);

-- Foreign Keys and Primary Keys for TrabajaEn
ALTER TABLE TrabajaEn 
ADD CONSTRAINT FK_TalentoHumano 
FOREIGN KEY (TipoIdTalentoHumano,IdTalentoHumano) 
REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano);

ALTER TABLE TrabajaEn 
ADD CONSTRAINT FK_PuntoVac_TE 
FOREIGN KEY (DireccionPuntoVacunacion) 
REFERENCES PuntoVacunacion(DireccionPuntoVacunacion);

ALTER TABLE TrabajaEn 
ADD CONSTRAINT PK_TrabajaEn 
PRIMARY KEY (TipoIdTalentoHumano,IdTalentoHumano,DireccionPuntoVacunacion);

-- Foreign Keys for Ciudadano
ALTER TABLE Ciudadano 
ADD CONSTRAINT FK_EstadoVacunacion 
FOREIGN KEY (EstadoVacunacion) 
REFERENCES EstadoVacunacion(IdentificadorEstado);

ALTER TABLE Ciudadano 
ADD CONSTRAINT FK_Etapa 
FOREIGN KEY (Etapa) 
REFERENCES Etapa(NumeroDeEtapa);

ALTER TABLE Ciudadano 
ADD CONSTRAINT FK_PuntoVac_Ciudadano 
FOREIGN KEY (PuntoVacunacion) 
REFERENCES PuntoVacunacion(DireccionPuntoVacunacion);

ALTER TABLE Ciudadano 
ADD CONSTRAINT OfEPS_FK_Ciudadano 
FOREIGN KEY (NombreOficina,RegionOficina) 
REFERENCES OficinaRegionalEPS(NombreEPS,RegionEPS);

-- Foreign Keys and Primary Key for TalentoHumano
ALTER TABLE TalentoHumano 
ADD CONSTRAINT FK_TalentoHumano2 
FOREIGN KEY (TipoDeIdentificacion, IdentificacionCiudadano) 
REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano);

ALTER TABLE TalentoHumano 
ADD CONSTRAINT PK_TalentoHumano 
PRIMARY KEY (TipoDeIdentificacion, IdentificacionCiudadano);

-- Foreign Keys for Cita
ALTER TABLE Cita 
ADD CONSTRAINT FK_CiudadanoCitado 
FOREIGN KEY (TipoIdCiudadano, IdCiudadano) 
REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano);

-- Foreign Keys and Primary Keys for TieneInfraestructura
ALTER TABLE TieneInfraestructura 
ADD CONSTRAINT FK_IdInfraestructura 
FOREIGN KEY (IdentificadorInfraestructura) 
REFERENCES Infraestructura(IdentificadorInfraestructura);

ALTER TABLE TieneInfraestructura 
ADD CONSTRAINT FK_PuntoVac_TI 
FOREIGN KEY (DireccionPuntoVacunacion) 
REFERENCES PuntoVacunacion(DireccionPuntoVacunacion);

ALTER TABLE TieneInfraestructura 
ADD CONSTRAINT PK_TieneInfraestructura 
PRIMARY KEY (IdentificadorInfraestructura,DireccionPuntoVacunacion);

-- Foreign Keys and Primary Keys for PerteneceA
ALTER TABLE PerteneceA 
ADD CONSTRAINT FK_CondicionCiudadano 
FOREIGN KEY (TipoIdCiudadano,IdCiudadano) 
REFERENCES Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano);

ALTER TABLE PerteneceA 
ADD CONSTRAINT FK_IdCondicion 
FOREIGN KEY (IdentificadorCondicion, DescripcionCondicion) 
REFERENCES Condicion(IdentificadorCondicion, DescripcionCondicion);

ALTER TABLE PerteneceA 
ADD CONSTRAINT PK_PerteneceA 
PRIMARY KEY (TipoIdCiudadano,IdCiudadano,IdentificadorCondicion, DescripcionCondicion); 