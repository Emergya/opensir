-- Update gis_folder_type table
ALTER TABLE gis_folder_type ADD COLUMN folder_type_parent_id bigint;
ALTER TABLE gis_folder_type ADD CONSTRAINT folder_type_parent_id FOREIGN KEY (folder_type_parent_id) REFERENCES gis_folder_type (id);
-- insert IPT type
insert into gis_folder_type values (8, 'IPT', null);
-- update IPT children to reference to IPT type
update gis_folder_type set folder_type_parent_id = 8 where folder_type ilike 'pr%';