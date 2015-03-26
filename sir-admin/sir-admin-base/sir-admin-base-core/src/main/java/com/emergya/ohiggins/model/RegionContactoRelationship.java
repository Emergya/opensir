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

import com.emergya.ohiggins.model.RegionAuthorityRelationship.RegionAuthorityPk;

@Entity
@Table(name = "gis_region_contacto")
@AssociationOverrides({
		@AssociationOverride(name = "rc.region", joinColumns = @JoinColumn(name = "id_region", referencedColumnName = "id", nullable = false, updatable = false)),
		@AssociationOverride(name = "rc.contacto", joinColumns = @JoinColumn(name = "id_contacto", referencedColumnName = "id", nullable = false, updatable = false)) })
public class RegionContactoRelationship implements Serializable {

	private static final long serialVersionUID = 5270123033820947037L;

	private RegionContactoPk rc = new RegionContactoPk();

	/**
	 * Default constructor required by Hibernate.
	 */
	public RegionContactoRelationship() {
	}

	public RegionContactoRelationship(RegionContactoPk rc) {
		this.rc = rc;
	}

	@EmbeddedId
	private RegionContactoPk getRc() {
		return rc;
	}

	@SuppressWarnings("unused")
	private void setRc(RegionContactoPk rc) {
		this.rc = rc;
	}

	@Transient
	public RegionEntity getRegion() {
		return getRc().getRegion();
	}

	public void setRegion(RegionEntity region) {
		getRc().setRegion(region);
	}

	@Transient
	public ContactoEntity getContacto() {
		return getRc().getContacto();
	}

	public void setContacto(ContactoEntity contacto) {
		getRc().setContacto(contacto);
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		RegionContactoRelationship that = (RegionContactoRelationship) o;

		if (getRc() != null ? !getRc().equals(that.getRc())
				: that.getRc() != null)
			return false;

		return true;
	}

	public int hashCode() {
		return (getRc() != null ? getRc().hashCode() : 0);
	}

	@Embeddable
	public static class RegionContactoPk implements Serializable {

		private static final long serialVersionUID = -4453817049814575631L;

		private RegionEntity region;
		private ContactoEntity contacto;

		public RegionContactoPk() {
		}

		public RegionContactoPk(RegionEntity region, ContactoEntity contacto) {
			super();
			this.region = region;
			this.contacto = contacto;
		}

		@ManyToOne
		public RegionEntity getRegion() {
			return region;
		}

		public void setRegion(RegionEntity region) {
			this.region = region;
		}

		@ManyToOne
		public ContactoEntity getContacto() {
			return contacto;
		}

		public void setContacto(ContactoEntity contacto) {
			this.contacto = contacto;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			RegionContactoPk that = (RegionContactoPk) o;

			if (region != null ? !region.equals(that.region)
					: that.region != null)
				return false;
			if (contacto != null ? !contacto.equals(that.contacto)
					: that.contacto != null)
				return false;

			return true;
		}

		public int hashCode() {
			int result;
			result = (region != null ? region.hashCode() : 0);
			result = 31 * result + (contacto != null ? contacto.hashCode() : 0);
			return result;
		}
	}
}
