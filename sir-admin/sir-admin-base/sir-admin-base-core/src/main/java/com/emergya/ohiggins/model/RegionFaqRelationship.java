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
@Table(name = "gis_region_faq")
@AssociationOverrides({
		@AssociationOverride(name = "rf.region", joinColumns = @JoinColumn(name = "id_region", referencedColumnName = "id", nullable = false, updatable = false)),
		@AssociationOverride(name = "rf.faq", joinColumns = @JoinColumn(name = "id_faq", referencedColumnName = "id", nullable = false, updatable = false)) })
public class RegionFaqRelationship implements Serializable {

	private static final long serialVersionUID = 5270123033820947037L;

	private RegionFaqPk rf = new RegionFaqPk();

	/**
	 * Default constructor required by Hibernate.
	 */
	public RegionFaqRelationship() {
	}

	public RegionFaqRelationship(RegionFaqPk rf) {
		this.rf = rf;
	}

	@EmbeddedId
	private RegionFaqPk getRf() {
		return rf;
	}

	@SuppressWarnings("unused")
	private void setRf(RegionFaqPk rf) {
		this.rf = rf;
	}

	@Transient
	public RegionEntity getRegion() {
		return getRf().getRegion();
	}

	public void setRegion(RegionEntity region) {
		getRf().setRegion(region);
	}

	@Transient
	public FaqEntity getFaq() {
		return getRf().getFaq();
	}

	public void setFaq(FaqEntity faq) {
		getRf().setFaq(faq);
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		RegionFaqRelationship that = (RegionFaqRelationship) o;

		if (getRf() != null ? !getRf().equals(that.getRf())
				: that.getRf() != null)
			return false;

		return true;
	}

	public int hashCode() {
		return (getRf() != null ? getRf().hashCode() : 0);
	}

	@Embeddable
	public static class RegionFaqPk implements Serializable {

		private static final long serialVersionUID = -4453817049814575631L;

		private RegionEntity region;
		private FaqEntity faq;

		public RegionFaqPk() {
		}

		public RegionFaqPk(RegionEntity region, FaqEntity faq) {
			super();
			this.region = region;
			this.faq = faq;
		}

		@ManyToOne
		public RegionEntity getRegion() {
			return region;
		}

		public void setRegion(RegionEntity region) {
			this.region = region;
		}

		@ManyToOne
		public FaqEntity getFaq() {
			return faq;
		}

		public void setFaq(FaqEntity faq) {
			this.faq = faq;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			RegionFaqPk that = (RegionFaqPk) o;

			if (region != null ? !region.equals(that.region)
					: that.region != null)
				return false;
			if (faq != null ? !faq.equals(that.faq) : that.faq != null)
				return false;

			return true;
		}

		public int hashCode() {
			int result;
			result = (region != null ? region.hashCode() : 0);
			result = 31 * result + (faq != null ? faq.hashCode() : 0);
			return result;
		}
	}
}
