--
-- PostgreSQL database dump
--
SET search_path = ohiggins, public;

DELETE FROM  ohiggins.gis_user where id in (2, 3, 4, 5, 6, 7);
DELETE FROM  ohiggins.gis_authority;

COMMIT;
