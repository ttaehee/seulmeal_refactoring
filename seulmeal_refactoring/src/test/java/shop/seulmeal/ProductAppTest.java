package shop.seulmeal;

import java.util.List;
import java.util.Map;

import javax.management.BadAttributeValueExpException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.Assert;
import shop.seulmeal.common.Search;
import shop.seulmeal.service.domain.Foodcategory;
import shop.seulmeal.service.domain.Parts;
import shop.seulmeal.service.domain.Product;
import shop.seulmeal.service.domain.Review;
import shop.seulmeal.service.domain.User;
import shop.seulmeal.service.product.ProductService;

@SpringBootTest
@WebAppConfiguration
public class ProductAppTest {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	int pageUnit = 5;
	int pageSize = 5;

	public void testInsertProduct() throws Exception {

		Product product = new Product();
		Foodcategory food = new Foodcategory();
		food.setFoodCategoryNo(0);

		product.setFoodCategory(food);
		product.setName("test01");
		product.setSubContent("TEST01");
		product.setPrice(1000);
		product.setCalorie(1000);
		product.setContent("TEST01 PROCESSING");
		product.setStock(100);
		product.setThumbnail("0");

		System.out.println(product);

		productService.insertProduct(product);

	}

	public void testGetProduct() throws Exception {
		Product product = productService.getProduct(1);
		System.out.println(product);
	}

	public void testListProduct() throws Exception {
		Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	Map<String,Object> map = productService.getListProduct(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
	 	System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("");
	 	map = productService.getListProduct(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
	 	System.out.println(list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);

	}

	public void testGetProductAndUpdateProduct() throws Exception {
		Product product = productService.getProduct(1);

		Foodcategory food = new Foodcategory();
		food.setFoodCategoryNo(3);

		product.setFoodCategory(food);
		product.setName("UPDATED");
		product.setSubContent("업데이트 완료");
		product.setPrice(5900);
		product.setCalorie(980);
		product.setContent("업데이트를 성공적으로 마쳤습니다");
		product.setStock(50);
		product.setThumbnail("1");

		System.out.println(product);

		productService.updateProduct(product);
		System.out.println(productService.getProduct(1));
	}

	public void testDeleteProduct() throws Exception {
		Product product = productService.getProduct(1);

		int A = product.getProductNo();
		System.out.println(product.getStatus());

		productService.deleteProduct(A);

		product = productService.getProduct(1);
		System.out.println(product.getStatus());
	}
	
	public void testRestoreProduct() throws Exception {
		Product product = productService.getProduct(1);

		int A = product.getProductNo();
		System.out.println(product.getStatus());

		productService.restoreProduct(A);

		product = productService.getProduct(1);
		System.out.println(product.getStatus());
	}

	
	// FOOD CATEGORY
	// test done
	
	public void testInsertFoodCategory() throws Exception {
		productService.insertFoodCategory("프랜차이즈");
	}

	public void testListFoodCategory() throws Exception {
		Search search = new Search();

	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	List<Foodcategory> list = productService.getListFoodCategory();
	 	
//	 	List<Object> list = (List<Object>)map.get("list");
//	 	Assert.assertEquals(3, list.size());
	 	
	 	System.out.println(list);
	 	
//	 	Integer totalCount = (Integer)map.get("totalCount");
//	 	System.out.println(totalCount);

	}

	// test done +
	public void testDeleteFoodCategory() throws Exception {
		productService.deleteFoodCategory(6);
	}
	
	public void testRestoreFoodCategory() throws Exception {
		productService.restoreFoodCategory(6);
	}
	
	// REVIEW
	// test done +
	public void testInsertReview() throws Exception {
		Product product = new Product();
		Review review = new Review();
		User user = new User();

		product.setProductNo(2);
		user.setUserId("LJC");

		review.setProduct(product);
		review.setUser(user);
		review.setTitle("테스트 생성");
		review.setContent("테스트 입력 성공");
		review.setRating(2);

		productService.insertReview(review);

	}
	
	public void testGetReviewAndUpdateReview() throws Exception {
		Review review = productService.getReview(42);
		System.out.println(review);
		
		review.setTitle("수정되었습니다");
		review.setContent("오류나면 안 돼요");
		review.setRating(3);
		System.out.println(review);
		productService.updateReview(review);

	}
	
	public void testGetListReview() throws Exception {
		Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	Map<String,Object> map = productService.getListReview(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
	 	System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	}
	
	public void testDeleteReview() throws Exception {
		Review review = productService.getReview(42);
		System.out.println(review);
		
		productService.deleteReview(review.getReviewNo());
		System.out.println(review.getStatus());
	}
	
	public void testInsertParts() throws Exception {
		Parts pt = new Parts();
		pt.setName("망고");
		pt.setPrice(150);
		pt.setCalorie(80);
		System.out.println(pt);
		productService.insertParts(pt);
	}
}
