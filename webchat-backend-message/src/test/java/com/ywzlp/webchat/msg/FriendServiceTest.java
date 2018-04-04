package com.ywzlp.webchat.msg;

import static com.ywzlp.webchat.msg.vo.FriendStatus.ACCEPT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.util.DBObjectUtils;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.ywzlp.webchat.msg.entity.FriendEntity;
import com.ywzlp.webchat.msg.repository.FriendRepository;
import com.ywzlp.webchat.msg.service.UserService;
import com.ywzlp.webchat.msg.util.ChineseUtil;
import com.ywzlp.webchat.msg.util.IndexComparator;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendServiceTest {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private FriendRepository friendRepository;
	
	@Test
	public void test1() {
		AggregationResults<FriendEntity> agg = mongoTemplate.aggregate(
				new TypedAggregation<FriendEntity>(
						FriendEntity.class, 
						LookupOperation.newLookup().from("user_info").localField("friendName").foreignField("username").as("friendInfo"),
						match(new Criteria("username").is("yuwei").and("status").is(ACCEPT.getStatus()))
						),
				FriendEntity.class);
		
		List<FriendEntity> fs = agg.getMappedResults();
		System.out.println(JSON.toJSONString(fs));
	}
	
	@Test
	public void getFriendList() {
		
//		mongoTemplate.aggregate(
//				new TypedAggregation<>(
//						FriendEntity.class,
//						new LookupOperation(from, localField, foreignField, as)),
//				inputType,
//				outputType)
		
		List<FriendEntity> friends = friendRepository.findByUsernameAndStatus(
				"yuwei",
				ACCEPT.getStatus()
				);
		
		Map<String, List<FriendEntity>> vo = new TreeMap<>(new IndexComparator());

		friends.forEach(f -> {
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
		System.out.println(JSON.toJSONString(vo));
	}

}
