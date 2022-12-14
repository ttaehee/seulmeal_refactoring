package shop.seulmeal.web.purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.emory.mathcs.backport.java.util.Arrays;
import lombok.RequiredArgsConstructor;
import shop.seulmeal.common.Page;
import shop.seulmeal.common.Search;
import shop.seulmeal.service.domain.CustomProduct;
import shop.seulmeal.service.domain.Parts;
import shop.seulmeal.service.domain.Point;
import shop.seulmeal.service.domain.Product;
import shop.seulmeal.service.domain.Purchase;
import shop.seulmeal.service.domain.User;
import shop.seulmeal.service.product.ProductService;
import shop.seulmeal.service.purchase.PurchaseService;
import shop.seulmeal.service.user.UserService;
import shop.seulmeal.web.purchase.dto.CustomProductDto;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/api/v1/purchase/*")
@RequiredArgsConstructor
@ApiIgnore
public class PurchaseController {

	private final PurchaseService purchaseService;
	private final ProductService productService;
	private final UserService userService;

	@Value("${pageUnit}")
	private int pageUnit;

	@Value("${pageSize}")
	private int pageSize;

	// 커스터마이징 옵션선택 화면출력
	@GetMapping("custom/{productNo}")
	public String insertCustomProduct(@PathVariable int productNo, Model model) throws Exception {

		Product product = productService.getProduct(productNo);
		List<Parts> partsList = productService.getProductParts(productNo);

		model.addAttribute("product", product);
		model.addAttribute("partsList", partsList);

		return "purchase/insertPurchaseCustomProduct";

	}

	// 커스터마이징 상품 인서트
	@PostMapping("custom")
	public String insertCustomProduct(@RequestParam(value = "productNo") int productNo, CustomProductDto dto,
			Model model, HttpSession session) throws Exception {

		CustomProduct customProduct = dto.toDomain();
		customProduct.setProduct(productService.getProduct(productNo));
		customProduct.setUser((User) session.getAttribute("user"));

		customProduct = purchaseService.insertCustomProduct(customProduct);
		
		// minus parts
		if (dto.getMinusNo().size() > 0) {
			purchaseService.insertMinusParts(customProduct.getCustomProductNo(), dto);
		}
		
		// plus parts
		if (dto.getPlusPartsNo().size() > 0) {
			purchaseService.insertPlusParts(customProduct.getCustomProductNo(), dto);
		}
		
		if(customProduct.getCartStatus().equals("1")) {
			return "redirect:/api/v1/purchase/cart/1";
		}else {
			model.addAttribute("customProduct", customProduct);
			model.addAttribute("cartStatus", "0");
			
			return "purchase/insertPurchase";
		}
		
	}

	// 장바구니 리스트
	@GetMapping("cart/{currentPage}")
	public String getListCustomProduct(@PathVariable(required = false) int currentPage, Model model,
			HttpSession session) {

		User user = (User) session.getAttribute("user");
		String userId = user.getUserId();

		Search search = new Search();
		search.setCurrentPage(currentPage);
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);

		// 커스터마이징상품 리스트
		Map<String, Object> map = purchaseService.getListCustomProduct(search, userId);

		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);

		model.addAttribute("customProductList", map.get("cproductList"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "purchase/listPurchaseCart";

	}

	// 커스터마이징 상품 옵션수정(커스터마이징재료 삭제 후 추가)
	@PostMapping("customcart")
	public String updateCustomProduct(CustomProductDto dto,
			Model model, HttpSession session) {
		
		purchaseService.updateCustomProductCount(dto.toDomain());
		purchaseService.deleteCustomParts(dto.getCustomProductNo());

		// minus parts
		if (dto.getMinusNo().size() > 0) {
			purchaseService.insertMinusParts(dto.getCustomProductNo(), dto);
		}

		// plus parts
		if (dto.getPlusPartsNo().size() > 0) {
			purchaseService.insertPlusParts(dto.getCustomProductNo(), dto);
		}

		return "redirect:/api/v1/purchase/cart/1";
	}

	// 장바구니 거쳐서 구매정보입력창
	@GetMapping("purchase")
	public String insertPurchase(Model model, HttpSession session) {

		User user = (User) session.getAttribute("user");
		String userId = user.getUserId();

		Search search = new Search();
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);

		Map<String, Object> map = purchaseService.getListCustomProduct(search, userId);

		model.addAttribute("customProductList", map.get("cproductList"));
		model.addAttribute("cartStatus", "1");

		return "purchase/insertPurchase";

	}

	// 포인트만으로 결제시
	@PostMapping("point")
	public String insertPurchase(Purchase purchase, Integer[] customProductNo, HttpSession session, User user,
			Point point, Model model) throws Exception {

		user = (User) session.getAttribute("user");
		purchase.setUser(user);
		purchase = purchaseService.insertPurchase(purchase);

		// 구매완료로 구매상태변경
		purchaseService.updatePurchase(purchase);

		// customProduct 에 구매번호추가
		List<Integer> cpdNoList = new ArrayList<Integer>(Arrays.asList(customProductNo));
		purchaseService.updateCustomProductPurchaseNo(purchase.getPurchaseNo(), cpdNoList);
		// 장바구니리스트에서 삭제
		purchaseService.deleteCustomProducts(cpdNoList);

		// 사용포인트
		point.setUserId(user.getUserId());
		point.setPurchaseNo(purchase.getPurchaseNo());
		point.setPointStatus("0");
		point.setPoint(purchase.getUsePoint());
		userService.insertPoint(point);

		// 총포인트에서 사용포인트 빼기
		user.setTotalPoint(user.getTotalPoint() - purchase.getUsePoint());
		userService.updateUserTotalPoint(user);

		model.addAttribute(purchase);

		return "redirect:/api/v1/purchase/purchase/" + purchase.getPurchaseNo();

	}

	// 구매상세
	@GetMapping("purchase/{purchaseNo}")
	public String getPurchase(@PathVariable int purchaseNo, Purchase purchase, Model model) throws Exception {

		purchase = purchaseService.getPurchase(purchaseNo);

		model.addAttribute(purchase);

		return "purchase/getPurchase";

	}

	// 구매내역리스트
	@RequestMapping(value = "/")
	public String getListPurchase(Search search, Model model, HttpSession session) throws Exception {

		User user = (User) session.getAttribute("user");
		String userId = user.getUserId();

		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);

		Map<String, Object> map = purchaseService.getListPurchase(search, userId);

		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);

		model.addAttribute("purchaseList", map.get("purchaseList"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "purchase/listPurchase";
	}

	// 판매내역목록
	@RequestMapping(value = { "/sales/{currentPage}/{purchaseStatus}", "/sales/{currentPage}", "/sales" })
	public String getListSale(@PathVariable int currentPage, @PathVariable(required = false) String purchaseStatus,
			Search search, Model model, HttpSession session) throws Exception {

		search.setCurrentPage(currentPage);
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		search.setSearchCondition(purchaseStatus);

		Map<String, Object> map = purchaseService.getListSale(search);

		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);

		model.addAttribute("saleList", map.get("saleList"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "purchase/listPurchaseSale";
	}

}