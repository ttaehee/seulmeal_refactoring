package shop.seulmeal.web.purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.HashedMap;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import shop.seulmeal.common.Search;
import shop.seulmeal.service.domain.CustomProduct;
import shop.seulmeal.service.domain.Parts;
import shop.seulmeal.service.domain.Point;
import shop.seulmeal.service.domain.Purchase;
import shop.seulmeal.service.domain.User;
import shop.seulmeal.service.product.ProductService;
import shop.seulmeal.service.purchase.PurchaseService;
import shop.seulmeal.service.user.UserService;
import springfox.documentation.annotations.ApiIgnore;


@ApiResponses({
    @ApiResponse(code = 200, message = "Success"),
    @ApiResponse(code = 201, message = "Success"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 500, message = "Internal Server Error")
})

@RestController
@RequestMapping("/api/v1/purchase/*")
@RequiredArgsConstructor 
@Api(tags = "PurchaseRestController")
public class PurchaseRestController {
	
	private final PurchaseService purchaseService;
	private final ProductService productService;
	private final UserService userService;
	
	@Value("${pageUnit}")
	private int pageUnit;
	
	@Value("${pageSize}")
	private int pageSize;
	
	
	//오토컴플릿
	@ApiOperation(
			value = "자동완성", 
			notes = "<h3> 추가할 재료 검색 시, 검색어가 포함된 재료목록 출력</h3>"
			+ "- insertCustomProduct.jsp, listPurchaseCart.jsp 에서 사용")
	@PostMapping("autocomplete")
	public ResponseEntity<Map<String, Object>> autocomplete(
			@RequestBody @ApiParam(value="검색어 <br> {\"value\":\"양\"}", required=true, defaultValue="{\"value\":\"양\"}") Map<String, Object> paramMap) throws Exception{
		
		List<Map> resultList = purchaseService.autocomplete(paramMap);
		paramMap.put("resultList", resultList);

		return new ResponseEntity<>(paramMap,HttpStatus.OK);
	}

	//장바구니에서 수량변경
	@ApiOperation(
			value = "장바구니에서 수량변경", 
			notes = "<h3> 장바구니에 담긴 상품의 수량을 변경 <br> 수량변경에 성공하면 1 리턴</h3>"
			+ "- listPurchaseCart.jsp 에서 사용")
	@PutMapping("cart/{customProductNo}/{count}")
	public ResponseEntity<Integer> updateCusotmProduct(
			@PathVariable @ApiParam(value="커스터마이징상품 고유키", required=true, defaultValue="650") int customProductNo, 
			@PathVariable @ApiParam(value="변경수량", required=true, defaultValue="3") int count) throws Exception {
		
		CustomProduct customProduct = new CustomProduct();
	
		customProduct=purchaseService.getCustomProduct(customProductNo);
		customProduct.setCount(count);
		
		int result = purchaseService.updateCustomProductCount(customProduct);

		return new ResponseEntity<>(result, HttpStatus.CREATED);	
	}	
	
	//커스터마이징 상품 장바구니에서 낱개삭제 
	@ApiOperation(
			value = "장바구니목록에서 낱개삭제", 
			notes = "<h3> 장바구니에 담긴 상품 낱개삭제 <br> 삭제에 성공하면 1 리턴</h3>"
			+ "- listPurchaseCart.jsp 에서 사용")
	@DeleteMapping("cart/{customProductNo}")
	@Transactional(rollbackFor= {Exception.class})
	public ResponseEntity<Integer> deleteCustomProduct(
			@PathVariable @ApiParam(value="커스터마이징상품 고유키", required=true, defaultValue="650") int customProductNo) {

		int result = purchaseService.deleteCustomProduct(customProductNo);
		
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
	
	//커스터마이징 상품 장바구니에서 선택다중삭제 
	@ApiOperation(
			value = "장바구니목록에서 다중삭제", 
			notes = "<h3> 장바구니에 담긴 상품 다중 선택삭제 <br> 삭제에 성공하면 1 리턴</h3>"
			+ "- listPurchaseCart.jsp 에서 사용")
	@DeleteMapping("cart")
	@Transactional(rollbackFor= {Exception.class})
	public ResponseEntity<Integer> deleteCustomProduct(
			@RequestBody @ApiParam(value="체크된 상품들 고유키<br>[[866],[867],[868]]", required=true, defaultValue="[[866],[867],[868]]") String checkBoxArr) {
		
		String[] check = (checkBoxArr.substring(1,checkBoxArr.length()-1)).split(",");
		
		int result = 0;
		for(String customProductNo : check ) {
			result = purchaseService.deleteCustomProduct(Integer.parseInt(customProductNo.substring(1, customProductNo.length()-1)));
		}
		
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
	
	//커스터마이징 상품 옵션수정화면 출력
	@ApiOperation(
			value = "상품옵션 수정화면 데이터", 
			notes = "<h3> 장바구니에 담긴 상품의 옵션을 수정하는 화면에 담을 데이터</h3>"
			+ "- listPurchaseCart.jsp 에서 사용")
	@GetMapping("customcart/{customProductNo}")
	@Transactional(rollbackFor= {Exception.class})
	public Map<String, Object> getCustomProduct(
			@PathVariable @ApiParam(value="커스터마이징상품 고유키", required=true, defaultValue="650") int customProductNo
			) throws Exception {

		CustomProduct customProduct = new CustomProduct();
		customProduct=purchaseService.getCustomProduct(customProductNo);
		
		List<Parts> partsList=productService.getProductParts(customProduct.getProduct().getProductNo());
		
		Map<String, Object> map= new HashedMap();
		map.put("customProduct", customProduct);
		map.put("partsList",partsList);
		
		return map;
	}	
	
	//포인트사용 시 비밀번호확인
	@ApiOperation(
			value = "비밀번호 체크", 
			notes = "<h3>포인트사용 시 비밀번호(로그인시 사용하는)확인</h3>"
			+ "- insertPurchase.jsp 에서 사용")
	@GetMapping("point/{password}/{usePoint}/{userId}")
	public JSONObject confirmPassword(
			@PathVariable @ApiParam(value="비밀번호", required=true, defaultValue="1234") String password, 
			@PathVariable @ApiParam(value="사용할 포인트", required=true, defaultValue="100") int usePoint, 
			@PathVariable @ApiParam(value="사용자 아이디", required=true, defaultValue="gghm4905") String userId) throws Exception {
		
		User user=userService.getUser(userId);
		String realPw=user.getPassword();
		int realPt=user.getTotalPoint();
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		JSONObject json=new JSONObject();
		if(encoder.matches(password, realPw) && usePoint<=realPt) {
			json.put("success", "true");
		}else if(usePoint > realPt) {
			json.put("success","pt");
		}else {
			json.put("success","pw");
		}
		return json;	
	}	
	
	//아임포트 결제 전 DB에 insertPurchase
	@ApiOperation(
			value = "구매 등록", 
			notes = "<h3>결제 전 구매테이블에 등록, 추후 이 정보와 결제 후 정보 비교</h3>"
			+ "- insertPurchase.jsp 에서 사용")
	@PostMapping("purchase")
	public Purchase insertPurchase(
			@RequestBody Map<String, Object> map) throws Exception {
		
		System.out.println((String)map.get("userId"));
		User user=userService.getUser((String)map.get("userId"));
		Purchase purchase = new Purchase();
		
		purchase.setUser(user);
		purchase.setName((String)map.get("name"));
		purchase.setAddress((String)map.get("address"));
		purchase.setPhone((String)map.get("phone"));
		purchase.setEmail((String)map.get("email"));
		purchase.setMessage((String)map.get("message"));
		purchase.setPrice(Integer.parseInt((String)map.get("price")));
		purchase.setPaymentCondition(String.valueOf(map.get("paymentCondition")));
		purchase.setUsePoint(Integer.parseInt((String)map.get("usePoint")));
		
		//insert
		purchaseService.insertPurchase(purchase);
		
		//커스터마이징상품에 구매번호추가 but 결제중 취소할 수 있으니 구매상태는 구매완료가 아닌 0
		ArrayList customProductNo=(ArrayList) map.get("customProductNo");
		
		List<CustomProduct> cpList=new ArrayList<CustomProduct>();
		for(int i=0; i<customProductNo.size(); i++) {
			CustomProduct cp=new CustomProduct();
			cp=purchaseService.getCustomProduct(Integer.parseInt((String)customProductNo.get(i)));
			cp.setPurchaseNo(purchase.getPurchaseNo());
			purchaseService.updateCustomProductPurchaseNo(cp);
			cpList.add(cp);
		}
		
		//구매 insert 하자마자 구매정보 get
		purchase=purchaseService.getPurchase(purchase.getPurchaseNo());
		purchase.setUser(user);

		return purchase;		
	}	
	
	//아임포트 검증
	@ApiOperation(
			value = "아임포트 서버정보와 비교", 
			notes = "<h3>구매 후 아임포트 서버에 접근해 결제금액 일치하는지 비교</h3>"
			+ "- insertPurchase.jsp 에서 사용")
	@PostMapping("iamport")
	public JSONObject verifyIamport(
			@RequestBody Purchase purchase
			) throws Exception {
		
		//결제완료 시 구매상태 상품준비중으로 변경
		int success = purchaseService.updatePurchase(purchase);
			
		purchase=purchaseService.getPurchase(purchase.getPurchaseNo());
		User user=userService.getUser(purchase.getUser().getUserId());
		purchase.setUser(user);		
		
		String token=purchaseService.getImportToken();
		
		JSONObject json=new JSONObject();
		Point point = new Point();
		if(success ==1) {
			String portAmount=purchaseService.getAmount(token, Integer.toString(purchase.getPurchaseNo()));
			
			if(purchase.getPrice() == Integer.parseInt(portAmount)) {
				
				//커스터마이징상품은 장바구니리스트에서 삭제 
				List<CustomProduct> cpList=purchase.getCustomProduct();
				for(CustomProduct cp : cpList) {
					purchaseService.updateCustomProductStatus(cp);
				}
				
				if(purchase.getUsePoint()!=0) {
					//사용포인트
					point.setUserId(user.getUserId());
					point.setPurchaseNo(purchase.getPurchaseNo());
					point.setPointStatus("0");
					point.setPoint(purchase.getUsePoint());
					userService.insertPoint(point);
					//총포인트에서 사용포인트 빼기
					user.setTotalPoint(user.getTotalPoint()-purchase.getUsePoint());
					userService.updateUserTotalPoint(user);
				}
				
				json.put("purchase", purchase);
				json.put("sucess", "true");
				json.put("message", "성공!!!!!!");
			}else {
				json.put("success", "false");
				int cancel=purchaseService.cancelPayment(token, Integer.toString(purchase.getPurchaseNo()));
				if(cancel==1) {
					json.put("message", "성공!!!!!");
				}else {
					json.put("message", "실패");
				}
			}
		}else {
			purchaseService.cancelPayment(token, Integer.toString(purchase.getPurchaseNo()));
			json.put("message", "취소실패ㅠㅠ");
		}
		return json;
	}
	
	//구매내역리스트 무한스크롤
	@ApiIgnore
	@RequestMapping(value={"/purchases","/purchases/{currentPage}", "/purchases/{currentPage}/{searchCondition}"})
	public List<Purchase> getListPurchase(
			@PathVariable int currentPage, 
			@PathVariable(required = false) String searchCondition, 
			HttpSession session)
			throws Exception {
		
		User user=(User)session.getAttribute("user");
		String userId=user.getUserId();
		
		Search search = new Search();
		search.setCurrentPage(currentPage);
		search.setPageSize(pageSize);
		search.setSearchCondition(searchCondition);

		Map<String, Object> map = purchaseService.getListPurchase(search, userId);
		List<Purchase> purchaseList=(List<Purchase>)map.get("purchaseList");
		
		for(Purchase p:purchaseList) {
			p.setUser(user);
		}
		return purchaseList;
	}
	
	//배송하기, 구매확정 후 구매처리상태변경
	@ApiOperation(
			value = "구매처리상태변경", 
			notes = "<h3>purchaseStatus - 결제 전 : 0, 구매완료 : 1, 배송중 : 2, 배송완료 : 3, 구매확정 : 4</h3>"
			+ "- listPurchase.jsp, listPurchaseSale.jsp 에서 사용")
   @PutMapping("purchase")
   public int updatePurchaseCode(  //purchaseNo : purchaseNo,purchaseStatus: "4"
		   @RequestBody @ApiParam(value="구매 정보", required=true, defaultValue="{\"purchaseNo\":\\{\"4\"}}") Purchase purchase
		   ) throws Exception{

         purchaseService.updatePurchaseCode(purchase);
         purchase=purchaseService.getPurchase(purchase.getPurchaseNo());
         User user=userService.getUser(purchase.getUser().getUserId());
         purchase.setUser(user);
         
         //구매확정 후 포인트적립
         int plusPoint=0;
         if(purchase.getPurchaseStatus().equals("4")) {
         
            int total=purchase.getPrice()+purchase.getUsePoint();
            String grade=user.getGrade();
            
            if(grade.equals("0")) {
               plusPoint=(int) (total*0.005);
            }else if(grade.equals("1")) {
               plusPoint=(int) (total*0.01);
            }else if(grade.equals("2")) {
               plusPoint=(int) (total*0.03);
            }else if(grade.equals("3")) {
               plusPoint=(int) (total*0.05);
            }
            
           Point point = new Point();
           point.setUserId(user.getUserId());
           point.setPurchaseNo(purchase.getPurchaseNo());
           point.setPointStatus("1");
           point.setPoint(plusPoint);
           userService.insertPoint(point);
           //유저 총포인트
           user.setTotalPoint(user.getTotalPoint()+plusPoint);
           userService.updateUserTotalPoint(user);
         }           
         return plusPoint;
      } 
   
	//구매내역 삭제 
	@ApiOperation(
			value = "구매내역 삭제 ", 
			notes = "<h3>구매내역 목록에서 삭제 <br> 삭제에 성공하면 1 리턴</h3>"
			+ "- listPurchase.jsp 에서 사용")
	@DeleteMapping("purchase/{purchaseNo}")
	@Transactional(rollbackFor= {Exception.class})
	public int deletePurchase(
			@PathVariable @ApiParam(value="구매내역 고유키", required=true, defaultValue="1") int purchaseNo) {
		
		int success = purchaseService.deletePurchase(purchaseNo);
		
		return success;
	}	
	
}
