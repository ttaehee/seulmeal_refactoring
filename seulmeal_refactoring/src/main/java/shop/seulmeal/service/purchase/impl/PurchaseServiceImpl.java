package shop.seulmeal.service.purchase.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import shop.seulmeal.common.Search;
import shop.seulmeal.service.domain.CustomParts;
import shop.seulmeal.service.domain.CustomProduct;
import shop.seulmeal.service.domain.Parts;
import shop.seulmeal.service.domain.Purchase;
import shop.seulmeal.service.mapper.PurchaseMapper;
import shop.seulmeal.service.purchase.PurchaseService;
import shop.seulmeal.web.purchase.dto.CustomProductDto;

@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService{
	
	
	@Value("${IMPORT_TOKEN_URL}")
	private String IMPORT_TOKEN_URL;
	
	@Value("${IMPORT_PAYMENTINFO_URL}")
	private String IMPORT_PAYMENTINFO_URL;
	
	@Value("${IMPORT_CANCEL_URL}")
	private String IMPORT_CANCEL_URL;
	
	@Value("${IMPORT_PREPARE_URL}")
	private String IMPORT_PREPARE_URL;
	
	@Value("${iamport.Key}")
	private String KEY;
	
	@Value("${iamport.Secret}")
	private String SECRET;
	
	@Autowired
	private PurchaseMapper purchaseMapper;
	
	public PurchaseServiceImpl() {
		System.out.println(this.getClass());
	}
	
	//오토컴플릿
	@Override
	public List<Map> autocomplete(Map<String, Object> paramMap) throws Exception {
		return purchaseMapper.autocomplete(paramMap);
	}	
	
	//커스터마이징재료 
	@Override
	public int insertMinusParts(int customProductNo, CustomProductDto dto) {
		
		List<CustomParts> minusParts = new ArrayList<>();
		
		for(int i=0; i<dto.getMinusNo().size(); i++) {
			CustomParts minus = new CustomParts();
			minus.setMinusNo(dto.getMinusNo().get(i));
			minus.setMinusName(dto.getMinusName().get(i));
			minus.setCustomProductNo(customProductNo);
			minusParts.add(minus);
		}
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("minusParts",minusParts);
		
		return purchaseMapper.insertMinusParts(map);
	}
	
	@Override
	public int insertPlusParts(int customProductNo, CustomProductDto dto) {
		
		List<CustomParts> plusParts = new ArrayList<>();
		
		for(int i=0; i<dto.getPlusPartsNo().size(); i++) {
			Parts p = new Parts();
			p.setPartsNo(dto.getPlusPartsNo().get(i));
			p.setPrice(dto.getPlusPrice().get(i));
			
			CustomParts plus = new CustomParts();
			plus.setParts(p);
			plus.setGram(dto.getPlusGram().get(i));
			plus.setCustomProductNo(customProductNo);
			plusParts.add(plus);
		}
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("plusParts",plusParts);
		
		return purchaseMapper.insertPlusParts(map);
	}

	@Override 
	public CustomParts getCustomParts(int customPartsNo) {
		return purchaseMapper.getCustomParts(customPartsNo);
	}

	@Override
	public List<CustomParts> getListCustomParts(int customProductNo) {
		
		return purchaseMapper.getListCustomParts(customProductNo);
	}

	@Override
	public int deleteCustomParts(int customProductNo) {
		return purchaseMapper.deleteCustomParts(customProductNo);
	}
	
	
	//커스터마이징상품 
	@Override
	public int insertCustomProduct(CustomProduct customProduct) {
		return purchaseMapper.insertCustomProduct(customProduct);
	}

	@Override
	public CustomProduct getCustomProduct(int customProductNo) {
		return purchaseMapper.getCustomProduct(customProductNo);
	}

	@Override
	public Map<String, Object> getListCustomProduct(Search search, String userId) {
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("search", search);
		map.put("userId", userId);
	
		map.put("cproductList", purchaseMapper.getListCustomProduct(map));
		map.put("totalCount",purchaseMapper.getCustomProductTotalCount(map));
		
		return map;
	}

	@Override
	public int updateCustomProductPurchaseNo(int purchaseNo, List<Integer> customProductNo) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("purchaseNo", purchaseNo);
		map.put("customProductNo", customProductNo);
		
		return purchaseMapper.updateCustomProductPurchaseNo(map);
	}
	
	@Override
	public int updateCustomProductStatus(List<CustomProduct> customProductList) {
		
		List<Integer> list = new ArrayList<>();
		for(CustomProduct cp : customProductList) {
			list.add(cp.getCustomProductNo());
		}
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("cproductList", list);
		return purchaseMapper.updateCustomProductStatus(map);
	}
	
	
	@Override
	public int updateCustomProductCount(CustomProduct customProduct) {
		return purchaseMapper.updateCustomProductCount(customProduct);
	}

	@Override
	public int deleteCustomProduct(int customProductNo) {
		return purchaseMapper.deleteCustomProduct(customProductNo);
	}
	
	@Override
	public int deleteCustomProducts(List<Integer> customProductList) {
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("cproductList", customProductList);
		return purchaseMapper.updateCustomProductStatus(map);
	}
	
	//구매 
	@Override
	public Purchase insertPurchase(Purchase purchase) {
		purchaseMapper.insertPurchase(purchase);
		purchase = purchaseMapper.getPurchase(purchase.getPurchaseNo());
		return purchase;
	}

	@Override
	public Purchase getPurchase(int purchaseNo) {
		return purchaseMapper.getPurchase(purchaseNo);
	}

	@Override
	public Map<String, Object> getListPurchase(Search search, String userId) {
		
		Map<String, Object> map=new HashMap<>();
		map.put("search", search);
		map.put("userId", userId);
		
		List<Purchase> list=purchaseMapper.getListPurchase(map);
		int totalCount=purchaseMapper.getPurchaseTotalCount(map);
		
		map.put("purchaseList", list);
		map.put("totalCount", totalCount);
		
		return map;
	}
	
	@Override
	public int updatePurchase(Purchase purchase) {
		return purchaseMapper.updatePurchase(purchase);
	}

	@Override
	public int updatePurchaseCode(Purchase purchase) {
		return purchaseMapper.updatePurchaseCode(purchase);
	}

	@Override
	public int deletePurchase(int purchaseNo) {
		return purchaseMapper.deletePurchase(purchaseNo);
	}
	@Override
	//아임포트 인증(토큰)받아주는 함수
	public String getImportToken() {
		
		String result = "";
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(IMPORT_TOKEN_URL);
		Map<String,String> m  =new HashMap<String,String>();
		m.put("imp_key", KEY);
		m.put("imp_secret", SECRET);
		try {
			post.setEntity(new UrlEncodedFormEntity(convertParameter(m)));
			HttpResponse res = client.execute(post);
			
			ObjectMapper mapper = new ObjectMapper();
			String body = EntityUtils.toString(res.getEntity());
			
			JsonNode rootNode = mapper.readTree(body);
			JsonNode resNode = rootNode.get("response");
			result = resNode.get("access_token").asText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	// Map을 사용해서 Http요청 파라미터를 만들어 주는 함수
	public List<NameValuePair> convertParameter(Map<String,String> paramMap){
		
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		
		Set<Entry<String,String>> entries = paramMap.entrySet();
		
		for(Entry<String,String> entry : entries) {
			paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return paramList;
	}	
	
	@Override
	// 결제취소
	public int cancelPayment(String token, String mid) {
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(IMPORT_CANCEL_URL);
		Map<String, String> map = new HashMap<String, String>();
		post.setHeader("Authorization", token);
		map.put("merchant_uid", mid);
		String asd = "";
		try {
			post.setEntity(new UrlEncodedFormEntity(convertParameter(map)));
			HttpResponse res = client.execute(post);
			ObjectMapper mapper = new ObjectMapper();
			String enty = EntityUtils.toString(res.getEntity());
			JsonNode rootNode = mapper.readTree(enty);
			asd = rootNode.get("response").asText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (asd.equals("null")) {
			System.err.println("환불실패");
			return -1;
		} else {
			System.err.println("환불성공");
			return 1;
		}
	}
	
	@Override
	// 아임포트 결제정보에서 amount 조회
	public String getAmount(String token, String mId) {
		
		String amount = "";
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(IMPORT_PAYMENTINFO_URL + mId + "/paid");
		get.setHeader("Authorization", token);

		try {
			HttpResponse res = client.execute(get);
			ObjectMapper mapper = new ObjectMapper();
			String body = EntityUtils.toString(res.getEntity());
			JsonNode rootNode = mapper.readTree(body);
			JsonNode resNode = rootNode.get("response");
			amount = resNode.get("amount").asText();
			System.out.println("======================amount : "+amount);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return amount;
	}
	
	//판매내역리스트 
	@Override
	public Map<String, Object> getListSale(Search search) {
		
		Map<String, Object> map=new HashMap<>();
		
		List<Purchase> list=purchaseMapper.getListSale(search);
		int totalCount=purchaseMapper.getSaleTotalCount(search);
		
		map.put("saleList", list);
		map.put("totalCount", totalCount);
		
		return map;
	}

}
