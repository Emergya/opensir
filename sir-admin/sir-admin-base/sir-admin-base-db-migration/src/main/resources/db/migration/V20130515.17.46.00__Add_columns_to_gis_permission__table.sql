-- Add filter, name, ptype and config columns to gis_permission

ALTER TABLE ohiggins.gis_permission ADD COLUMN "filter" varchar(256);
ALTER TABLE ohiggins.gis_permission ADD COLUMN "name" varchar(256);
ALTER TABLE ohiggins.gis_permission ADD COLUMN "ptype" varchar(256);
ALTER TABLE ohiggins.gis_permission ADD COLUMN "config" varchar(256);

-- //TODO: alter column name_permission to name