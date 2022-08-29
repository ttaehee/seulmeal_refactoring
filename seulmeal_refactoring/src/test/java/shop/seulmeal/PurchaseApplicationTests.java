package shop.seulmeal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import shop.seulmeal.common.Search;
import shop.seulmeal.service.domain.CustomParts;
import shop.seulmeal.service.domain.CustomProduct;
import shop.seulmeal.service.domain.Parts;
import shop.seulmeal.service.domain.Product;
import shop.seulmeal.service.domain.Purchase;
import shop.seulmeal.service.domain.User;
import shop.seulmeal.service.mapper.ProductMapper;
import shop.seulmeal.service.mapper.PurchaseMapper;

@SpringBootTest
class PurchaseApplicationTests {
	
	@Autowired
	private PurchaseMapper purchaseMapper;
	
	@Autowired
	private ProductMapper productMapper;	
	
	int pageUnit = 5;	
	int pageSize = 5;
	
	//@Test
	void insertCustomParts() {
		
		List<CustomParts> minus=new ArrayList<>();
		CustomParts cp1=new CustomParts();
		cp1.setMinusNo(8);
		cp1.setMinusName("소고기");
		cp1.setCustomProductNo(5);
		minus.add(cp1);
		
		List<CustomParts> plus=new ArrayList<>();
		CustomParts cp2=new CustomParts();
		Parts parts1=new Parts();
		parts1.setPartsNo(1);
		parts1.setName("양파");
		cp2.setParts(parts1);
		cp2.setGram(50);
		cp2.setCustomProductNo(5);
		plus.add(cp2);
		
		CustomParts cp3=new CustomParts();
		Parts parts2=new Parts();
		parts2.setPartsNo(8);
		parts2.setName("소고기");
		cp3.setParts(parts2);
		cp3.setCustomProductNo(5);
		cp2.setGram(100);
		plus.add(cp3);
		
		
		Map<String, Object> map=new HashMap<>();
		map.put("customProductNo",5);
		map.put("minusParts",minus);
		map.put("plusParts",plus);
			
		int result2=purchaseMapper.insertMinusParts(map);
		int result3=purchaseMapper.insertPlusParts(map);

		System.out.println("결과 : "+result2);
		System.out.println("결과 : "+result3);
		
	}
	
	//@Test
	void getCustomParts() {
		CustomParts customParts=new CustomParts();
		
		customParts=purchaseMapper.getCustomParts(11);
		System.out.println("결과 : "+customParts);

	}
	
	//@Test
	void getListCustomParts() throws Exception {
		
		Search search = new Search();
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		System.out.println("search : "+search);
		
		List<CustomParts> list=purchaseMapper.getListCustomParts(5);
		
		System.out.println("list 결과 : "+list);		
		
		//for(CustomParts cp:list) {
		//	System.out.println("커스터마이징 재료 : "+cp);
		//}

	}	
	
	//@Test
	void deleteCustomParts() {
		
		//customProductNo=1
		purchaseMapper.deleteCustomParts(1);
		
		int result=purchaseMapper.deleteCustomParts(1);
		System.out.println("결과 : "+result);
	}	
	
	
	//@Test
	void insertCustomProduct() throws Exception {
		CustomProduct customProduct=new CustomProduct();
		User user=new User();
		user.setUserId("ghm4905");
		Product product=productMapper.getProduct(4);
		
		customProduct.setUser(user);
		customProduct.setProduct(product);
		customProduct.setCount(5);
		customProduct.setCartStatus("1");
		
		int price=product.getPrice();

		
		List<CustomParts> minus=new ArrayList<>();
		CustomParts cp1=new CustomParts();
		cp1.setMinusNo(7);
		cp1.setMinusName("오이");
		minus.add(cp1);
		//customProduct.setMinusParts(minus);	
		
		List<CustomParts> plus=new ArrayList<>();
		CustomParts cp2=new CustomParts();
		Parts parts1=new Parts();
		parts1.setPartsNo(1);
		parts1.setName("양파");
		parts1.setPrice(221);	
		price += parts1.getPrice();
		cp2.setParts(parts1);
		cp2.setGram(50);
		plus.add(cp2);
		
		CustomParts cp3=new CustomParts();
		Parts parts2=new Parts();
		parts2.setPartsNo(2);
		parts2.setName("당근");
		parts2.setPrice(168);
		price += parts2.getPrice();
		cp3.setParts(parts2);
		cp2.setGram(100);
		plus.add(cp3);
		//customProduct.setPlusParts(plus);
		
		customProduct.setPrice(price);
		
		int result1=purchaseMapper.insertCustomProduct(customProduct);
		cp1.setCustomProductNo(customProduct.getCustomProductNo());
		cp2.setCustomProductNo(customProduct.getCustomProductNo());
		cp3.setCustomProductNo(customProduct.getCustomProductNo());
		
		Map<String, Object> map=new HashMap<>();
		map.put("customProductNo",customProduct.getCustomProductNo());
		map.put("minusParts",minus);
		map.put("plusParts",plus);
			
		int result2=purchaseMapper.insertMinusParts(map);
		int result3=purchaseMapper.insertPlusParts(map);
		System.out.println("결과 : "+result1);
		System.out.println("결과 : "+result2);
		System.out.println("결과 : "+result3);

		//assertEquals(purchaseMapper.getCustomProduct(3).getCount(), 7);
	}
	
	//@Test 
	void getCustomProduct() {
		purchaseMapper.getCustomProduct(52);
		
		System.out.println("커스터마이징상품 : "+purchaseMapper.getCustomProduct(52));
	}			
	
	//@Test 
	void getListCustomProduct() {
		
		Search search = new Search();
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map =new HashMap<>();
		
		map.put("search", search);
		map.put("userId","ghm4905");
		
		//커스터마이징상품 리스트 
		List<CustomProduct> list = purchaseMapper.getListCustomProduct(map);
		System.out.println("커스터마이징상품 : "+list);

	}		
	
	@Test
	void updateCustomProduct() {
		
		List<Integer> cpdNoList = new ArrayList<>();	
		cpdNoList.add(921);
		cpdNoList.add(922);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("purchaseNo", 100);
		map.put("customProductNo", cpdNoList);
		
		purchaseMapper.updateCustomProductPurchaseNo(map);
		
		CustomProduct customProduct1 = purchaseMapper.getCustomProduct(921);
		CustomProduct customProduct2 = purchaseMapper.getCustomProduct(922);
		
		assertThat(customProduct1.getPurchaseNo()).isEqualTo(100);
		assertThat(customProduct2.getPurchaseNo()).isEqualTo(100);
	}
	
	@Test
	void deleteCustomProduct() {

		purchaseMapper.deleteCustomProduct(927);
		
		CustomProduct customProduct = purchaseMapper.getCustomProduct(927);
		
		assertThat(customProduct.getCartStatus()).isEqualTo("0");
	}	
	
	@Test
	void insertPurchase() {
		Purchase purchase=new Purchase();
		User user=new User();
		user.setUserId("ghm4905");
		
		purchase.setUser(user);
		purchase.setPhone("4905");
		purchase.setPrice(10000);
		purchase.setAddress("강남");
		purchase.setName("홍길동");
		purchase.setMessage("빠른배송요");
		purchase.setPurchaseStatus("0");
		purchase.setPaymentCondition("0");
		
		purchaseMapper.insertPurchase(purchase);
		
		Purchase purchase2 = purchaseMapper.getPurchase(purchase.getPurchaseNo());
		
		assertThat(purchase2.getPhone()).isEqualTo("4905");
		assertThat(purchase2.getPrice()).isEqualTo(10000);
		assertThat(purchase2.getAddress()).isEqualTo("강남");
		assertThat(purchase2.getName()).isEqualTo("홍길동");
		assertThat(purchase2.getMessage()).isEqualTo("빠른배송요");
		assertThat(purchase2.getPurchaseStatus()).isEqualTo("0");
		assertThat(purchase2.getPaymentCondition()).isEqualTo("0");
	}
	
	//@Test
	void getPurchase() {
		
		Purchase purchase=new Purchase();
		purchase=purchaseMapper.getPurchase(2);
		System.out.println("결과 : "+purchase);
	}
	
	//@Test
	void getListPurchase() throws Exception {
		
		Search search = new Search();
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		System.out.println("search : "+search);
		
		Map<String, Object> map=new HashMap<>();
		map.put("userId","ghm4905");
		map.put("search", search);
		
		List<Purchase> list=purchaseMapper.getListPurchase(map);

	}			
	
	//@Test
	void updatePurchaseCode() {
		Purchase purchase = new Purchase();
		
		purchase.setPurchaseNo(2);
		purchase.setPurchaseStatus("1");
		
		purchaseMapper.updatePurchaseCode(purchase);
		purchase=purchaseMapper.getPurchase(2);
		System.out.println("결과 : "+purchase);
		
		assertEquals(purchase.getPurchaseStatus(), "1");
	}
	
	//@Test
	void deletePurchase() {
		int result=purchaseMapper.deletePurchase(2);
		System.out.println("결과 : "+result);
	}

}
