--Sentencias usadas en la clase Ciudadano
INSERT INTO CIUDADANO (TipoDeIdentificacion, IdentificacionCiudadano,
                       NombreCiudadano, ApellidoCiudadano, EsVacunable,
                       FechaDeNacimiento, TelefonoDeContacto, EstadoVacunacion,
                       Etapa, NombreOficina, RegionOficina)
VALUES (?, ?,
        ?, ? ,? ,
        ?, ?, ?,
        ?, ?, ?);

UPDATE CIUDADANO
SET PuntoVacunacion = ?
WHERE TipoDeIdentificacion = ? AND
      IdentificacionCiudadano = ?;

SELECT *
FROM CIUDADANO
WHERE TipoDeIdentificacion = ? AND
      IdentificacionCiudadano = ?;

UPDATE CIUDADANO
SET etapa = ?
WHERE TipoDeIdentificacion = ? AND
      IdentificacionCiudadano = ?;
