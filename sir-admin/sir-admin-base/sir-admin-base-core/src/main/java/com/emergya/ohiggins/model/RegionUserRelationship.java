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
@Table(name = "gis_region_user")
@AssociationOverrides({
		@AssociationOverride(name = "ru.region", joinColumns = @JoinColumn(name = "id_region", referencedColumnName = "id", nullable = false, updatable = false)),
		@AssociationOverride(name = "ru.user", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id", nullable = false, updatable = false)) })
public class RegionUserRelationship implements Serializable {

	private static final long serialVersionUID = 2217179993607984421L;

	private RegionUserPk ru = new RegionUserPk();

	/**
	 * Default constructor required by Hibernate.
	 */
	public RegionUserRelationship() {
	}

	public RegionUserRelationship(RegionUserPk ru) {
		this.ru = ru;
	}

	@EmbeddedId
	private RegionUserPk getRu() {
		return ru;
	}

	@SuppressWarnings("unused")
	private void setRu(RegionUserPk ru) {
		this.ru = ru;
	}

	@Transient
	public RegionEntity getRegion() {
		return getRu().getRegion();
	}

	public void setRegion(RegionEntity region) {
		getRu().setRegion(region);
	}

	@Transient
	public UserEntity getUser() {
		return getRu().getUser();
	}

	public void setUser(UserEntity user) {
		getRu().setUser(user);
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		RegionUserRelationship that = (RegionUserRelationship) o;

		if (getRu() != null ? !getRu().equals(that.getRu())
				: that.getRu() != null)
			return false;

		return true;
	}

	public int hashCode() {
		return (getRu() != null ? getRu().hashCode() : 0);
	}

	@Embeddable
	public static class RegionUserPk implements Serializable {

		private static final long serialVersionUID = 637442249569816282L;

		private RegionEntity region;
		private UserEntity user;

		public RegionUserPk() {
		}

		public RegionUserPk(RegionEntity region, UserEntity user) {
			super();
			this.region = region;
			this.user = user;
		}

		@ManyToOne
		public RegionEntity getRegion() {
			return region;
		}

		public void setRegion(RegionEntity region) {
			this.region = region;
		}

		@ManyToOne
		public UserEntity getUser() {
			return user;
		}

		public void setUser(UserEntity user) {
			this.user = user;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			RegionUserPk that = (RegionUserPk) o;

			if (region != null ? !region.equals(that.region)
					: that.region != null)
				return false;
			if (user != null ? !user.equals(that.user) : that.user != null)
				return false;

			return true;
		}

		public int hashCode() {
			int result;
			result = (region != null ? region.hashCode() : 0);
			result = 31 * result + (user != null ? user.hashCode() : 0);
			return result;
		}
	}
}
