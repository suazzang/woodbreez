package woodbreeze.wdb.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import woodbreeze.wdb.domain.*;
import woodbreeze.wdb.domain.Process;
import woodbreeze.wdb.repository.ControlStatusRepository;
import woodbreeze.wdb.repository.ProcessRepository;
import woodbreeze.wdb.service.ControlStatusService;
import woodbreeze.wdb.service.LotService;
import woodbreeze.wdb.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final LotService lotService;
    private  final ControlStatusRepository controlStatusRepository;
    private final Product product = new Product(); // 필드 초기화


    // 재고입력 폼
    @GetMapping("/product/new")
    public String createMaterial(Model model, HttpServletRequest request) {
        model.addAttribute("form", new ProductForm());

        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);

            return "product/createProductForm";
        }
        return "redirect:/";

    }
    // 재고 추가
    @RequestMapping("/product/new")
    @PostMapping("/product/new")
    public String create(@ModelAttribute("materialForm") ProductForm form) {
        // Lot 생성
        String lotId = lotService.createLot(form.getMaterialName().name(), form.getDateReceived());

        // Product 생성
        product.allMaterial(form.getId(), form.getMaterialName(), form.getMaterialQuantity(), form.getDateReceived(), form.getManufacturer(), form.getExpiry());

        // Lot 번호 설정
        Lot lot = new Lot();
        lot.setLotnumber(lotId);
        product.setLot(lot);

        // Product 저장
        productService.saveProduct(product);

        return "redirect:/product";
    }

    // 재고 조회
    @GetMapping("/product")
    public String list(Model model,HttpServletRequest request) {

        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }


        List<Product> products = productService.findProduct();

        // 각 원자재별 누적 재고량을 계산하기 위한 맵 생성
        Map<MaterialName, Integer> totalMaterialQuantities = new HashMap<>();
        for (Product product : products) {
            MaterialName materialName = product.getMaterialName();
            int quantity = product.getMaterialQuantity();
            // 원자재명이 이미 존재하는지 확인하고 존재한다면 기존 수량에 더해줌
            totalMaterialQuantities.merge(materialName, quantity, Integer::sum);
        }

// 각 제품이 사용하는 원자재 수 계산
        Map<Long, Integer> productMaterialCount = new HashMap<>();
        for (Product product : products) {
            int materialCount = 1; // 각 제품은 하나의 원자재만 사용하므로 항상 1로 설정됨
            productMaterialCount.put(product.getId(), materialCount);
        }

        model.addAttribute("totalMaterialQuantities", totalMaterialQuantities); // 모델에 원자재별 누적 재고량 추가
        model.addAttribute("productMaterialCount", productMaterialCount); // 모델에 각 제품의 원자재 수 추가
        model.addAttribute("products", products);

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);

            return "product/ProductList";
        }
        return "redirect:/";
    }

    @GetMapping("/product1")
    public String list1(Model model,HttpServletRequest request) {
        List<Product> products = productService.findProduct();

        // 각 원자재별 누적 재고량을 계산하기 위한 맵 생성
        Map<MaterialName, Integer> totalMaterialQuantities = new HashMap<>();
        for (Product product : products) {
            MaterialName materialName = product.getMaterialName();
            int quantity = product.getMaterialQuantity();
            // 원자재명이 이미 존재하는지 확인하고 존재한다면 기존 수량에 더해줌
            totalMaterialQuantities.merge(materialName, quantity, Integer::sum);
        }

// 각 제품이 사용하는 원자재 수 계산
        Map<Long, Integer> productMaterialCount = new HashMap<>();
        for (Product product : products) {
            int materialCount = 1; // 각 제품은 하나의 원자재만 사용하므로 항상 1로 설정됨
            productMaterialCount.put(product.getId(), materialCount);
        }

        model.addAttribute("totalMaterialQuantities", totalMaterialQuantities); // 모델에 원자재별 누적 재고량 추가
        model.addAttribute("productMaterialCount", productMaterialCount); // 모델에 각 제품의 원자재 수 추가
        model.addAttribute("products", products);

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);

            return "product/ProductList1";
        }
        return "redirect:/";
    }



    //재고 수정 폼
    @GetMapping("/product/{id}/edit")
    public String updateProductForm(@PathVariable("id") Long id, Model model,HttpServletRequest request) {

        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }

        Product product = productService.findProductById(id);
        if (product == null) {
            // 해당 아이디의 제품이 없을 경우 처리
            return "error";// 예를 들어, 오류 페이지로 리다이렉트하거나 에러 메시지를 반환할 수 있습니다.
        }

        // 첫 번째 제품을 가져옴 (여러 제품 중에서 어떤 것을 선택할 지에 대한 추가 로직이 필요할 수 있음)
        ProductForm form = new ProductForm();
        form.setId(product.getId());
        form.setMaterialName(product.getMaterialName());
        form.setMaterialQuantity(product.getMaterialQuantity());
        form.setDateReceived(product.getDateReceived());
        form.setManufacturer(product.getManufacturer());
        form.setExpiry(product.getExpiry());
        model.addAttribute("form", form);

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);

            return "product/updateProductForm";
        }
        return "redirect:/";

    }

    //재고 수정 레포지토리에 저장
    @PostMapping("/product/{id}/edit")
    public String updateProduct(@ModelAttribute("form") ProductForm form, @PathVariable("id") Long id) {
        Product product = productService.findProductById(id);
        if (product == null) {
            // 해당 아이디의 제품이 없을 경우 처리
            return "error";// 예를 들어, 오류 페이지로 리다이렉트하거나 에러 메시지를 반환할 수 있습니다.
        }

        // 회사 이름을 포함한 모든 속성을 수정
        product.setMaterialQuantity(form.getMaterialQuantity());
        product.setDateReceived(form.getDateReceived());
        product.setManufacturer(form.getManufacturer());
        product.setExpiry(form.getExpiry());
        // 제품 저장
        productService.saveProduct(product);
        return "redirect:/product";
    }

    // 재고 상세 조회
    @GetMapping("/product/{name}/detailed")
    public String detailedlist(@PathVariable("name") String materialName, Model model,HttpServletRequest request) {

        HttpSession session = request.getSession();
        Member loginMember1 = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 로그인 멤버가 없거나 등급이 ADMIN이 아닌 경우에는 리다이렉트
        if (loginMember1 == null || loginMember1.getGrade() != Grade.ADMIN) {
            return "redirect:/";
        }

        // 제품 이름에 해당하는 모든 제품 조회
        List<Product> products = productService.getProductsByMaterialName(materialName);

        // 수정: 제품 목록을 모델에 추가

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);
            model.addAttribute("products", products);

            return "product/detailedList";
        }
        return "redirect:/";

        // 상세 조회 페이지로 이동
    }

    @GetMapping("/product/{name}/detailed1")
    public String detailedlist1(@PathVariable("name") String materialName, Model model,HttpServletRequest request) {

        // 제품 이름에 해당하는 모든 제품 조회
        List<Product> products = productService.getProductsByMaterialName(materialName);

        // 수정: 제품 목록을 모델에 추가

        Object loginMember = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember != null){
            model.addAttribute("member",loginMember);
            model.addAttribute("products", products);

            return "product/detailedList1";
        }
        return "redirect:/";

        // 상세 조회 페이지로 이동
    }


    // 삭제
    @GetMapping("/product/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        log.info("제품삭제를 시작합니다.");
        Product product = productService.findProductById(id); // 해당 ID의 제품을 찾습니다.
        log.info("찾아온 제품(controller) : " + product);
        if (product == null) {
            // 제품이 존재하지 않는 경우, 에러 페이지로 리다이렉트합니다.
            return "redirect:/error";
        } else {
            productService.deleteById(id); // 제품을 삭제합니다.
            return "redirect:/product"; // 제품 목록 페이지로 리다이렉트합니다.
        }
    }

    //공정 시각화 !!
    @GetMapping("/product/view")
    public String listView(Model model){
        ControlStatus controlStatus1 = controlStatusRepository.findOne(1L);
        ControlStatus controlStatus2 = controlStatusRepository.findOne(2L);
        ControlStatus controlStatus3 = controlStatusRepository.findOne(3L);
        ControlStatus controlStatus4 = controlStatusRepository.findOne(4L);
        ControlStatus controlStatus5 = controlStatusRepository.findOne(5L);
        ControlStatus controlStatus6 = controlStatusRepository.findOne(6L);

        // 모델에 데이터 추가
        model.addAttribute("process1", controlStatus1);
        model.addAttribute("process2", controlStatus2);
        model.addAttribute("process3", controlStatus3);
        model.addAttribute("process4", controlStatus4);
        model.addAttribute("process5", controlStatus5);
        model.addAttribute("process6", controlStatus6);

        return "product/productData";
    }


}
