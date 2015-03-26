-- create permission sequence
CREATE SEQUENCE gis_permission_seq
    START WITH 30
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- update authority sequence
ALTER SEQUENCE gis_authority_seq restart with 6;