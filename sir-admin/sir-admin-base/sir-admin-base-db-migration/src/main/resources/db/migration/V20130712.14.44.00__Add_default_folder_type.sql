-- insert DEFAULT type
insert into gis_folder_type values (9, 'DEFAULT', null);
-- update DEFAULT children to reference to the DEFAULT type
update gis_folder_type set folder_type_parent_id = 9 where id in (1,7);