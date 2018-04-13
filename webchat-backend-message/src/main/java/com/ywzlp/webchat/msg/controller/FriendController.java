package com.ywzlp.webchat.msg.controller;

import static com.ywzlp.webchat.msg.vo.FriendStatus.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ywzlp.webchat.msg.dto.FriendDto;
import com.ywzlp.webchat.msg.dto.MessageType;
import com.ywzlp.webchat.msg.dto.WebChatMessage;
import com.ywzlp.webchat.msg.entity.FriendEntity;
import com.ywzlp.webchat.msg.service.FriendService;
import com.ywzlp.webchat.msg.service.UserService;
import com.ywzlp.webchat.msg.util.ChineseUtil;
import com.ywzlp.webchat.msg.validator.ValidatorGroups;
import com.ywzlp.webchat.msg.vo.WebChatResponse;

@RestController
@RequestMapping("/friend")
public class FriendController {
	
	private final static Logger logger = LoggerFactory.getLogger(FriendController.class);

	@Autowired
	private FriendService friendService;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@PostMapping("/getFriendList")
	public WebChatResponse<?> getFriendList() {
		Map<String, List<FriendEntity>> friends = friendService.getFriendList();
		return WebChatResponse.success(friends);
	}
	
	@PostMapping("/getFriendReq")
	public WebChatResponse<?> getFriendReq() {
		List<FriendEntity> friends = friendService.getFriendReq();
		if (CollectionUtils.isEmpty(friends)) {
			return WebChatResponse.success(friends);
		}
		List<String> res = friends.stream().map(f ->  f.getFriendName()).collect(Collectors.toList());
		return WebChatResponse.success(res);
	}
	
	@PostMapping("/addFriend")
	public WebChatResponse<?> addFriend(@RequestBody @Validated(ValidatorGroups.AddFriend.class) FriendDto dto) {
		friendService.addFriend(dto);
		return WebChatResponse.success();
	}
	
	@PostMapping("/searchFriend")
	public WebChatResponse<FriendEntity> searchFriend(@RequestBody @Validated(ValidatorGroups.SearchFriend.class) FriendDto dto) {
		FriendEntity friend = friendService.searchFriend(dto);
		return WebChatResponse.success(friend);
	}
	
	@PostMapping("/dealFriendReq")
	public WebChatResponse<?> dealFriendReq(@RequestBody @Validated(ValidatorGroups.DealFriendReq.class) FriendDto dto) {
		friendService.dealRequest(dto);
		if (ACCEPT.getStatus().equals(dto.getStatus())) {
			WebChatMessage msg = new WebChatMessage();
			msg.setMessageType(MessageType.ACCEPTED_FRIEND_REQ);
			msg.setContent(ChineseUtil.getFirstAndToUpcase(dto.getFriendName()));
			msg.setFrom(UserService.getCurrentUsername());
			msg.setTo(dto.getFriendName());
			logger.info("Message: {}", JSON.toJSONString(msg));
			template.convertAndSend("/message/" + msg.getTo(), msg);
		}
		
		if (DELETE.getStatus().equals(dto.getStatus())) {
			WebChatMessage msg = new WebChatMessage();
			msg.setMessageType(MessageType.DELETE_FRIEND);
			if (!StringUtils.isEmpty(dto.getRemark())) {
				msg.setContent(ChineseUtil.getFirstAndToUpcase(dto.getRemark()));
			} else {
				msg.setContent(ChineseUtil.getFirstAndToUpcase(dto.getFriendName()));
			}
			msg.setFrom(UserService.getCurrentUsername());
			msg.setTo(dto.getFriendName());
			logger.info("Message: {}", JSON.toJSONString(msg));
			template.convertAndSend("/message/" + msg.getTo(), msg);
		}
		
		return WebChatResponse.success();
	}
	
	@PostMapping("/setRemark")
	public WebChatResponse<?> setRemark(@RequestBody @Validated(ValidatorGroups.SetFriendRemark.class) FriendDto dto) {
		friendService.setRemark(dto);
		return WebChatResponse.success();
	}

}
