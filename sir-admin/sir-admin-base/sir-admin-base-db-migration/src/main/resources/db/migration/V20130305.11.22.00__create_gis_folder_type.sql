-- Table: gis_folder_type

-- DROP TABLE gis_folder_type;

CREATE TABLE gis_folder_type
(
  id bigint NOT NULL,
  folder_type character varying,
  CONSTRAINT id PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE gis_folder_type OWNER TO ohiggins;

-- Initialize gis_folder_type

insert into gis_folder_type values (1, 'Canal');
insert into gis_folder_type values (2, 'PRC');
insert into gis_folder_type values (3, 'PRI');
insert into gis_folder_type values (4, 'PRS');
insert into gis_folder_type values (5, 'PRM');
insert into gis_folder_type values (6, 'PRDU');
insert into gis_folder_type values (7, 'Carpeta');

-- Update gis_folder table

ALTER TABLE gis_folder ADD COLUMN folder_type_id bigint;
UPDATE gis_folder SET folder_type_id = 1 WHERE is_plain IS NULL;
ALTER TABLE gis_folder ADD CONSTRAINT folder_type_id FOREIGN KEY (folder_type_id) REFERENCES gis_folder_type (id) MATCH FULL;
ALTER TABLE gis_folder DROP COLUMN is_plain RESTRICT;