package net.hlinfo.pbp.pay.opt.wechat;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.hlinfo.opt.Jackson;

public class CertificateList {
	@JsonProperty("data")
    private List<CertificateItem> certs = new ArrayList<>();

	public List<CertificateItem> getCerts() {
		return certs;
	}

	public void setCerts(List<CertificateItem> certs) {
		this.certs = certs;
	}
	
	public String toString() {
		return Jackson.entityToString(this);
	}
}
