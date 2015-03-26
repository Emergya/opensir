-- ADD NEW LAYER TYPES 
INSERT INTO ohiggins.gis_layer_type (id, name, tipo) 
VALUES(nextval('ohiggins.gis_layer_type_seq'), 'imagemosaic', 'Raster');
INSERT INTO ohiggins.gis_layer_type (id, name, tipo) 
VALUES(nextval('ohiggins.gis_layer_type_seq'), 'imageworld', 'Raster');

