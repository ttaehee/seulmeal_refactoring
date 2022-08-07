package shop.seulmeal;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import shop.seulmeal.common.Search;
import shop.seulmeal.service.domain.Parts;
import shop.seulmeal.service.domain.Point;
import shop.seulmeal.service.domain.Purchase;
import shop.seulmeal.service.domain.User;
import shop.seulmeal.service.mapper.ProductMapper;
import shop.seulmeal.service.mapper.UserMapper;
import shop.seulmeal.service.user.UserService;



@SpringBootTest
public class UserTest {

	@Autowired
	private UserMapper userMapper;
	
	
	
	int pageUnit = 5;	
	int pageSize = 5;	
	
	//@Test
	void insertUser() throws Exception{
		User user = new User();
		user.setUserId("testUser1");
		user.setUserName("testName1");
		user.setPassword("testPassword1");
		//user.setBirth("00/01/01");
		user.setAddress("서울");
		user.setPhone("00031");
		user.setEmail("xcvbe1");
		user.setNickName("닉네임1");
		
		
		assertThat(userMapper.insertUser(user)).isEqualTo(1);
	}
	
	
	//@Test
	void insertHatesParts() throws Exception{
		
		List<Parts> parts = new ArrayList<Parts>();
		//parts.add( 1, "양파");
	
		
	}
	
	//@Test
	void getUser() throws Exception{
		User user = new User();
		
		user.setUserId("a");
		
		user=userMapper.getUser("a");
		System.out.println("getUser : "+user);
		
		assertEquals(user.getUserName(), "a");
		assertEquals(user.getPassword(), "1234");
		assertEquals(user.getAddress(), "서울");
		
	}
	
	//@Test
	void updateUser() throws Exception {
		
		User user =new User();
		user.setUserId("testUser");
		user.setUserName("testUser11");
		user.setPassword("1234");
		user.setPhone("0004");
		user.setEmail("bvcx");
		
		
		
		userMapper.updateUser(user);
		user=userMapper.getUser("testUser");
		System.out.println("updateUser : "+user);
		
		assertEquals(user.getUserId(), "testUser");
		
	}
	
	//@Test
	void deleteUser() throws Exception {
		User user = new User();
		user.setUserId("testUser");
		
		userMapper.deleteUser(user.getUserId());
		user=userMapper.getUser("testUser");
		System.out.println("deleteUser : "+user);
		
		assertEquals(user.getUserId(),  "testUser");
		
	}
	
	//@Test
	void getListUser() throws Exception {
		
		Search search = new Search();
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		search.setSearchKeyword("a");
		
		System.out.println(search);
		Map<String, Object> map = new HashMap<>();
		map.put("Search", search);
		map.put(null, map);
		
		userMapper.getListUser(search);
		System.out.println("search===>>"+search);
		System.out.println("map===>>"+map);
		
	}
	
	//@Test
	void insertPoint() throws Exception {
	
		
		Point point = new Point();
		point.setUserId("ghm4905");
		point.setPurchaseNo(2);
		point.setPointStatus("1");
		point.setPoint(6);
		
		assertThat(userMapper.insertPoint(point)).isEqualTo(1);
		
	}
	
	//@Test
	void getProfile() throws Exception {
		
		User user = new User();
		
		user.setUserId("a");
		
		user=userMapper.getUser("a");
		System.out.println("getUser : "+user);
		
		assertEquals(user.getNickName(), "s");
		assertEquals(user.getGrade(), "0");
		
	}
	
	//@Test
//	void getListPoint() throws Exception {
//		
//			
//		Search search = new Search();
//		if(search.getCurrentPage() ==0 ){
//			search.setCurrentPage(1);
//		}
//		search.setPageSize(pageSize);
//		System.out.println(search);
//		Map<String, Object> map = new HashMap<>();
//		
//		userMapper.getListPoint(search);
//	}
	
		
	/*
	 * List<Parts> list = new ArrayList<Parts>(); list.add(parts); list.add(parts);
	 * 
	 * //int a = productMapper.insertProudctParts(list);
	 */	
	
}
