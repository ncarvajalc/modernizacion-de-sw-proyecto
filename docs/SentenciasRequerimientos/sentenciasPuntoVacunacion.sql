--Sentencias usadas en la clase PuntoVacunacion
INSERT INTO PUNTOVACUNACION (DireccionPuntoVacunacion, NombrePuntoVacunacion,
                            CapacidadSimultanea, CapacidadDiaria,
                            DisponibilidadDeDosis, NombreOficina,
                            RegionOficina)
VALUES (?, ?,
        ?, ?,
        ?, ?,
        ?);

SELECT *
FROM PUNTOVACUNACION;
