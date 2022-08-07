package shop.seulmeal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import shop.seulmeal.common.Page;
import shop.seulmeal.common.Search;
import shop.seulmeal.service.domain.Attachments;
import shop.seulmeal.service.domain.Comment;
import shop.seulmeal.service.domain.CustomParts;
import shop.seulmeal.service.domain.CustomProduct;
import shop.seulmeal.service.domain.Foodcategory;
import shop.seulmeal.service.domain.Parts;
import shop.seulmeal.service.domain.Post;
import shop.seulmeal.service.domain.Product;
import shop.seulmeal.service.domain.Relation;
import shop.seulmeal.service.domain.User;
import shop.seulmeal.service.mapper.AttachmentsMapper;
import shop.seulmeal.service.mapper.CommunityMapper;
import shop.seulmeal.service.mapper.OperationMapper;
import shop.seulmeal.service.mapper.ProductMapper;
import shop.seulmeal.service.mapper.UserMapper;
import shop.seulmeal.service.naver.impl.EmailService;
import shop.seulmeal.service.naver.impl.NaverServiceImpl;
import shop.seulmeal.service.product.ProductService;

@SpringBootTest
class OperationTest {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private OperationMapper operationMapper;
	
	@Autowired
	private AttachmentsMapper attachmentsMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CommunityMapper communityMapper;
	
	@Autowired
	private NaverServiceImpl naverServiceImpl;
	
	@Autowired
	private EmailService emailserivce;
	
	int pageUnit = 5;	
	int pageSize = 5;
	
	@Value("${java.file.test}")
	String envValue;
	
	@Value("${chatBot.secretKey}")
	String api;
	
	@Test
	void monthAdmin() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("table", "post");
		map.put("option", "post");
		List<Map<String,Object>> list = operationMapper.monthChart(map);
		
		for (Map<String, Object> map2 : list) {
			System.out.println(map2);
		}
		
		map.put("table", "post p,comments c");
		map.put("option", "commentCount");
		list = operationMapper.monthChart(map);
		for (Map<String, Object> map2 : list) {
			System.out.println(map2);
		}
	}
	
	//@Test
	void adminCount() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("table", "post");
		map.put("post_status", 0);
		//map.put("option", "price");
		List<Map<String,Object>> list = operationMapper.countAdminDay(map);
		
		for (Map<String, Object> map2 : list) {
			System.out.println(map2);
		}
	}
	
	//@Test
	void mainProduct() {
		User user = new User();
		Map<String,Object> map = new HashMap<>();
		user.setFoodCategoryName1("한식");
		map.put("user", user);
		List<Map<String,Object>> list = operationMapper.selectUserProduct(map);
		System.out.println(list);
	}
	
	//@Test
	void userCount() {
		Map<String,Object> map =operationMapper.countAdmin();
		List<Map<String,Object>> list = operationMapper.salePrice();
		//System.out.println(map);
		System.out.println(list);
	}
	
	//@Test
	void operation() {
		Post post = new Post();
		post.setPostNo(127);
		post.setPostStatus("3");
		
		post = operationMapper.getOperation(post);
		
		System.out.println("글임 : "+post);
		for (Comment comment : post.getComments()) {
			System.out.println("답변입니다 : "+comment);
		}
	}
	
	//@Test
	void contextLoads() throws Exception {
		User user = new User();
		user = userMapper.getUser("jeong");
		
		Post post = new Post();
		post.setUser(user);
		post.setTitle("��������");
		post.setContent("�������� ����");
		post.setPostStatus("1");
		
		// C
		//operationMapper.insertOperation(post);
		
		// R
		//post = operationMapper.getOperation(21);
		
		// U
		post.setContent("������ ��������");
		post.setPostNo(21);
		//operationMapper.updateOperation(post);
		
		// D
		//operationMapper.deleteOperation(post);
		
		/*
		Search search = new Search();
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		System.out.println(search);
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("postStatus", "1");
		
		List<Post> list = operationMapper.getListOperation(map);
		int rs = operationMapper.getTotalCount(map);
		
		for (Post post2 : list) {
			System.out.println("���� : "+post2);
		}
		System.out.println("�Ѱ� : "+rs);
		
		*/
		
		/*
		Attachments a1 = new Attachments();
		Attachments a2 = new Attachments();
		Attachments a3 = new Attachments();
		Attachments a4 = new Attachments();
		Attachments a5 = new Attachments();
		
		a1.setPostNo("21");
		a2.setPostNo("21");
		a3.setPostNo("21");
		a4.setPostNo("21");
		a5.setPostNo("21");
		
		a1.setAttachmentName("test��");
		a2.setAttachmentName("test��");
		a3.setAttachmentName("test��");
		a4.setAttachmentName("test��");
		a5.setAttachmentName("test��");
		
		List<Attachments> list = new ArrayList<>();
		list.add(a1);
		list.add(a2);
		list.add(a3);
		list.add(a4);
		list.add(a5);
		attachmentsMapper.insertListAttachments(list);
		*/
		/*
		Parts parts = new Parts();
		parts.setName("��ġ3");
		parts.setCalorie(170);
		parts.setPrice(1000);
		
		//productMapper.insertParts(parts);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", "��ġ2");
		
		parts = productMapper.getParts(map);
		
		System.out.println("GET : "+ parts);
		
		parts.setProductNo(1);
		
		List<Parts> list = new ArrayList<Parts>();
		list.add(parts);
		list.add(parts);
		
		//int a = productMapper.insertProudctParts(list);
		
		//System.out.println(a == list.size());
		
		list = productMapper.getProductParts(1);
		
		for (Parts parts2 : list) {
			System.out.println("재료 : "+parts2);
		}
		*/
		/*
		Comment cm = new Comment();
		cm.setUser(user);
		cm.setPostNo(10);
		cm.setContent("���� �亯�Դϴ�.");
		int r =operationMapper.insertAnswer(cm);
		System.out.println(r == 1);
		*/
		
		//String rootPath = System.getProperty("user.dir");
		//System.out.println(rootPath);
		
		Post post1 = new Post();
		List<Post> list = new ArrayList<Post>();
		for(int j=0; j<3; j++) {
			post1.setPostNo(j);
			list.add(j,post1);
		}
		for (Post post2 : list) {
			System.out.println(post2.getPostNo());
		}
		
		System.out.println(envValue);
		System.out.println("API KEY : "+api);
		
		Parts parts = new Parts();
		parts.setName("O테스트2");
		parts.setCalorie(100);
		parts.setPrice(100);
		//productService.insertParts(parts);
		
		System.out.println(parts);
		
		Map<String,Object> mapP = new HashMap<String,Object>();
		mapP.put("name",parts.getName());
		//parts = productService.getParts(mapP);
		
		parts.setPrice(10000);
		//int r = productService.updateParts(parts);
		
		//System.out.println(r==1);
		Search search = new Search();
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		mapP.put("search", search);
		List<Parts> listP = productMapper.getListParts(mapP);
		for (Parts parts2 : listP) {
			System.out.println("재료 : "+parts2);
		}
	}
	
	//@Test
	void foodCategory() throws Exception {
		
		//List<Foodcategory> list = productService.getListFoodCategory();
		
//		for (Foodcategory foodcategory : list) {
//			System.out.println("푸카 : "+foodcategory);
//		}
	}
	
	//@Test
	@Transactional
	void insertProductFinal() throws Exception {
		Product p = new Product();
		Foodcategory f= new Foodcategory();
		f.setFoodCategoryNo(1);
		
		p.setName("김치찌개9");
		p.setFoodCategory(f);
		p.setSubContent("정말 맛있는 찌개임");
		p.setPrice(50000);
		p.setThumbnail("123없음");
		p.setCalorie(5000);
		p.setContent("맛있다 정말로 김치찌개");
		p.setStock(5);
		
		Map<String,Object> map = new HashMap<>();
		map.put("name", "김치");
		Parts parts = productService.getParts(map);
		
		map.put("name", "O테스트2");
		Parts parts2 = productService.getParts(map);
		
		productService.insertProduct(p);
		
		List<Parts> list = new ArrayList();
		parts.setProductNo(p.getProductNo());
		parts2.setProductNo(p.getProductNo());
		list.add(parts);
		list.add(parts2);
		//productService.insertProudctParts(list);
	}
	
	//@Test
	void getParts() throws Exception {
		Search search = new Search();
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		Map<String,Object> map = new HashMap();
		map.put("search", search);
		List<Parts> list = productMapper.getListParts(map);
		
		for (Parts parts : list) {
			System.out.println(parts);
		}
	}
	
	//@Test
	void insertProduct() throws Exception {
		Product p = new Product();
		p.setName("김치볶음");
		p.setPrice(1000);
		p.setCalorie(100);
		p.setStock(2);		
		Foodcategory f = new Foodcategory();
		f.setFoodCategoryNo(1);
		p.setFoodCategory(f);
		p.setSubContent("김치볶음맛있다");
		p.setContent("정말로 대박 맜있는 김치볶음");
		p.setThumbnail("sss");
		
		productMapper.insertProduct(p);
		
	}
	
	//@Test
	void naver() {
		String phone = "01035937843";
		Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());
        
        int num = (generator.nextInt(1000000) % 1000000);
        String message = "인증번호는 ["+num+"] 입니다";
        
		naverServiceImpl.sendSMS(phone,message);
	}
	
	//@Test
	void email() {
		String to = "ijhghji@gmail.com";
		Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());
        
        int num = (generator.nextInt(1000000) % 1000000);
        String message = "인증번호는 ["+num+"] 입니다";
        
        emailserivce.sendMail(message, to);
	}
}
