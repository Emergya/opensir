
-- 
-- get_user_roles(int) function
-- 
CREATE OR REPLACE FUNCTION get_user_roles(user_id int) 
RETURNS SETOF text AS $$
BEGIN
    return query 
	EXECUTE 'select ''ROLE_'' || upper(name)
	from gis_user, gis_authority as auth
	where gis_user.user_authority_id = auth.id 
		and gis_user.id= $1 
	union (select ''ROLE_'' || ''ADMIN''
		from gis_user 
			where admin = true and id = $1)
	union (select ''ROLE_'' || ''USER''
		from gis_user 
			where admin = false and id = $1)
	union (select ''PERMISSION_'' || id
		from gis_permission 
		where exists (select * from gis_permission_by_authtype 
			where gis_permission_by_authtype.auth_type_id in 
				(select auth. auth_type_id
					from gis_user, gis_authority as auth
					where gis_user.user_authority_id = auth.id 
						and gis_user.id=$1 )));'
     USING user_id; 
END;
$$ LANGUAGE plpgsql;

-- 
-- get_user_roles_by_username(text) function
-- 
CREATE OR REPLACE FUNCTION get_user_roles_by_username(username text) 
RETURNS SETOF text AS $$
BEGIN
    return query 
	EXECUTE 'select ''ROLE_'' || upper(name)
	from gis_user, gis_authority as auth
	where gis_user.user_authority_id = auth.id 
		and gis_user.username= $1 
	union (select ''ROLE_'' || ''ADMIN''
		from gis_user 
			where admin = true and username = $1)
	union (select ''ROLE_'' || ''USER''
		from gis_user 
			where admin = false and username = $1)
	union (select ''PERMISSION_'' || id
		from gis_permission 
		where exists (select * from gis_permission_by_authtype 
			where gis_permission_by_authtype.auth_type_id in 
				(select auth. auth_type_id
					from gis_user, gis_authority as auth
					where gis_user.user_authority_id = auth.id 
						and gis_user.username=$1 )));'
     USING username; 
END;
$$ LANGUAGE plpgsql;