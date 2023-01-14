package net.hlinfo.pbp.pay.opt.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EncryptedCertificateItem {
	@JsonProperty("algorithm")
    private String algorithm;

    @JsonProperty("nonce")
    private String nonce;

    @JsonProperty("associated_data")
    private String associatedData;

    @JsonProperty("ciphertext")
    private String ciphertext;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getAssociatedData() {
		return associatedData;
	}

	public void setAssociatedData(String associatedData) {
		this.associatedData = associatedData;
	}

	public String getCiphertext() {
		return ciphertext;
	}

	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}
}
