CREATE TABLE ohiggins.gis_chileindica_inversion_data
(
   id bigserial, 
   region integer, 
   ano integer, 
   c_institucion integer, 
   c_preinversion integer, 
   c_ficha integer, 
   codigo integer, 
   c_tipo_codigo integer, 
   n_tipo_codigo character varying(128), 
   c_etapa_idi integer, 
   n_etapa_idi character varying(32), 
   nombre_institucion_responsable character varying(128), 
   nombre_unidad_tecnica character varying(128), 
   n_region character varying(64), 
   nombre_proyecto character varying(256), 
   item_presupuestario character varying(256), 
   nombre_item_presupuestario character varying(256), 
   nombre_provincia character varying(256), 
   nombre_comuna character varying(1024), 
   nombre_localidad character varying(1024), 
   costo_total_ajustado_inversion numeric(15,0), 
   gastado_anos_anteriores_inversion numeric(15,0), 
   solicitado_ano_inversion numeric(15,0), 
   saldo_proximo_ano_inversion numeric(15,0), 
   saldo_anos_restantes_inversion numeric(15,0), 
   total_asignado_inversion numeric(15,0), 
   asignacion_disponible_inversion numeric(15,0), 
   saldo_por_asignar_inversion numeric(15,0), 
   total_pagado_inversion numeric(15,0), 
   fecha_registro_chileindica date, 
   created_date timestamp default now(),
   updated_date timestamp default now(),
   CONSTRAINT gis_chileindica_inversion_pk PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;
SELECT AddGeometryColumn('ohiggins', 'gis_chileindica_inversion_data', 'the_geom', 4326, 'GEOMETRY', 2, true);

CREATE INDEX gis_chileindica_search_index
   ON gis_chileindica_inversion_data (ano ASC NULLS LAST, c_institucion ASC NULLS LAST, c_preinversion ASC NULLS LAST, c_ficha ASC NULLS LAST);



COMMENT ON COLUMN ohiggins.gis_chileindica_inversion_data.id IS 'Clave primaria';
COMMENT ON COLUMN ohiggins.gis_chileindica_inversion_data.fecha_registro_chileindica IS 'Fecha de registro de la última modificación a la iniciativa de inversión.';
COMMENT ON TABLE ohiggins.gis_chileindica_inversion_data
  IS 'Almacena los proyectos de inversión de Chileindica.';

  
  CREATE TABLE ohiggins.gis_inversion_financiamiento_data
(
   id bigserial, 
   codigo_fuente_financiamiento character varying(32), 
   nombre_fuente_financiamiento character varying(256), 
   asignacion_presupuestaria character varying(16), 
   nombre_asignacion_presupuestaria character varying(512), 
   costo_total_ebi numeric(15,0), 
   costo_total_ebi_actualizado numeric(15,0), 
   costo_total_ebi_actualizado_mas_diez_porciento numeric(15,0), 
   costo_total_core numeric(15,0), 
   costo_total_ajustado numeric(15,0), 
   gastado_anos_anteriores numeric(15,0), 
   solicitado_ano numeric(15,0), 
   saldo_proximo_ano numeric(15,0), 
   saldo_anos_restantes numeric(15,0), 
   total_asignado numeric(15,0), 
   asignacion_disponible numeric(15,0), 
   saldo_por_asignar numeric(15,0), 
   total_contratado numeric(15,0), 
   total_pagado numeric(15,0), 
   arrastre numeric(15,0), 
   total_programado numeric(15,0), 
   enero_programado numeric(15,0), 
   febrero_programado numeric(15,0), 
   marzo_programado numeric(15,0), 
   abril_programado numeric(15,0), 
   mayo_programado numeric(15,0), 
   junio_programado numeric(15,0), 
   julio_programado numeric(15,0), 
   agosto_programado numeric(15,0), 
   septiembre_programado numeric(15,0), 
   octubre_programado numeric(15,0), 
   noviembre_programado numeric(15,0), 
   diciembre_programado numeric(15,0), 
   enero_pagado numeric(15,0), 
   febrero_pagado numeric(15,0), 
   marzo_pagado numeric(15,0), 
   abril_pagado numeric(15,0), 
   mayo_pagado numeric(15,0), 
   junio_pagado numeric(15,0), 
   julio_pagado numeric(15,0), 
   agosto_pagado numeric(15,0), 
   septiembre_pagado numeric(15,0), 
   octubre_pagado numeric(15,0), 
   noviembre_pagado numeric(15,0), 
   diciembre_pagado numeric(15,0), 
   id_gis_chileindica_inversion_data bigint, 
   created_date timestamp default now(),
   updated_date timestamp default now(),
   CONSTRAINT gis_inversion_financiamiento_data_pk PRIMARY KEY (id),
   CONSTRAINT gis_inversion_financiamiento_data_gis_chileindica_inversion_data_fk FOREIGN KEY (id_gis_chileindica_inversion_data) REFERENCES gis_chileindica_inversion_data(id)
) 
WITH (
  OIDS = FALSE
)
;
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.codigo_fuente_financiamiento IS 'Código de la fuente de financiamiento';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.nombre_fuente_financiamiento IS 'Nombre de la fuente de financiamiento';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.asignacion_presupuestaria IS 'Clasificador presupuestario a nivel de asignación (formato XX.XX.XXX)';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.nombre_asignacion_presupuestaria IS 'Nombre de la asignación presupuestaria';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.costo_total_ebi IS 'Costo total EBI';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.costo_total_ebi_actualizado IS 'Costo total EBI Actualizado';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.costo_total_core IS 'Costo total CORE';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.costo_total_ajustado IS 'Costo total ajustado';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.solicitado_ano IS 'Monto solicitado para el año';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.saldo_proximo_ano IS 'Saldo del próximo año';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.saldo_anos_restantes IS 'Saldo de años restantes';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.total_asignado IS 'Monto total asignado';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.asignacion_disponible IS 'Asignación disponible';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.saldo_por_asignar IS 'Saldo por asignar';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.total_pagado IS 'Monto total pagado';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.arrastre IS 'Arrastre';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.enero_programado IS 'Monto programado Enero';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.febrero_programado IS 'Monto programado Febrero';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.marzo_programado IS 'Monto programado Marzo';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.abril_programado IS 'Monto programado Abril';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.mayo_programado IS 'Monto programado Mayo';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.junio_programado IS 'Monto programado Junio';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.julio_programado IS 'Monto programado Julio';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.agosto_programado IS 'Monto programado Agosto';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.septiembre_programado IS 'Monto programado Septiembre';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.octubre_programado IS 'Monto programado Octubre';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.noviembre_programado IS 'Monto programado Noviembre';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.diciembre_programado IS 'Monto programado Diciembre';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.enero_pagado IS 'Monto pagado Enero';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.febrero_pagado IS 'Monto pagado Febrero';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.marzo_pagado IS 'Monto pagado Marzo';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.abril_pagado IS 'Monto pagado Abril';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.mayo_pagado IS 'Monto pagado Mayo';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.junio_pagado IS 'Monto pagado Junio';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.julio_pagado IS 'Monto pagado Julio';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.agosto_pagado IS 'Monto pagado Agosto';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.septiembre_pagado IS 'Monto pagado Septiembre';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.octubre_pagado IS 'Monto pagado Octubre';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.noviembre_pagado IS 'Monto pagado Noviembre';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.diciembre_pagado IS 'Monto pagado Diciembre';
COMMENT ON COLUMN ohiggins.gis_inversion_financiamiento_data.id_gis_chileindica_inversion_data IS 'Clave ajena a gis_chileindica_inversion_data';


CREATE TABLE ohiggins.gis_relacion_intrumentos
(
   id bigserial, 
   codigo_instrumento integer, 
   nombre_instrumento character varying(256), 
   especificacion character varying(256), 
   relacion_principal_codigo character varying(128), 
   relacion_principal_nombre character varying(256), 
   relacion_asociada_codigo character varying(128), 
   relacion_asociada_nombre character varying(256), 
   id_gis_chileindica_inversion_data bigint,
   created_date timestamp default now(),
   updated_date timestamp default now(),
   CONSTRAINT relacion_instrumentos_data_pk PRIMARY KEY (id), 
   CONSTRAINT relacion_instrumentos_gis_chileindica_inversion_data_fk FOREIGN KEY (id_gis_chileindica_inversion_data) REFERENCES gis_chileindica_inversion_data (id) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
COMMENT ON COLUMN ohiggins.gis_relacion_intrumentos.id IS 'Clave primaria';
COMMENT ON COLUMN ohiggins.gis_relacion_intrumentos.codigo_instrumento IS 'Código del instrumento regional';
COMMENT ON COLUMN ohiggins.gis_relacion_intrumentos.nombre_instrumento IS 'Nombre del instrumento regional';
COMMENT ON COLUMN ohiggins.gis_relacion_intrumentos.especificacion IS 'Indica si el instrumento está relacionado a la iniciativa';
COMMENT ON COLUMN ohiggins.gis_relacion_intrumentos.relacion_principal_codigo IS 'Código de la relación principal entre el elemento del instrumento y la iniciativa';
COMMENT ON COLUMN ohiggins.gis_relacion_intrumentos.relacion_principal_nombre IS 'Nombre de la relación principal entre el elemento del instrumento y la iniciativa';
COMMENT ON COLUMN ohiggins.gis_relacion_intrumentos.relacion_asociada_codigo IS 'Código de la o las relaciones asociadas entre el elemento del instrumento y la iniciativa';
COMMENT ON COLUMN ohiggins.gis_relacion_intrumentos.relacion_asociada_nombre IS 'Nombre de la o las relaciones asociadas entre el elemento del instrumento y la iniciativa';
COMMENT ON TABLE ohiggins.gis_relacion_intrumentos
  IS 'Agrupa las relaciones entre el proyecto/iniciativa con los instrumentos regionales. Sólo muestra los instrumentos vigentes en el año de la iniciativa.';
