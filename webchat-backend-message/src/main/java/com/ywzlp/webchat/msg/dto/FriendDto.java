package com.ywzlp.webchat.msg.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.ywzlp.webchat.msg.validator.ValidatorGroups;

public class FriendDto extends AbstractDto {
	
	@NotBlank(
			groups = {
					ValidatorGroups.AddFriend.class,
					ValidatorGroups.SearchFriend.class,
					ValidatorGroups.DealFriendReq.class,
					ValidatorGroups.SetFriendRemark.class
					},
			message = "friend name can not be null")
	private String friendName;
	
	@NotNull(groups = {ValidatorGroups.DealFriendReq.class}, message = "status can not be null")
	private Integer status;
	
	@Length(groups = { ValidatorGroups.AddFriend.class,ValidatorGroups.SetFriendRemark.class }, max = 50, message = "remark too large")
	private String remark;
	
	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setReMark(String remark) {
		this.remark = remark;
	}

}
