update ohiggins.gis_layer set layer_layer_type_id=7 where layer_layer_type_id=4 and table_name is not null;
update ohiggins.gis_layer_resources set type_layer_id=7 where type_layer_id=4;