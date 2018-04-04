package com.ywzlp.webchat.msg.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "friend_info")
public class FriendEntity {
	
	@Id
	private String friendId;
	
	@Field("username")
	private String username;
	
	@Field("friendName")
	private String friendName;
	
	@Field("remark")
	private String remark;
	
	@Field("createTime")
	private Long createTime;
	
	@Field("status")
	private Integer status;
	
	@DBRef(db = "user_info")
	private UserEntity friendInfo;
	
	@Transient
	private String fullSpell;
	
	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFullSpell() {
		return fullSpell;
	}

	public void setFullSpell(String fullSpell) {
		this.fullSpell = fullSpell;
	}

	public UserEntity getFriendInfo() {
		return friendInfo;
	}

	public void setFriendInfo(UserEntity friendInfo) {
		this.friendInfo = friendInfo;
	}

}
