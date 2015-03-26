--
-- PostgreSQL database dump
--
SET search_path = ohiggins, public;

CREATE TABLE ohiggins.regiones_d7d3eb18_0c5c_4164_959a_8d55b6863f81 ( 
	gid       	int4 NOT NULL,
	NOM_REG   	varchar(50) NULL,
	NOM_PROV  	varchar(20) NULL,
	COD_COM   	varchar(5) NULL,
	NOM_COM   	varchar(30) NULL,
	COD_REGI  	numeric NULL,
	SUPERFICIE	numeric NULL,
	POBLAC02  	int4 NULL,
	POBL2010  	int4 NULL,
	SHAPE_Leng	numeric NULL,
	SHAPE_Area	numeric NULL,
	geom      	geometry NULL 
	);
