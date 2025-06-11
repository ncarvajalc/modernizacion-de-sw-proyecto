--En caso de querer limpiar tablas
DELETE FROM cita;
DELETE FROM talentohumano;
DELETE FROM pertenecea;
DELETE FROM condicion;
DELETE FROM trabajaen;
DELETE FROM ciudadano;
DELETE FROM puntovacunacion;
DELETE FROM lotevacuna;
DELETE FROM oficinaregionaleps;
DELETE FROM estadovacunacion;
DELETE FROM etapa;

--Pruebas de unicidad
--Insertar PK inexistente
INSERT INTO "OFICINAREGIONALEPS" (NOMBREEPS, REGIONEPS)
VALUES ('Saludmás', 1);

--Insertar PK existente
INSERT INTO "OFICINAREGIONALEPS" (NOMBREEPS, REGIONEPS)
VALUES ('Saludmás', 1);
