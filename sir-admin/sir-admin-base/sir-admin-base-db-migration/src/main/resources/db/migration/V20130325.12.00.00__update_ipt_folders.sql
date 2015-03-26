-- Update the folders which folder_type_id is null
update gis_folder set folder_type_id = 2 where folder_type_id is null and folder_zone_id in (select id from gis_zone where type ilike 'M');
update gis_folder set folder_type_id = 3 where folder_type_id is null and folder_zone_id in (select id from gis_zone where type not like 'M');