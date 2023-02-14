package net.hlinfo.pbp.pay.opt.apple;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.Jackson;

/**
 * 苹果登录用户信息
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppleLoginUserInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("错误信息,正常是：'getUserInfo:ok'")
	private String errMsg;
	
	@ApiModelProperty("用户信息")
	@NotNull(message = "用户信息不能为空")
	@Valid
	private UserInfo userInfo;
	
	public AppleLoginUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AppleLoginUserInfo(String errMsg, @NotNull(message = "用户信息不能为空") UserInfo userInfo) {
		super();
		this.errMsg = errMsg;
		this.userInfo = userInfo;
	}
	/**
	 *  Getter method for property <b>errMsg</b>.
	 * @return property value of errMsg
	 */
	public String getErrMsg() {
		return errMsg;
	}
	/**
	 * Setter method for property <b>errMsg</b>.
	 *
	 * @param errMsg value to be assigned to property errMsg
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	/**
	 *  Getter method for property <b>userInfo</b>.
	 * @return property value of userInfo
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}
	/**
	 * Setter method for property <b>userInfo</b>.
	 *
	 * @param userInfo value to be assigned to property userInfo
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	public class UserInfo{
		@ApiModelProperty("ios用户唯一标识符")
		@NotNull(message = "openId不能为空")
		private String openId;
		
		@ApiModelProperty("用户共享的可选电子邮件")
		private String email;
		
		@ApiModelProperty("授权验证code")
		@NotNull(message = "authorizationCode不能为空")
		private String authorizationCode;
		
		@ApiModelProperty("Web令牌(JWT)")
		@NotNull(message = "identityToken不能为空")
		private String identityToken;
		
		@ApiModelProperty("标识用户是否为真实的人 0：当前平台不支持，忽略该值；1：无法确认；2：用户真实性非常高")
		private String realUserStatus;
		
		@ApiModelProperty("姓名信息")
		private FullName fullName;

		public UserInfo() {
			super();
			// TODO Auto-generated constructor stub
		}

		public UserInfo(@NotNull(message = "openId不能为空") String openId, String email,
				@NotNull(message = "authorizationCode不能为空") String authorizationCode,
				@NotNull(message = "identityToken不能为空") String identityToken, String realUserStatus,
				FullName fullName) {
			super();
			this.openId = openId;
			this.email = email;
			this.authorizationCode = authorizationCode;
			this.identityToken = identityToken;
			this.realUserStatus = realUserStatus;
			this.fullName = fullName;
		}

		/**
		 *  Getter method for property <b>openId</b>.
		 * @return property value of openId
		 */
		public String getOpenId() {
			return openId;
		}

		/**
		 * Setter method for property <b>openId</b>.
		 *
		 * @param openId value to be assigned to property openId
		 */
		public void setOpenId(String openId) {
			this.openId = openId;
		}

		/**
		 *  Getter method for property <b>email</b>.
		 * @return property value of email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * Setter method for property <b>email</b>.
		 *
		 * @param email value to be assigned to property email
		 */
		public void setEmail(String email) {
			this.email = email;
		}

		/**
		 *  Getter method for property <b>authorizationCode</b>.
		 * @return property value of authorizationCode
		 */
		public String getAuthorizationCode() {
			return authorizationCode;
		}

		/**
		 * Setter method for property <b>authorizationCode</b>.
		 *
		 * @param authorizationCode value to be assigned to property authorizationCode
		 */
		public void setAuthorizationCode(String authorizationCode) {
			this.authorizationCode = authorizationCode;
		}

		/**
		 *  Getter method for property <b>identityToken</b>.
		 * @return property value of identityToken
		 */
		public String getIdentityToken() {
			return identityToken;
		}

		/**
		 * Setter method for property <b>identityToken</b>.
		 *
		 * @param identityToken value to be assigned to property identityToken
		 */
		public void setIdentityToken(String identityToken) {
			this.identityToken = identityToken;
		}

		/**
		 *  Getter method for property <b>realUserStatus</b>.
		 * @return property value of realUserStatus
		 */
		public String getRealUserStatus() {
			return realUserStatus;
		}

		/**
		 * Setter method for property <b>realUserStatus</b>.
		 *
		 * @param realUserStatus value to be assigned to property realUserStatus
		 */
		public void setRealUserStatus(String realUserStatus) {
			this.realUserStatus = realUserStatus;
		}

		/**
		 *  Getter method for property <b>fullName</b>.
		 * @return property value of fullName
		 */
		public FullName getFullName() {
			return fullName;
		}

		/**
		 * Setter method for property <b>fullName</b>.
		 *
		 * @param fullName value to be assigned to property fullName
		 */
		public void setFullName(FullName fullName) {
			this.fullName = fullName;
		}
		@Override
		public String toString() {
			return Jackson.entityToString(this);
		}
		/**
		 * 姓名信息
		 *
		 */
		public class FullName implements Serializable{
			private static final long serialVersionUID = 1L;
			
			@ApiModelProperty("姓")
			private String familyName;
			
			@ApiModelProperty("名")
			private String giveName;
			
			@ApiModelProperty("名字")
			private String givenName;
			
			@ApiModelProperty("昵称")
			private String nickName;
			
			public FullName() {
				super();
			}
			
			public FullName(String familyName, String giveName, String givenName, String nickName) {
				super();
				this.familyName = familyName;
				this.giveName = giveName;
				this.givenName = givenName;
				this.nickName = nickName;
			}
			
			/**
			 *  Getter method for property <b>givenName</b>.
			 * @return property value of givenName
			 */
			public String getGivenName() {
				return givenName;
			}
			
			/**
			 * Setter method for property <b>givenName</b>.
			 *
			 * @param givenName value to be assigned to property givenName
			 */
			public void setGivenName(String givenName) {
				this.givenName = givenName;
			}
			
			/**
			 *  Getter method for property <b>familyName</b>.
			 * @return property value of familyName
			 */
			public String getFamilyName() {
				return familyName;
			}
			
			/**
			 * Setter method for property <b>familyName</b>.
			 *
			 * @param familyName value to be assigned to property familyName
			 */
			public void setFamilyName(String familyName) {
				this.familyName = familyName;
			}
			
			/**
			 *  Getter method for property <b>giveName</b>.
			 * @return property value of giveName
			 */
			public String getGiveName() {
				return giveName;
			}
			
			/**
			 * Setter method for property <b>giveName</b>.
			 *
			 * @param giveName value to be assigned to property giveName
			 */
			public void setGiveName(String giveName) {
				this.giveName = giveName;
			}
			
			/**
			 *  Getter method for property <b>nickName</b>.
			 * @return property value of nickName
			 */
			public String getNickName() {
				return nickName;
			}
			
			/**
			 * Setter method for property <b>nickName</b>.
			 *
			 * @param nickName value to be assigned to property nickName
			 */
			public void setNickName(String nickName) {
				this.nickName = nickName;
			}
			@Override
			public String toString() {
				return Jackson.entityToString(this);
			}
		}
	}
	@Override
	public String toString() {
		return Jackson.entityToString(this);
	}
	
	
}
