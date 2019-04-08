package com.ywzlp.webchat.msg.service;

import static com.ywzlp.webchat.msg.vo.FriendStatus.ACCEPT;
import static com.ywzlp.webchat.msg.vo.FriendStatus.RECEIVE;
import static com.ywzlp.webchat.msg.vo.FriendStatus.REQUEST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ywzlp.webchat.msg.dto.FriendDto;
import com.ywzlp.webchat.msg.dto.MessageType;
import com.ywzlp.webchat.msg.dto.WebChatMessage;
import com.ywzlp.webchat.msg.entity.FriendEntity;
import com.ywzlp.webchat.msg.entity.UserEntity;
import com.ywzlp.webchat.msg.repository.FriendRepository;
import com.ywzlp.webchat.msg.repository.UserRepository;
import com.ywzlp.webchat.msg.util.ChineseUtil;
import com.ywzlp.webchat.msg.util.IndexComparator;
import com.ywzlp.webchat.msg.vo.FriendStatus;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * Get friend List
	 * 
	 * @return For example:
	 * 
	 *         <pre>
	 * {
	 *  "a": [
	 * 	  {
	 * 		"friendName": "abc",
	 * 	  },
	 * 	  {
	 * 		"friendName": "abb",
	 * 	  },
	 * 	],
	 * "b": [
	 *    {
	 * 	    "friendName": "bcc",
	 * 	  }, {
	 * 		"friendName": "bba",
	 * 	  }
	 * 	]
	 * }
	 *         </pre>
	 * 
	 */
	public Map<String, List<FriendEntity>> getFriendList() {

		AggregationResults<FriendEntity> aggregationResults = mongoTemplate.aggregate(new TypedAggregation<FriendEntity>(
						FriendEntity.class,
						LookupOperation.newLookup().from("user_info").localField("friendName").foreignField("username")
								.as("friendInfo"),
								Aggregation.match(new Criteria("username").is(UserService.getCurrentUsername()).and("status")
								.is(ACCEPT.getStatus()))
						),
						FriendEntity.class);
		
		List<FriendEntity> friends = aggregationResults.getMappedResults();
		
//		List<FriendEntity> friends = friendRepository.findByUsernameAndStatus(
// 				UserService.getCurrentUsername(),
// 				ACCEPT.getStatus()
// 				);

		Map<String, List<FriendEntity>> vo = new TreeMap<>(new IndexComparator());

		friends.forEach(f -> {
			if (f.getFriendInfo() != null) {
				f.getFriendInfo().setPassword(null);
			}
			String sortBy = (f.getRemark() == null ? f.getFriendName() : f.getRemark());
			String fullSpell = ChineseUtil.getFullSpell(sortBy);
			f.setFullSpell(fullSpell);

			String index = ChineseUtil.getSortIndex(fullSpell);
			List<FriendEntity> l = vo.get(index);

			if (l != null) {
				l.add(f);
			} else {
				l = new ArrayList<>();
				l.add(f);
				vo.put(index, l);
			}
		});

		vo.forEach((k, v) -> {
			Collections.sort(v, (v1, v2) -> {
				return v1.getFullSpell().compareTo(v2.getFullSpell());
			});
		});
		return vo;
	}

	public boolean addFriend(FriendDto dto) {
		String currentUsername = UserService.getCurrentUsername();
		if (dto.getFriendName().equals(currentUsername)) {
			return false;
		}
		FriendEntity friend = friendRepository.findByUsernameAndFriendName(currentUsername, dto.getFriendName());
		if (friend != null) {
			return true;
		}

		FriendEntity request = dto.toEntity(FriendEntity.class);
		request.setUsername(UserService.getCurrentUsername());
		request.setStatus(REQUEST.getStatus());

		FriendEntity receive = new FriendEntity();
		receive.setUsername(request.getFriendName());
		receive.setFriendName(request.getUsername());
		receive.setStatus(RECEIVE.getStatus());

		// Save to db
		friendRepository.saveAll(Arrays.asList(request, receive));

		// Notify user
		this.notifyAddFriend(currentUsername, dto.getFriendName());

		return true;
	}

	private void notifyAddFriend(String fromUsername, String toUsername) {
		WebChatMessage msg = new WebChatMessage();
		msg.setFrom(fromUsername);
		msg.setTo(toUsername);
		msg.setMessageType(MessageType.ADD_FRIEND);
		template.convertAndSend("/message/" + toUsername, msg);
	}

	public List<FriendEntity> getRequestList() {
		List<FriendEntity> requestList = friendRepository.findByUsernameAndStatus(UserService.getCurrentUsername(),
				REQUEST.getStatus());
		return requestList;
	}

	public List<FriendEntity> getReceiveList() {
		List<FriendEntity> receiveList = friendRepository.findByUsernameAndStatus(UserService.getCurrentUsername(),
				RECEIVE.getStatus());
		return receiveList;
	}

	public void setRemark(FriendDto dto) {
		FriendEntity friend = friendRepository.findByUsernameAndFriendName(UserService.getCurrentUsername(),
				dto.getFriendName());
		if (friend == null) {
			return;
		}
		friend.setRemark(dto.getRemark());
		friendRepository.save(friend);
	}

	public void dealRequest(FriendDto dto) {
		FriendStatus status = FriendStatus.valueOf(dto.getStatus());
		String currentUsername = UserService.getCurrentUsername();

		FriendEntity friend = friendRepository.findByUsernameAndFriendName(currentUsername, dto.getFriendName());

		if (friend == null) {
			return;
		}

		switch (status) {
		case ACCEPT: {
			friend.setStatus(ACCEPT.getStatus());
			FriendEntity request = friendRepository.findByUsernameAndFriendName(dto.getFriendName(), currentUsername);
			request.setStatus(ACCEPT.getStatus());
			friendRepository.saveAll(Arrays.asList(friend, request));
			break;
		}
		case REFUSE: {
			FriendEntity request = friendRepository.findByUsernameAndFriendName(dto.getFriendName(), currentUsername);
			friendRepository.deleteAll(Arrays.asList(friend, request));
			break;
		}
		case DELETE: {
			FriendEntity request = friendRepository.findByUsernameAndFriendName(dto.getFriendName(), currentUsername);
			friendRepository.deleteAll(Arrays.asList(friend, request));
			break;
		}
		// case BLACK_LIST:
		// break;
		default:
			throw new IllegalArgumentException("Unknow FriendStatus type of " + status.toString());
		}
	}

	public FriendEntity searchFriend(FriendDto dto) {
		String currentUsername = UserService.getCurrentUsername();
		if (dto.getFriendName().equals(currentUsername)) {
			return null;
		}
		UserEntity user = userRepository.findByUsername(dto.getFriendName());
		if (user == null) {
			return null;
		}
		FriendEntity friend = friendRepository.findByUsernameAndFriendName(currentUsername, dto.getFriendName());
		if (friend != null) {
			return friend;
		}
		friend = new FriendEntity();
		friend.setUsername(user.getUsername());
		return friend;
	}

	public List<FriendEntity> getFriendReq() {
		return friendRepository.findByUsernameAndStatus(UserService.getCurrentUsername(),
				FriendStatus.RECEIVE.getStatus());
	}

}
