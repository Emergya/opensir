/* GeoJsonMapper.java
 * 
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto ohiggins
 * 
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 * 
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.web.util.geo;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.geolatte.common.Feature;
import org.geolatte.common.dataformats.json.jackson.AnyGeometrySerializer;
import org.geolatte.common.dataformats.json.jackson.FeatureSerializer;
import org.geolatte.common.dataformats.json.jackson.GeometryCollectionSerializer;
import org.geolatte.common.dataformats.json.jackson.JsonMapper;
import org.geolatte.common.dataformats.json.jackson.LineStringSerializer;
import org.geolatte.common.dataformats.json.jackson.MultiLineStringSerializer;
import org.geolatte.common.dataformats.json.jackson.MultiPointSerializer;
import org.geolatte.common.dataformats.json.jackson.MultiPolygonSerializer;
import org.geolatte.common.dataformats.json.jackson.PointSerializer;
import org.geolatte.common.dataformats.json.jackson.PolygonSerializer;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;

/**
 * @author jlrodriguez
 * 
 */
public class GeometryJsonMapper extends ObjectMapper {
	private JsonMapper mapper;

	public GeometryJsonMapper() {
		mapper = new JsonMapper();
		CustomSerializerFactory serializerFactory = new CustomSerializerFactory();

		serializerFactory.addGenericMapping(MultiLineString.class,
				new MultiLineStringSerializer(mapper));
		serializerFactory.addGenericMapping(LineString.class,
				new LineStringSerializer(mapper));
		serializerFactory.addGenericMapping(Point.class, new PointSerializer(
				mapper));
		serializerFactory.addGenericMapping(MultiPoint.class,
				new MultiPointSerializer(mapper));
		serializerFactory.addGenericMapping(Polygon.class,
				new PolygonSerializer(mapper));
		serializerFactory.addGenericMapping(Feature.class,
				new FeatureSerializer(mapper));
		serializerFactory.addGenericMapping(MultiPolygon.class,
				new MultiPolygonSerializer(mapper));
		serializerFactory.addGenericMapping(Geometry.class,
				new AnyGeometrySerializer(mapper));
		serializerFactory.addGenericMapping(GeometryCollection.class,
				new GeometryCollectionSerializer(mapper));

		this.setSerializerFactory(serializerFactory);

	}

}
