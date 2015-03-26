package com.emergya.ohiggins.model;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "gis_authority_region")
@AssociationOverrides({
		@AssociationOverride(name = "ra.region", joinColumns = @JoinColumn(name = "id_region", referencedColumnName = "id", nullable = false, updatable = false)),
		@AssociationOverride(name = "ra.authority", joinColumns = @JoinColumn(name = "id_authority", referencedColumnName = "id", nullable = false, updatable = false)) })
public class RegionAuthorityRelationship implements Serializable {

	private static final long serialVersionUID = 2052093035865891794L;

	private RegionAuthorityPk ra = new RegionAuthorityPk();

	/**
	 * Default constructor required by Hibernate.
	 */
	public RegionAuthorityRelationship() {
	}

	public RegionAuthorityRelationship(RegionAuthorityPk ra) {
		this.ra = ra;
	}

	@EmbeddedId
	private RegionAuthorityPk getRa() {
		return ra;
	}

	@SuppressWarnings("unused")
	private void setRa(RegionAuthorityPk ra) {
		this.ra = ra;
	}

	@Transient
	public RegionEntity getRegion() {
		return getRa().getRegion();
	}

	public void setRegion(RegionEntity region) {
		getRa().setRegion(region);
	}

	@Transient
	public AuthorityEntity getAuthority() {
		return getRa().getAuthority();
	}

	public void setAuthority(AuthorityEntity authority) {
		getRa().setAuthority(authority);
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		RegionAuthorityRelationship that = (RegionAuthorityRelationship) o;

		if (getRa() != null ? !getRa().equals(that.getRa())
				: that.getRa() != null)
			return false;

		return true;
	}

	public int hashCode() {
		return (getRa() != null ? getRa().hashCode() : 0);
	}

	@Embeddable
	public static class RegionAuthorityPk implements Serializable {

		private static final long serialVersionUID = 1396966555897739034L;

		private RegionEntity region;
		private AuthorityEntity authority;

		public RegionAuthorityPk() {
		}

		public RegionAuthorityPk(RegionEntity region, AuthorityEntity authority) {
			super();
			this.region = region;
			this.authority = authority;
		}

		@ManyToOne
		public RegionEntity getRegion() {
			return region;
		}

		public void setRegion(RegionEntity region) {
			this.region = region;
		}

		@ManyToOne
		public AuthorityEntity getAuthority() {
			return authority;
		}

		public void setAuthority(AuthorityEntity authority) {
			this.authority = authority;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			RegionAuthorityPk that = (RegionAuthorityPk) o;

			if (region != null ? !region.equals(that.region)
					: that.region != null)
				return false;
			if (authority != null ? !authority.equals(that.authority)
					: that.authority != null)
				return false;

			return true;
		}

		public int hashCode() {
			int result;
			result = (region != null ? region.hashCode() : 0);
			result = 31 * result
					+ (authority != null ? authority.hashCode() : 0);
			return result;
		}
	}
}
