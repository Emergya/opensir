--
-- PostgreSQL database dump
--
SET search_path = ohiggins, pg_catalog;

--
-- Name: folder_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE folder_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.folder_seq OWNER TO ohiggins;

--
-- Name: folder_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('folder_seq', 1, false);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: gis_acuerdos_core; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_acuerdos_core (
    gis_ohiggins_id integer NOT NULL,
    acuerdo_modifi character varying(2048),
    anyo character varying(255),
    codigo_bip character varying(2048),
    comuna character varying(2048),
    costo_total_ms numeric(19,2),
    etapa character varying(2048),
    fecha_acuerdo character varying(2048),
    fuente_inversion character varying(2048),
    monto_ms numeric(19,2),
    nombre_iniciativa character varying(2048),
    numero_acuerdo character varying(2048),
    sector character varying(2048),
    servicio_responsable character varying(255),
    sub_fuente character varying(2048),
    tipo character varying(2048),
    investment_initiative_update_id bigint
);


ALTER TABLE ohiggins.gis_acuerdos_core OWNER TO ohiggins;

--
-- Name: gis_acuerdos_core_gis_ohiggins_id_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_acuerdos_core_gis_ohiggins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_acuerdos_core_gis_ohiggins_id_seq OWNER TO ohiggins;

--
-- Name: gis_acuerdos_core_gis_ohiggins_id_seq; Type: SEQUENCE OWNED BY; Schema: ohiggins; Owner: ohiggins
--

ALTER SEQUENCE gis_acuerdos_core_gis_ohiggins_id_seq OWNED BY gis_acuerdos_core.gis_ohiggins_id;


--
-- Name: gis_acuerdos_core_gis_ohiggins_id_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_acuerdos_core_gis_ohiggins_id_seq', 293, true);


--
-- Name: gis_authority; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_authority (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    name character varying(255) NOT NULL,
    update_date timestamp without time zone,
    auth_type_id bigint,
    auth_parent_id bigint,
    zone_id bigint
);


ALTER TABLE ohiggins.gis_authority OWNER TO ohiggins;

--
-- Name: gis_authority_gis_layer; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_authority_gis_layer (
    gis_authority_id bigint NOT NULL,
    layerlist_id bigint NOT NULL
);


ALTER TABLE ohiggins.gis_authority_gis_layer OWNER TO ohiggins;

--
-- Name: gis_authority_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_authority_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_authority_seq OWNER TO ohiggins;

--
-- Name: gis_authority_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_authority_seq', 4, true);


--
-- Name: gis_authority_type; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_authority_type (
    id bigint NOT NULL,
    citizen boolean,
    create_date timestamp without time zone,
    name_auth_type character varying(255) NOT NULL,
    update_date timestamp without time zone
);


ALTER TABLE ohiggins.gis_authority_type OWNER TO ohiggins;

--
-- Name: gis_authority_type_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_authority_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_authority_type_seq OWNER TO ohiggins;

--
-- Name: gis_authority_type_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_authority_type_seq', 1, true);


--
-- Name: gis_base_preinversion_gore; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_base_preinversion_gore (
    gis_ohiggins_id bigint NOT NULL,
    acuerdo_core character varying(2048),
    anyo character varying(255),
    atributos_que_presenta character varying(2048),
    carpeta_digital_bip character varying(2048),
    codigo_bip character varying(255),
    codigo_idi character varying(2048),
    convenio_programacion character varying(2048),
    erd character varying(2048),
    etapa character varying(255),
    id character varying(2048),
    ingresado_mod_preinv_chilenindica character varying(2048),
    institucion character varying(2048),
    lineamiento_ude character varying(2048),
    notas_observaciones character varying(2048),
    nro_iniciativas_postuladas character varying(2048),
    nro_of_institucion character varying(2048),
    oficio_envio_mideplan_dacg character varying(2048),
    plan_ohiggins_2010_2014 character varying(2048),
    plan_reconstruc_27f character varying(2048),
    politica_regional_cyt character varying(2048),
    politica_regional_turismo character varying(2048),
    seleccionada_por_intendente character varying(2048),
    servicio_responsable character varying(255),
    total_de_atributos character varying(2048),
    investment_initiative_update_id bigint
);


ALTER TABLE ohiggins.gis_base_preinversion_gore OWNER TO ohiggins;

--
-- Name: gis_base_preinversion_gore_gis_ohiggins_id_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_base_preinversion_gore_gis_ohiggins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_base_preinversion_gore_gis_ohiggins_id_seq OWNER TO ohiggins;

--
-- Name: gis_base_preinversion_gore_gis_ohiggins_id_seq; Type: SEQUENCE OWNED BY; Schema: ohiggins; Owner: ohiggins
--

ALTER SEQUENCE gis_base_preinversion_gore_gis_ohiggins_id_seq OWNED BY gis_base_preinversion_gore.gis_ohiggins_id;


--
-- Name: gis_base_preinversion_gore_gis_ohiggins_id_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_base_preinversion_gore_gis_ohiggins_id_seq', 798, true);


--
-- Name: gis_chileindica_ejecucion; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_chileindica_ejecucion (
    gis_ohiggins_id bigint NOT NULL,
    anyo character varying(255),
    asignacion_disponible numeric(19,2),
    codigo character varying(2048),
    codigo_bip character varying(255),
    costo_core numeric(19,2),
    costo_ebi numeric(19,2),
    costo_total_ajustado numeric(19,2),
    etapa character varying(2048),
    gastado_anyos_anteriores numeric(19,2),
    item_presupuestario character varying(2048),
    nombre_iniciativa character varying(2048),
    pagado_abril numeric(19,2),
    pagado_agosto numeric(19,2),
    pagado_diciembre numeric(19,2),
    pagado_enero numeric(19,2),
    pagado_febrero numeric(19,2),
    pagado_julio numeric(19,2),
    pagado_junio numeric(19,2),
    pagado_marzo numeric(19,2),
    pagado_mayo numeric(19,2),
    pagado_noviembre numeric(19,2),
    pagado_octubre numeric(19,2),
    pagado_septiembre numeric(19,2),
    pago_acumulado_a_septiembre numeric(19,2),
    porcentaje_avance_financiero character varying(255),
    porcentaje_avance_fisico character varying(255),
    rate character varying(2048),
    saldo_anyos_restantes numeric(19,2),
    saldo_por_asignar numeric(19,2),
    saldo_proximo_anyo numeric(19,2),
    sector character varying(2048),
    servicio_responsable character varying(2048),
    solicitado numeric(19,2),
    total_asignado numeric(19,2),
    total_pagado numeric(19,2),
    ubicacion_geografica character varying(2048),
    investment_initiative_update_id bigint
);


ALTER TABLE ohiggins.gis_chileindica_ejecucion OWNER TO ohiggins;

--
-- Name: gis_chileindica_ejecucion_detalle; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_chileindica_ejecucion_detalle (
    gis_ohiggins_id bigint NOT NULL,
    anyo character varying(2048),
    codigo character varying(2048),
    codigo_bip character varying(255),
    comuna character varying(2048),
    costo_ajustado numeric(19,2),
    descripcion character varying(2048),
    etapa character varying(2048),
    fuente_financiamiento character varying(2048),
    gastado_anyos_anteriores numeric(19,2),
    nivel_territorial character varying(2048),
    nombre_iniciativa character varying(2048),
    pagado numeric(19,2),
    sector character varying(2048),
    servicio_responsable character varying(2048),
    solicitado numeric(19,2),
    unidad_tecnica character varying(2048),
    investment_initiative_update_id bigint
);


ALTER TABLE ohiggins.gis_chileindica_ejecucion_detalle OWNER TO ohiggins;

--
-- Name: gis_chileindica_ejecucion_detalle_gis_ohiggins_id_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_chileindica_ejecucion_detalle_gis_ohiggins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_chileindica_ejecucion_detalle_gis_ohiggins_id_seq OWNER TO ohiggins;

--
-- Name: gis_chileindica_ejecucion_detalle_gis_ohiggins_id_seq; Type: SEQUENCE OWNED BY; Schema: ohiggins; Owner: ohiggins
--

ALTER SEQUENCE gis_chileindica_ejecucion_detalle_gis_ohiggins_id_seq OWNED BY gis_chileindica_ejecucion_detalle.gis_ohiggins_id;


--
-- Name: gis_chileindica_ejecucion_detalle_gis_ohiggins_id_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_chileindica_ejecucion_detalle_gis_ohiggins_id_seq', 2411, true);


--
-- Name: gis_chileindica_ejecucion_gis_ohiggins_id_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_chileindica_ejecucion_gis_ohiggins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_chileindica_ejecucion_gis_ohiggins_id_seq OWNER TO ohiggins;

--
-- Name: gis_chileindica_ejecucion_gis_ohiggins_id_seq; Type: SEQUENCE OWNED BY; Schema: ohiggins; Owner: ohiggins
--

ALTER SEQUENCE gis_chileindica_ejecucion_gis_ohiggins_id_seq OWNED BY gis_chileindica_ejecucion.gis_ohiggins_id;


--
-- Name: gis_chileindica_ejecucion_gis_ohiggins_id_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_chileindica_ejecucion_gis_ohiggins_id_seq', 1970, true);


--
-- Name: gis_chileindica_preinversion; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_chileindica_preinversion (
    gis_ohiggins_id bigint NOT NULL,
    anyo character varying(2048),
    codigo_bip character varying(2048),
    comuna character varying(2048),
    costo_total numeric(19,2),
    estado character varying(2048),
    etapa character varying(2048),
    fuente_financiamiento character varying(2048),
    institucion_responsable character varying(2048),
    observaciones character varying(2048),
    sector character varying(2048),
    servicio_responsable character varying(255),
    solicitado_anyo numeric(19,2),
    via_de_financiamiento character varying(2048),
    investment_initiative_update_id bigint
);


ALTER TABLE ohiggins.gis_chileindica_preinversion OWNER TO ohiggins;

--
-- Name: gis_chileindica_preinversion_gis_ohiggins_id_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_chileindica_preinversion_gis_ohiggins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_chileindica_preinversion_gis_ohiggins_id_seq OWNER TO ohiggins;

--
-- Name: gis_chileindica_preinversion_gis_ohiggins_id_seq; Type: SEQUENCE OWNED BY; Schema: ohiggins; Owner: ohiggins
--

ALTER SEQUENCE gis_chileindica_preinversion_gis_ohiggins_id_seq OWNED BY gis_chileindica_preinversion.gis_ohiggins_id;


--
-- Name: gis_chileindica_preinversion_gis_ohiggins_id_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_chileindica_preinversion_gis_ohiggins_id_seq', 690, true);


--
-- Name: gis_contacto; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_contacto (
    id bigint NOT NULL,
    descripcion text NOT NULL,
    email character varying(255) NOT NULL,
    fecha_actualizacion timestamp without time zone,
    fecha_creacion timestamp without time zone,
    leido boolean,
    nombre character varying(255),
    titulo character varying(255) NOT NULL
);


ALTER TABLE ohiggins.gis_contacto OWNER TO ohiggins;

--
-- Name: gis_faq; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_faq (
    id bigint NOT NULL,
    update_date timestamp without time zone,
    create_date timestamp without time zone,
    habilitada boolean,
    modulo character varying(255),
    respuesta text NOT NULL,
    titulo character varying(255) NOT NULL
);


ALTER TABLE ohiggins.gis_faq OWNER TO ohiggins;

--
-- Name: gis_file_column; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_file_column (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    dbname character varying(255),
    name character varying(255),
    type character varying(255),
    update_date timestamp without time zone,
    filetype_id bigint
);


ALTER TABLE ohiggins.gis_file_column OWNER TO ohiggins;

--
-- Name: gis_file_type; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_file_type (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    table_name character varying(255),
    type_name character varying(255),
    update_date timestamp without time zone
);


ALTER TABLE ohiggins.gis_file_type OWNER TO ohiggins;

--
-- Name: gis_folder; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_folder (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    enabled boolean,
    folder_order integer,
    is_channel boolean,
    is_plain boolean,
    name character varying(255),
    update_date timestamp without time zone,
    folder_auth_id bigint,
    folder_parent_id bigint,
    folder_user_id bigint,
    folder_zone_id bigint
);


ALTER TABLE ohiggins.gis_folder OWNER TO ohiggins;

--
-- Name: gis_investment_initiative_update; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_investment_initiative_update (
    id bigint NOT NULL,
    enabled boolean,
    last_update_date timestamp without time zone,
    filetype_id bigint
);


ALTER TABLE ohiggins.gis_investment_initiative_update OWNER TO ohiggins;

--
-- Name: gis_investment_initiative_update_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_investment_initiative_update_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_investment_initiative_update_seq OWNER TO ohiggins;

--
-- Name: gis_investment_initiative_update_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_investment_initiative_update_seq', 11, true);


--
-- Name: gis_layer; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_layer (
    id bigint NOT NULL,
    creat_date timestamp without time zone,
    data oid,
    enabled boolean,
    is_channel boolean,
    name_layer character varying(255),
    order_layer character varying(255),
    publicized boolean,
    server_resource character varying(255),
    update_date timestamp without time zone,
    layer_auth_id bigint,
    layer_folder_id bigint,
    layer_metadata_id bigint,
    layer_layer_type_id bigint,
    layer_user_id bigint
);


ALTER TABLE ohiggins.gis_layer OWNER TO ohiggins;

--
-- Name: gis_layer_gis_layer_property; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_layer_gis_layer_property (
    gis_layer_id bigint NOT NULL,
    properties_id bigint NOT NULL
);


ALTER TABLE ohiggins.gis_layer_gis_layer_property OWNER TO ohiggins;

--
-- Name: gis_layer_gis_style; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_layer_gis_style (
    gis_layer_id bigint NOT NULL,
    stylelist_id bigint NOT NULL
);


ALTER TABLE ohiggins.gis_layer_gis_style OWNER TO ohiggins;

--
-- Name: gis_layer_metadata; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_layer_metadata (
    id bigint NOT NULL,
    calidad character varying(255),
    crs_pr character varying(255),
    distribuidor character varying(255),
    update_date date,
    create_date date,
    formato character varying(255),
    frecuencia character varying(255),
    informacion text,
    informacion_cpr character varying(255),
    informacion_geo_localizacion character varying(255),
    informacion_pr character varying(255),
    nombre_pr character varying(255),
    otros text,
    periodo character varying(255),
    posicion character varying(255),
    "precision" character varying(255),
    puntos_referencia character varying(255),
    rango character varying(255),
    referencia character varying(255),
    requerimientos text,
    responsable text,
    siguiente date
);


ALTER TABLE ohiggins.gis_layer_metadata OWNER TO ohiggins;

--
-- Name: gis_layer_metadata_tmp; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_layer_metadata_tmp (
    id bigint NOT NULL,
    calidad character varying(255),
    crs_pr character varying(255),
    distribuidor character varying(255),
    update_date date,
    create_date date,
    formato character varying(255),
    frecuencia character varying(255),
    informacion text,
    informacion_cpr character varying(255),
    informacion_geo_localizacion character varying(255),
    informacion_pr character varying(255),
    nombre_pr character varying(255),
    otros text,
    periodo character varying(255),
    posicion character varying(255),
    "precision" character varying(255),
    puntos_referencia character varying(255),
    rango character varying(255),
    referencia character varying(255),
    requerimientos text,
    responsable text,
    siguiente date
);


ALTER TABLE ohiggins.gis_layer_metadata_tmp OWNER TO ohiggins;

--
-- Name: gis_layer_property; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_layer_property (
    id bigint NOT NULL,
    name character varying(255),
    value character varying(255)
);


ALTER TABLE ohiggins.gis_layer_property OWNER TO ohiggins;

--
-- Name: gis_layer_property_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_layer_property_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_layer_property_seq OWNER TO ohiggins;

--
-- Name: gis_layer_property_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_layer_property_seq', 1, false);


--
-- Name: gis_layer_publish_request; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_layer_publish_request (
    id bigint NOT NULL,
    actualizacion boolean,
    comentario character varying(255),
    estado character varying(255),
    update_date timestamp without time zone,
    create_date timestamp without time zone,
    fecha_respuesta timestamp without time zone,
    fecha_solicitud timestamp without time zone,
    leida boolean,
    nombre_deseado character varying(255),
    recurso_servidor character varying(255),
    layer_publish_request_auth_id bigint,
    layer_publish_request_layer_id bigint,
    layer_publish_request_metadata_tmp_id bigint,
    layer_publish_request_user_id bigint
);


ALTER TABLE ohiggins.gis_layer_publish_request OWNER TO ohiggins;

--
-- Name: gis_layer_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_layer_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_layer_seq OWNER TO ohiggins;

--
-- Name: gis_layer_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_layer_seq', 1, false);


--
-- Name: gis_layer_type; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_layer_type (
    id bigint NOT NULL,
    name character varying(255),
    tipo character varying(255)
);


ALTER TABLE ohiggins.gis_layer_type OWNER TO ohiggins;

--
-- Name: gis_layer_type_property; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_layer_type_property (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE ohiggins.gis_layer_type_property OWNER TO ohiggins;

--
-- Name: gis_layer_type_property_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_layer_type_property_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_layer_type_property_seq OWNER TO ohiggins;

--
-- Name: gis_layer_type_property_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_layer_type_property_seq', 1, false);


--
-- Name: gis_layer_type_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_layer_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_layer_type_seq OWNER TO ohiggins;

--
-- Name: gis_layer_type_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_layer_type_seq', 1, false);


--
-- Name: gis_map_conf; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_map_conf (
    id bigint NOT NULL,
    pdfserver character varying(255),
    bbox character varying(255),
    defaultidioma character varying(255),
    defaultuserlogo character varying(255),
    defaultwmsserver character varying(255),
    displayprojection character varying(255),
    downloadservleturl character varying(255),
    initialbbox character varying(255),
    maxresolution character varying(255),
    maxscale character varying(255),
    minresolution character varying(255),
    minscale character varying(255),
    numzoomlevels character varying(255),
    openlayersproxyhost character varying(255),
    projection character varying(255),
    resolutions character varying(255),
    uploadservleturl character varying(255),
    version character varying(255)
);


ALTER TABLE ohiggins.gis_map_conf OWNER TO ohiggins;

--
-- Name: gis_map_conf_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_map_conf_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_map_conf_seq OWNER TO ohiggins;

--
-- Name: gis_map_conf_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_map_conf_seq', 1, false);


--
-- Name: gis_permission; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_permission (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    name_permission character varying(255),
    update_date timestamp without time zone
);


ALTER TABLE ohiggins.gis_permission OWNER TO ohiggins;

--
-- Name: gis_permission_by_authtype; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_permission_by_authtype (
    auth_type_id bigint NOT NULL,
    permission_id bigint NOT NULL
);


ALTER TABLE ohiggins.gis_permission_by_authtype OWNER TO ohiggins;

--
-- Name: gis_property_in_layer_type; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_property_in_layer_type (
    gis_layer_type_id bigint NOT NULL,
    defaultproperties_id bigint NOT NULL
);


ALTER TABLE ohiggins.gis_property_in_layer_type OWNER TO ohiggins;

--
-- Name: gis_proyectos_dacg; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_proyectos_dacg (
    gis_ohiggins_id bigint NOT NULL,
    anyo character varying(255),
    codigo character varying(2048),
    codigo_bip character varying(255),
    etapa character varying(2048),
    fecha_observacion character varying(2048),
    id character varying(2048),
    nombre character varying(2048),
    observacion character varying(2048),
    servicio_responsable character varying(255),
    investment_initiative_update_id bigint
);


ALTER TABLE ohiggins.gis_proyectos_dacg OWNER TO ohiggins;

--
-- Name: gis_proyectos_dacg_gis_ohiggins_id_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_proyectos_dacg_gis_ohiggins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_proyectos_dacg_gis_ohiggins_id_seq OWNER TO ohiggins;

--
-- Name: gis_proyectos_dacg_gis_ohiggins_id_seq; Type: SEQUENCE OWNED BY; Schema: ohiggins; Owner: ohiggins
--

ALTER SEQUENCE gis_proyectos_dacg_gis_ohiggins_id_seq OWNED BY gis_proyectos_dacg.gis_ohiggins_id;


--
-- Name: gis_proyectos_dacg_gis_ohiggins_id_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_proyectos_dacg_gis_ohiggins_id_seq', 129, true);


--
-- Name: gis_proyectos_georreferenciados_mideso; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_proyectos_georreferenciados_mideso (
    gis_ohiggins_id bigint NOT NULL,
    cod_bip character varying(2048),
    etapa character varying(2048),
    nombre character varying(2048),
    sector character varying(2048),
    investment_initiative_update_id bigint
);


ALTER TABLE ohiggins.gis_proyectos_georreferenciados_mideso OWNER TO ohiggins;

--
-- Name: gis_proyectos_georreferenciados_mideso_gis_ohiggins_id_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_proyectos_georreferenciados_mideso_gis_ohiggins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_proyectos_georreferenciados_mideso_gis_ohiggins_id_seq OWNER TO ohiggins;

--
-- Name: gis_proyectos_georreferenciados_mideso_gis_ohiggins_id_seq; Type: SEQUENCE OWNED BY; Schema: ohiggins; Owner: ohiggins
--

ALTER SEQUENCE gis_proyectos_georreferenciados_mideso_gis_ohiggins_id_seq OWNED BY gis_proyectos_georreferenciados_mideso.gis_ohiggins_id;


--
-- Name: gis_proyectos_georreferenciados_mideso_gis_ohiggins_id_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_proyectos_georreferenciados_mideso_gis_ohiggins_id_seq', 1, false);


--
-- Name: gis_proyectos_mideso; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_proyectos_mideso (
    gis_ohiggins_id bigint NOT NULL,
    anyo character varying(255),
    codigo_bip character varying(2048),
    etapa character varying(2048),
    fecha_rate character varying(2048),
    rate character varying(2048),
    servicio_responsable character varying(255),
    investment_initiative_update_id bigint
);


ALTER TABLE ohiggins.gis_proyectos_mideso OWNER TO ohiggins;

--
-- Name: gis_proyectos_mideso_gis_ohiggins_id_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_proyectos_mideso_gis_ohiggins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_proyectos_mideso_gis_ohiggins_id_seq OWNER TO ohiggins;

--
-- Name: gis_proyectos_mideso_gis_ohiggins_id_seq; Type: SEQUENCE OWNED BY; Schema: ohiggins; Owner: ohiggins
--

ALTER SEQUENCE gis_proyectos_mideso_gis_ohiggins_id_seq OWNED BY gis_proyectos_mideso.gis_ohiggins_id;


--
-- Name: gis_proyectos_mideso_gis_ohiggins_id_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_proyectos_mideso_gis_ohiggins_id_seq', 2056, true);


--
-- Name: gis_rule; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_rule (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    filter character varying(255),
    symbolizer character varying(255),
    update_date timestamp without time zone
);


ALTER TABLE ohiggins.gis_rule OWNER TO ohiggins;

--
-- Name: gis_rule_gis_rule_property; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_rule_gis_rule_property (
    gis_rule_id bigint NOT NULL,
    properties_id bigint NOT NULL
);


ALTER TABLE ohiggins.gis_rule_gis_rule_property OWNER TO ohiggins;

--
-- Name: gis_rule_property; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_rule_property (
    id bigint NOT NULL,
    name character varying(255),
    value character varying(255)
);


ALTER TABLE ohiggins.gis_rule_property OWNER TO ohiggins;

--
-- Name: gis_rule_property_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_rule_property_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_rule_property_seq OWNER TO ohiggins;

--
-- Name: gis_rule_property_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_rule_property_seq', 1, false);


--
-- Name: gis_rule_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_rule_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_rule_seq OWNER TO ohiggins;

--
-- Name: gis_rule_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_rule_seq', 1, false);


--
-- Name: gis_sector; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_sector (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    nombre character varying(255),
    update_date timestamp without time zone
);


ALTER TABLE ohiggins.gis_sector OWNER TO ohiggins;

--
-- Name: gis_shp_proyectos_georreferenciados; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_shp_proyectos_georreferenciados (
    gis_ohiggins_id bigint NOT NULL,
    cod_bip character varying(255),
    com character varying(255),
    coord_x numeric(19,2),
    coord_y numeric(19,2),
    etapa character varying(255),
    instit character varying(255),
    nombre character varying(255),
    prov character varying(255),
    reg character varying(255),
    sector character varying(255),
    subsec character varying(255),
    the_geom public.geometry,
    tipolo character varying(255),
    investment_initiative_update_id bigint
);


ALTER TABLE ohiggins.gis_shp_proyectos_georreferenciados OWNER TO ohiggins;

--
-- Name: gis_shp_proyectos_georreferenciados_gis_ohiggins_id_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_shp_proyectos_georreferenciados_gis_ohiggins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_shp_proyectos_georreferenciados_gis_ohiggins_id_seq OWNER TO ohiggins;

--
-- Name: gis_shp_proyectos_georreferenciados_gis_ohiggins_id_seq; Type: SEQUENCE OWNED BY; Schema: ohiggins; Owner: ohiggins
--

ALTER SEQUENCE gis_shp_proyectos_georreferenciados_gis_ohiggins_id_seq OWNED BY gis_shp_proyectos_georreferenciados.gis_ohiggins_id;


--
-- Name: gis_shp_proyectos_georreferenciados_gis_ohiggins_id_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_shp_proyectos_georreferenciados_gis_ohiggins_id_seq', 967, true);


--
-- Name: gis_style; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_style (
    id bigint NOT NULL,
    create_date timestamp without time zone,
    name_style character varying(255),
    update_date timestamp without time zone
);


ALTER TABLE ohiggins.gis_style OWNER TO ohiggins;

--
-- Name: gis_style_gis_rule; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_style_gis_rule (
    gis_style_id bigint NOT NULL,
    rulelist_id bigint NOT NULL
);


ALTER TABLE ohiggins.gis_style_gis_rule OWNER TO ohiggins;

--
-- Name: gis_style_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_style_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_style_seq OWNER TO ohiggins;

--
-- Name: gis_style_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_style_seq', 1, false);


--
-- Name: gis_user; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_user (
    id bigint NOT NULL,
    admin boolean,
    apellidos character varying(255),
    create_date timestamp without time zone,
    email character varying(255),
    nombre_completo character varying(255),
    password character varying(255) NOT NULL,
    telefono character varying(255),
    update_date timestamp without time zone,
    username character varying(255) NOT NULL,
    valid boolean,
    user_authority_id bigint
);


ALTER TABLE ohiggins.gis_user OWNER TO ohiggins;

--
-- Name: gis_user_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_user_seq OWNER TO ohiggins;

--
-- Name: gis_user_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_user_seq', 1, true);


--
-- Name: gis_zone; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_zone (
    id bigint NOT NULL,
    code character varying(255),
    create_date timestamp without time zone,
    enabled boolean,
    geom public.geometry,
    name character varying(255),
    type character varying(255),
    update_date timestamp without time zone,
    nivelpadre_id bigint
);


ALTER TABLE ohiggins.gis_zone OWNER TO ohiggins;

--
-- Name: gis_zone_in_zone; Type: TABLE; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

CREATE TABLE gis_zone_in_zone (
    zone_id bigint NOT NULL,
    subzone_id bigint NOT NULL
);


ALTER TABLE ohiggins.gis_zone_in_zone OWNER TO ohiggins;

--
-- Name: gis_zone_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE gis_zone_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.gis_zone_seq OWNER TO ohiggins;

--
-- Name: gis_zone_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('gis_zone_seq', 1, false);


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.hibernate_sequence OWNER TO ohiggins;

--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('hibernate_sequence', 20358, true);


--
-- Name: permission_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE permission_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.permission_seq OWNER TO ohiggins;

--
-- Name: permission_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('permission_seq', 1, false);


--
-- Name: sector_seq; Type: SEQUENCE; Schema: ohiggins; Owner: ohiggins
--

CREATE SEQUENCE sector_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ohiggins.sector_seq OWNER TO ohiggins;

--
-- Name: sector_seq; Type: SEQUENCE SET; Schema: ohiggins; Owner: ohiggins
--

SELECT pg_catalog.setval('sector_seq', 1, false);


SET search_path = public, pg_catalog;


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: ohiggins
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO ohiggins;

--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: ohiggins
--

SELECT pg_catalog.setval('hibernate_sequence', 330, true);


SET default_with_oids = false;

--
-- Name: request; Type: TABLE; Schema: public; Owner: ohiggins; Tablespace:
--

CREATE TABLE request (
    id bigint NOT NULL,
    status character varying(255),
    category character varying(255),
    path character varying(255),
    body bytea,
    query_string character varying(4096),
    body_content_type character varying(255),
    body_content_length bigint,
    server_host character varying(255),
    internal_server_host character varying(255),
    http_method character varying(255),
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    total_time bigint,
    remote_address character varying(255),
    remote_host character varying(255),
    remote_user character varying(255),
    remote_user_agent character varying(1024),
    remote_country character varying(255),
    remote_city character varying(255),
    remote_lat double precision,
    remote_lon double precision,
    service character varying(255),
    operation character varying(255),
    sub_operation character varying(255),
    ows_version character varying(255),
    content_type character varying(255),
    response_length bigint,
    error_message character varying(1024),
    exception_stack_trace oid
);


ALTER TABLE public.request OWNER TO ohiggins;

--
-- Name: request_resources; Type: TABLE; Schema: public; Owner: ohiggins; Tablespace:
--

CREATE TABLE request_resources (
    request_id bigint NOT NULL,
    name character varying(255),
    idx integer NOT NULL
);


ALTER TABLE public.request_resources OWNER TO ohiggins;


SET search_path = ohiggins, pg_catalog;

--
-- Name: gis_ohiggins_id; Type: DEFAULT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_acuerdos_core ALTER COLUMN gis_ohiggins_id SET DEFAULT nextval('gis_acuerdos_core_gis_ohiggins_id_seq'::regclass);


--
-- Name: gis_ohiggins_id; Type: DEFAULT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_base_preinversion_gore ALTER COLUMN gis_ohiggins_id SET DEFAULT nextval('gis_base_preinversion_gore_gis_ohiggins_id_seq'::regclass);


--
-- Name: gis_ohiggins_id; Type: DEFAULT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_chileindica_ejecucion ALTER COLUMN gis_ohiggins_id SET DEFAULT nextval('gis_chileindica_ejecucion_gis_ohiggins_id_seq'::regclass);


--
-- Name: gis_ohiggins_id; Type: DEFAULT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_chileindica_ejecucion_detalle ALTER COLUMN gis_ohiggins_id SET DEFAULT nextval('gis_chileindica_ejecucion_detalle_gis_ohiggins_id_seq'::regclass);


--
-- Name: gis_ohiggins_id; Type: DEFAULT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_chileindica_preinversion ALTER COLUMN gis_ohiggins_id SET DEFAULT nextval('gis_chileindica_preinversion_gis_ohiggins_id_seq'::regclass);


--
-- Name: gis_ohiggins_id; Type: DEFAULT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_proyectos_dacg ALTER COLUMN gis_ohiggins_id SET DEFAULT nextval('gis_proyectos_dacg_gis_ohiggins_id_seq'::regclass);


--
-- Name: gis_ohiggins_id; Type: DEFAULT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_proyectos_georreferenciados_mideso ALTER COLUMN gis_ohiggins_id SET DEFAULT nextval('gis_proyectos_georreferenciados_mideso_gis_ohiggins_id_seq'::regclass);


--
-- Name: gis_ohiggins_id; Type: DEFAULT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_proyectos_mideso ALTER COLUMN gis_ohiggins_id SET DEFAULT nextval('gis_proyectos_mideso_gis_ohiggins_id_seq'::regclass);


--
-- Name: gis_ohiggins_id; Type: DEFAULT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_shp_proyectos_georreferenciados ALTER COLUMN gis_ohiggins_id SET DEFAULT nextval('gis_shp_proyectos_georreferenciados_gis_ohiggins_id_seq'::regclass);



SET search_path = ohiggins, pg_catalog;

--
-- Name: gis_acuerdos_core_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_acuerdos_core
    ADD CONSTRAINT gis_acuerdos_core_pkey PRIMARY KEY (gis_ohiggins_id);


--
-- Name: gis_authority_gis_layer_layerlist_id_key; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_authority_gis_layer
    ADD CONSTRAINT gis_authority_gis_layer_layerlist_id_key UNIQUE (layerlist_id);


--
-- Name: gis_authority_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_authority
    ADD CONSTRAINT gis_authority_pkey PRIMARY KEY (id);


--
-- Name: gis_authority_type_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_authority_type
    ADD CONSTRAINT gis_authority_type_pkey PRIMARY KEY (id);


--
-- Name: gis_base_preinversion_gore_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_base_preinversion_gore
    ADD CONSTRAINT gis_base_preinversion_gore_pkey PRIMARY KEY (gis_ohiggins_id);


--
-- Name: gis_chileindica_ejecucion_detalle_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_chileindica_ejecucion_detalle
    ADD CONSTRAINT gis_chileindica_ejecucion_detalle_pkey PRIMARY KEY (gis_ohiggins_id);


--
-- Name: gis_chileindica_ejecucion_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_chileindica_ejecucion
    ADD CONSTRAINT gis_chileindica_ejecucion_pkey PRIMARY KEY (gis_ohiggins_id);


--
-- Name: gis_chileindica_preinversion_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_chileindica_preinversion
    ADD CONSTRAINT gis_chileindica_preinversion_pkey PRIMARY KEY (gis_ohiggins_id);


--
-- Name: gis_contacto_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_contacto
    ADD CONSTRAINT gis_contacto_pkey PRIMARY KEY (id);


--
-- Name: gis_faq_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_faq
    ADD CONSTRAINT gis_faq_pkey PRIMARY KEY (id);


--
-- Name: gis_file_column_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_file_column
    ADD CONSTRAINT gis_file_column_pkey PRIMARY KEY (id);


--
-- Name: gis_file_type_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_file_type
    ADD CONSTRAINT gis_file_type_pkey PRIMARY KEY (id);


--
-- Name: gis_file_type_type_name_key; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_file_type
    ADD CONSTRAINT gis_file_type_type_name_key UNIQUE (type_name);


--
-- Name: gis_folder_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_folder
    ADD CONSTRAINT gis_folder_pkey PRIMARY KEY (id);


--
-- Name: gis_investment_initiative_update_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_investment_initiative_update
    ADD CONSTRAINT gis_investment_initiative_update_pkey PRIMARY KEY (id);


--
-- Name: gis_layer_gis_layer_property_properties_id_key; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_layer_gis_layer_property
    ADD CONSTRAINT gis_layer_gis_layer_property_properties_id_key UNIQUE (properties_id);


--
-- Name: gis_layer_gis_style_stylelist_id_key; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_layer_gis_style
    ADD CONSTRAINT gis_layer_gis_style_stylelist_id_key UNIQUE (stylelist_id);


--
-- Name: gis_layer_metadata_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_layer_metadata
    ADD CONSTRAINT gis_layer_metadata_pkey PRIMARY KEY (id);


--
-- Name: gis_layer_metadata_tmp_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_layer_metadata_tmp
    ADD CONSTRAINT gis_layer_metadata_tmp_pkey PRIMARY KEY (id);


--
-- Name: gis_layer_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_layer
    ADD CONSTRAINT gis_layer_pkey PRIMARY KEY (id);


--
-- Name: gis_layer_property_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_layer_property
    ADD CONSTRAINT gis_layer_property_pkey PRIMARY KEY (id);


--
-- Name: gis_layer_publish_request_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_layer_publish_request
    ADD CONSTRAINT gis_layer_publish_request_pkey PRIMARY KEY (id);


--
-- Name: gis_layer_type_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_layer_type
    ADD CONSTRAINT gis_layer_type_pkey PRIMARY KEY (id);


--
-- Name: gis_layer_type_property_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_layer_type_property
    ADD CONSTRAINT gis_layer_type_property_pkey PRIMARY KEY (id);


--
-- Name: gis_map_conf_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_map_conf
    ADD CONSTRAINT gis_map_conf_pkey PRIMARY KEY (id);


--
-- Name: gis_permission_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_permission
    ADD CONSTRAINT gis_permission_pkey PRIMARY KEY (id);


--
-- Name: gis_proyectos_dacg_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_proyectos_dacg
    ADD CONSTRAINT gis_proyectos_dacg_pkey PRIMARY KEY (gis_ohiggins_id);


--
-- Name: gis_proyectos_georreferenciados_mideso_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_proyectos_georreferenciados_mideso
    ADD CONSTRAINT gis_proyectos_georreferenciados_mideso_pkey PRIMARY KEY (gis_ohiggins_id);


--
-- Name: gis_proyectos_mideso_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_proyectos_mideso
    ADD CONSTRAINT gis_proyectos_mideso_pkey PRIMARY KEY (gis_ohiggins_id);


--
-- Name: gis_rule_gis_rule_property_properties_id_key; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_rule_gis_rule_property
    ADD CONSTRAINT gis_rule_gis_rule_property_properties_id_key UNIQUE (properties_id);


--
-- Name: gis_rule_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_rule
    ADD CONSTRAINT gis_rule_pkey PRIMARY KEY (id);


--
-- Name: gis_rule_property_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_rule_property
    ADD CONSTRAINT gis_rule_property_pkey PRIMARY KEY (id);


--
-- Name: gis_sector_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_sector
    ADD CONSTRAINT gis_sector_pkey PRIMARY KEY (id);


--
-- Name: gis_shp_proyectos_georreferenciados_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_shp_proyectos_georreferenciados
    ADD CONSTRAINT gis_shp_proyectos_georreferenciados_pkey PRIMARY KEY (gis_ohiggins_id);


--
-- Name: gis_style_gis_rule_rulelist_id_key; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_style_gis_rule
    ADD CONSTRAINT gis_style_gis_rule_rulelist_id_key UNIQUE (rulelist_id);


--
-- Name: gis_style_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_style
    ADD CONSTRAINT gis_style_pkey PRIMARY KEY (id);


--
-- Name: gis_user_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_user
    ADD CONSTRAINT gis_user_pkey PRIMARY KEY (id);


--
-- Name: gis_zone_in_zone_subzone_id_key; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_zone_in_zone
    ADD CONSTRAINT gis_zone_in_zone_subzone_id_key UNIQUE (subzone_id);


--
-- Name: gis_zone_pkey; Type: CONSTRAINT; Schema: ohiggins; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY gis_zone
    ADD CONSTRAINT gis_zone_pkey PRIMARY KEY (id);


SET search_path = public, pg_catalog;



--
-- Name: request_pkey; Type: CONSTRAINT; Schema: public; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY request
    ADD CONSTRAINT request_pkey PRIMARY KEY (id);


--
-- Name: request_resources_pkey; Type: CONSTRAINT; Schema: public; Owner: ohiggins; Tablespace:
--

ALTER TABLE ONLY request_resources
    ADD CONSTRAINT request_resources_pkey PRIMARY KEY (request_id, idx);



SET search_path = ohiggins, pg_catalog;

--
-- Name: fk171e5f92e760a455; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_shp_proyectos_georreferenciados
    ADD CONSTRAINT fk171e5f92e760a455 FOREIGN KEY (investment_initiative_update_id) REFERENCES gis_investment_initiative_update(id);


--
-- Name: fk19924c9c30de5f20; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_folder
    ADD CONSTRAINT fk19924c9c30de5f20 FOREIGN KEY (folder_zone_id) REFERENCES gis_zone(id);


--
-- Name: fk19924c9c375063e4; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_folder
    ADD CONSTRAINT fk19924c9c375063e4 FOREIGN KEY (folder_parent_id) REFERENCES gis_folder(id);


--
-- Name: fk19924c9c646ae913; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_folder
    ADD CONSTRAINT fk19924c9c646ae913 FOREIGN KEY (folder_auth_id) REFERENCES gis_authority(id);


--
-- Name: fk19924c9cc322a380; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_folder
    ADD CONSTRAINT fk19924c9cc322a380 FOREIGN KEY (folder_user_id) REFERENCES gis_user(id);


--
-- Name: fk19c4f960e760a455; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_proyectos_dacg
    ADD CONSTRAINT fk19c4f960e760a455 FOREIGN KEY (investment_initiative_update_id) REFERENCES gis_investment_initiative_update(id);


--
-- Name: fk29aab40ba0360851; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_file_column
    ADD CONSTRAINT fk29aab40ba0360851 FOREIGN KEY (filetype_id) REFERENCES gis_file_type(id);


--
-- Name: fk2a12fbf99245f617; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_authority_gis_layer
    ADD CONSTRAINT fk2a12fbf99245f617 FOREIGN KEY (gis_authority_id) REFERENCES gis_authority(id);


--
-- Name: fk2a12fbf99fce444b; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_authority_gis_layer
    ADD CONSTRAINT fk2a12fbf99fce444b FOREIGN KEY (layerlist_id) REFERENCES gis_layer(id);


--
-- Name: fk2c0b648fe760a455; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_chileindica_ejecucion
    ADD CONSTRAINT fk2c0b648fe760a455 FOREIGN KEY (investment_initiative_update_id) REFERENCES gis_investment_initiative_update(id);


--
-- Name: fk32d4ad8d52841dd7; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer_gis_layer_property
    ADD CONSTRAINT fk32d4ad8d52841dd7 FOREIGN KEY (gis_layer_id) REFERENCES gis_layer(id);


--
-- Name: fk32d4ad8d62486f3c; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer_gis_layer_property
    ADD CONSTRAINT fk32d4ad8d62486f3c FOREIGN KEY (properties_id) REFERENCES gis_layer_property(id);


--
-- Name: fk349dce83413a3a16; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer_publish_request
    ADD CONSTRAINT fk349dce83413a3a16 FOREIGN KEY (layer_publish_request_auth_id) REFERENCES gis_authority(id);


--
-- Name: fk349dce836a503753; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer_publish_request
    ADD CONSTRAINT fk349dce836a503753 FOREIGN KEY (layer_publish_request_metadata_tmp_id) REFERENCES gis_layer_metadata_tmp(id);


--
-- Name: fk349dce839ff1f483; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer_publish_request
    ADD CONSTRAINT fk349dce839ff1f483 FOREIGN KEY (layer_publish_request_user_id) REFERENCES gis_user(id);


--
-- Name: fk349dce83c8b4137; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer_publish_request
    ADD CONSTRAINT fk349dce83c8b4137 FOREIGN KEY (layer_publish_request_layer_id) REFERENCES gis_layer(id);


--
-- Name: fk378271e752841dd7; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer_gis_style
    ADD CONSTRAINT fk378271e752841dd7 FOREIGN KEY (gis_layer_id) REFERENCES gis_layer(id);


--
-- Name: fk378271e753e504b; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer_gis_style
    ADD CONSTRAINT fk378271e753e504b FOREIGN KEY (stylelist_id) REFERENCES gis_style(id);


--
-- Name: fk42cbfcc8e760a455; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_acuerdos_core
    ADD CONSTRAINT fk42cbfcc8e760a455 FOREIGN KEY (investment_initiative_update_id) REFERENCES gis_investment_initiative_update(id);


--
-- Name: fk467772a16b65e9f1; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_zone_in_zone
    ADD CONSTRAINT fk467772a16b65e9f1 FOREIGN KEY (zone_id) REFERENCES gis_zone(id);


--
-- Name: fk467772a1afdda3b1; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_zone_in_zone
    ADD CONSTRAINT fk467772a1afdda3b1 FOREIGN KEY (subzone_id) REFERENCES gis_zone(id);


--
-- Name: fk47b64f79b973605d; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_user
    ADD CONSTRAINT fk47b64f79b973605d FOREIGN KEY (user_authority_id) REFERENCES gis_authority(id);


--
-- Name: fk47b8875ad44d4b59; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_zone
    ADD CONSTRAINT fk47b8875ad44d4b59 FOREIGN KEY (nivelpadre_id) REFERENCES gis_zone(id);


--
-- Name: fk60e6dda8936375d5; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_permission_by_authtype
    ADD CONSTRAINT fk60e6dda8936375d5 FOREIGN KEY (auth_type_id) REFERENCES gis_authority_type(id);


--
-- Name: fk60e6dda8c9bed5d1; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_permission_by_authtype
    ADD CONSTRAINT fk60e6dda8c9bed5d1 FOREIGN KEY (permission_id) REFERENCES gis_permission(id);


--
-- Name: fk67655d8e760a455; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_chileindica_preinversion
    ADD CONSTRAINT fk67655d8e760a455 FOREIGN KEY (investment_initiative_update_id) REFERENCES gis_investment_initiative_update(id);


--
-- Name: fk678a0d6ae760a455; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_base_preinversion_gore
    ADD CONSTRAINT fk678a0d6ae760a455 FOREIGN KEY (investment_initiative_update_id) REFERENCES gis_investment_initiative_update(id);


--
-- Name: fk680a09c7e760a455; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_chileindica_ejecucion_detalle
    ADD CONSTRAINT fk680a09c7e760a455 FOREIGN KEY (investment_initiative_update_id) REFERENCES gis_investment_initiative_update(id);


--
-- Name: fk84d807a6724af9d7; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_style_gis_rule
    ADD CONSTRAINT fk84d807a6724af9d7 FOREIGN KEY (gis_style_id) REFERENCES gis_style(id);


--
-- Name: fk84d807a6896c93; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_style_gis_rule
    ADD CONSTRAINT fk84d807a6896c93 FOREIGN KEY (rulelist_id) REFERENCES gis_rule(id);


--
-- Name: fk85c0556b65e9f1; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_authority
    ADD CONSTRAINT fk85c0556b65e9f1 FOREIGN KEY (zone_id) REFERENCES gis_zone(id);


--
-- Name: fk85c055787637cb; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_authority
    ADD CONSTRAINT fk85c055787637cb FOREIGN KEY (auth_parent_id) REFERENCES gis_authority(id);


--
-- Name: fk85c055936375d5; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_authority
    ADD CONSTRAINT fk85c055936375d5 FOREIGN KEY (auth_type_id) REFERENCES gis_authority_type(id);


--
-- Name: fk86b5099ea0360851; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_investment_initiative_update
    ADD CONSTRAINT fk86b5099ea0360851 FOREIGN KEY (filetype_id) REFERENCES gis_file_type(id);


--
-- Name: fk9aec17517528183; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_rule_gis_rule_property
    ADD CONSTRAINT fk9aec17517528183 FOREIGN KEY (gis_rule_id) REFERENCES gis_rule(id);


--
-- Name: fk9aec1759e9b7c8f; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_rule_gis_rule_property
    ADD CONSTRAINT fk9aec1759e9b7c8f FOREIGN KEY (properties_id) REFERENCES gis_rule_property(id);


--
-- Name: fkae8ce763708863ac; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer
    ADD CONSTRAINT fkae8ce763708863ac FOREIGN KEY (layer_metadata_id) REFERENCES gis_layer_metadata(id);


--
-- Name: fkae8ce7637ca40ef6; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer
    ADD CONSTRAINT fkae8ce7637ca40ef6 FOREIGN KEY (layer_auth_id) REFERENCES gis_authority(id);


--
-- Name: fkae8ce763c9e1c2c3; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer
    ADD CONSTRAINT fkae8ce763c9e1c2c3 FOREIGN KEY (layer_folder_id) REFERENCES gis_folder(id);


--
-- Name: fkae8ce763cc2149e; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer
    ADD CONSTRAINT fkae8ce763cc2149e FOREIGN KEY (layer_layer_type_id) REFERENCES gis_layer_type(id);


--
-- Name: fkae8ce763db5bc963; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_layer
    ADD CONSTRAINT fkae8ce763db5bc963 FOREIGN KEY (layer_user_id) REFERENCES gis_user(id);


--
-- Name: fkcc38f638e760a455; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_proyectos_mideso
    ADD CONSTRAINT fkcc38f638e760a455 FOREIGN KEY (investment_initiative_update_id) REFERENCES gis_investment_initiative_update(id);


--
-- Name: fked1798061cb6f1e; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_property_in_layer_type
    ADD CONSTRAINT fked1798061cb6f1e FOREIGN KEY (gis_layer_type_id) REFERENCES gis_layer_type(id);


--
-- Name: fked179806a2d6b1f5; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_property_in_layer_type
    ADD CONSTRAINT fked179806a2d6b1f5 FOREIGN KEY (defaultproperties_id) REFERENCES gis_layer_type_property(id);


--
-- Name: fkf9e86082e760a455; Type: FK CONSTRAINT; Schema: ohiggins; Owner: ohiggins
--

ALTER TABLE ONLY gis_proyectos_georreferenciados_mideso
    ADD CONSTRAINT fkf9e86082e760a455 FOREIGN KEY (investment_initiative_update_id) REFERENCES gis_investment_initiative_update(id);


SET search_path = public, pg_catalog;

--
-- Name: fk89e44835d3498c4c; Type: FK CONSTRAINT; Schema: public; Owner: ohiggins
--

ALTER TABLE ONLY request_resources
    ADD CONSTRAINT fk89e44835d3498c4c FOREIGN KEY (request_id) REFERENCES request(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;




--
-- PostgreSQL database dump complete
--
