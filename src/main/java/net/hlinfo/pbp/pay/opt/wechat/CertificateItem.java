package net.hlinfo.pbp.pay.opt.wechat;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CertificateItem {
	@JsonProperty("serial_no")
    private String serialNo;

    @JsonProperty("effective_time")
    private String effectiveTime;

    @JsonProperty("expire_time")
    private String expireTime;

    @JsonProperty("encrypt_certificate")
    private EncryptedCertificateItem encryptCertificate;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public EncryptedCertificateItem getEncryptCertificate() {
		return encryptCertificate;
	}

	public void setEncryptCertificate(EncryptedCertificateItem encryptCertificate) {
		this.encryptCertificate = encryptCertificate;
	}
    
}
