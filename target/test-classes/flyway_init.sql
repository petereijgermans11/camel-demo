-- Flyway init script for test purposes
-- Flyway fails to initialize in test setup because of the "public" schema.
-- By creating a jdbc string like below you can still enable flyway and have your tests working
-- jdbc string: "jdbc:h2:mem:DBNAMEHERE;MODE=MySQL;INIT=RUNSCRIPT FROM 'classpath:flyway_init.sql'"
CREATE SCHEMA IF NOT EXISTS "public";