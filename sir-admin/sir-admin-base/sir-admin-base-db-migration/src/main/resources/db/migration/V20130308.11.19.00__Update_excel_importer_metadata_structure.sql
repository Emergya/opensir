--Update table structure
ALTER TABLE ohiggins.gis_acuerdos_core DROP COLUMN acuerdo_modifi, DROP COLUMN tipo, DROP COLUMN fuente_inversion, DROP COLUMN sub_fuente, DROP COLUMN nombre_iniciativa, DROP COLUMN comuna, DROP COLUMN sector, DROP COLUMN monto_ms;

--Update table structure
ALTER TABLE ohiggins.gis_base_preinversion_gore DROP COLUMN id, DROP COLUMN codigo_idi, 
	DROP COLUMN nro_iniciativas_postuladas, DROP COLUMN institucion, 
	DROP COLUMN acuerdo_core, DROP COLUMN oficio_envio_mideplan_dacg,
	ADD COLUMN mesa_tecnica CHARACTER VARYING(512);

-- Update table structure
ALTER TABLE ohiggins.gis_chileindica_ejecucion DROP COLUMN codigo, DROP COLUMN nombre_iniciativa, 
	DROP COLUMN rate, DROP COLUMN item_presupuestario, DROP COLUMN ubicacion_geografica, 
	DROP COLUMN sector, DROP COLUMN costo_core, DROP COLUMN costo_ebi, 
	DROP COLUMN costo_total_ajustado, DROP COLUMN solicitado, DROP COLUMN saldo_proximo_anyo, 
	DROP COLUMN saldo_anyos_restantes, DROP COLUMN total_asignado, DROP COLUMN asignacion_disponible, 
	DROP COLUMN saldo_por_asignar, DROP COLUMN pago_acumulado_a_septiembre, DROP COLUMN pagado_enero, 
	DROP COLUMN pagado_febrero, DROP COLUMN pagado_marzo, DROP COLUMN pagado_abril, DROP COLUMN pagado_mayo, 
	DROP COLUMN pagado_junio, DROP COLUMN pagado_julio, DROP COLUMN pagado_agosto, DROP COLUMN pagado_septiembre, 
	DROP COLUMN pagado_octubre, DROP COLUMN pagado_noviembre, DROP COLUMN pagado_diciembre, 
	DROP COLUMN porcentaje_avance_fisico, DROP COLUMN porcentaje_avance_financiero;

--Update table structure
ALTER TABLE ohiggins.gis_chileindica_ejecucion_detalle DROP COLUMN codigo, DROP COLUMN nivel_territorial; 
	
--Update table structure
ALTER TABLE ohiggins.gis_chileindica_preinversion ADD COLUMN nombre_iniciativa CHARACTER VARYING (2048),
	DROP COLUMN institucion_responsable;

--Update table structure
ALTER TABLE ohiggins.gis_proyectos_dacg DROP COLUMN id, DROP COLUMN codigo, DROP COLUMN nombre;

--Update table structure
ALTER TABLE ohiggins.gis_shp_proyectos_georreferenciados DROP COLUMN tipolo, 
	DROP COLUMN reg, DROP COLUMN prov, DROP COLUMN com, DROP COLUMN sector, DROP COLUMN subsec, 
	DROP COLUMN instit, DROP COLUMN etapa, DROP COLUMN coord_x, DROP COLUMN coord_y;   