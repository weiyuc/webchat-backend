package com.ywzlp.webchat.msg.validator;

/**
 * Validator groups
 */
public interface ValidatorGroups {
	
	/**
	 * Validator group for user register 
	 */
	interface Register {}
	
	/**
	 * Validator group for user login 
	 */
	interface Login {}
	
	/**
	 * Validator group for add friend
	 */
	interface AddFriend {}
	
	/**
	 * Validator group for search friend
	 */
	interface SearchFriend {}
	
	/**
	 * Validator group for deal with friend reqeust
	 */
	interface DealFriendReq {}
	
	/**
	 * Validator group for set friend remark
	 *
	 */
	interface SetFriendRemark {}
	
	/**
	 * Validator group for set realName
	 *
	 */
	interface SetRealName {}
	
	/**
	 * Validator group for set whatUp
	 *
	 */
	interface SetWhatUp {}
	
	/**
	 * Validator group for set ProfilePhoto
	 *
	 */
	interface SetProfilePhoto {}
	
	/**
	 * Validator group for send message
	 *
	 */
	interface SendMessage {}
}
