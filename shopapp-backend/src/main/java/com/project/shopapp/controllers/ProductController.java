package com.project.shopapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.components.SecurityUtils;
import com.project.shopapp.dtos.*;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.ResponseObject;
import com.project.shopapp.responses.product.ProductListResponse;
import com.project.shopapp.responses.product.ProductResponse;
import com.project.shopapp.services.product.IProductRedisService;
import com.project.shopapp.services.product.IProductService;
import ChromeBrowser.MessageKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
        private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
        private final IProductService productService;
        private final LocalizationUtils localizationUtils;
        private final IProductRedisService productRedisService;
        private final SecurityUtils securityUtils;

        @PostMapping("")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        // POST http://localhost:8088/v1/api/products
        public ResponseEntity<ResponseObject> createProduct(
                        @Valid @RequestBody ProductDTO productDTO,
                        BindingResult result) throws Exception {
                if (result.hasErrors()) {
                        List<String> errorMessages = result.getFieldErrors()
                                        .stream()
                                        .map(FieldError::getDefaultMessage)
                                        .toList();
                        return ResponseEntity.badRequest().body(
                                        ResponseObject.builder()
                                                        .message(String.join("; ", errorMessages))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build());
                }
                Product newProduct = productService.createProduct(productDTO);
                return ResponseEntity.ok(
                                ResponseObject.builder()
                                                .message("Create new product successfully")
                                                .status(HttpStatus.CREATED)
                                                .data(newProduct)
                                                .build());
        }

        @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        // POST http://localhost:8088/v1/api/products
        public ResponseEntity<ResponseObject> uploadImages(
                        @PathVariable("id") Long productId,
                        @ModelAttribute("files") List<MultipartFile> files) throws Exception {
                Product existingProduct = productService.getProductById(productId);
                files = files == null ? new ArrayList<MultipartFile>() : files;
                if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                        return ResponseEntity.badRequest().body(
                                        ResponseObject.builder()
                                                        .message(localizationUtils
                                                                        .getLocalizedMessage(
                                                                                        MessageKeys.UPLOAD_IMAGES_MAX_5))
                                                        .build());
                }
                List<ProductImage> productImages = new ArrayList<>();
                for (MultipartFile file : files) {
                        if (file.getSize() == 0) {
                                continue;
                        }

                        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                                                .body(ResponseObject.builder()
                                                                .message(localizationUtils
                                                                                .getLocalizedMessage(
                                                                                                MessageKeys.UPLOAD_IMAGES_FILE_LARGE))
                                                                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                                                                .build());
                        }
                        String contentType = file.getContentType();
                        if (contentType == null || !contentType.startsWith("image/")) {
                                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                                .body(ResponseObject.builder()
                                                                .message(localizationUtils
                                                                                .getLocalizedMessage(
                                                                                                MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE))
                                                                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                                                .build());
                        }

                        String filename = productService.storeFile(file);
                        ProductImage productImage = productService.createProductImage(
                                        existingProduct.getId(),
                                        ProductImageDTO.builder()
                                                        .imageUrl(filename)
                                                        .build());
                        productImages.add(productImage);
                }

                return ResponseEntity.ok().body(ResponseObject.builder()
                                .message("Upload image successfully")
                                .status(HttpStatus.CREATED)
                                .data(productImages)
                                .build());
        }

        @GetMapping("/images/{imageName}")
        public ResponseEntity<?> viewImage(@PathVariable String imageName) {
                try {
                        java.nio.file.Path imagePath = Paths.get("shopapp-backend/uploads/" + imageName);
                        UrlResource resource = new UrlResource(imagePath.toUri());

                        if (resource.exists()) {
                                return ResponseEntity.ok()
                                                .contentType(MediaType.IMAGE_JPEG)
                                                .body(resource);
                        } else {
                                return ResponseEntity.ok()
                                                .contentType(MediaType.IMAGE_JPEG)
                                                .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
                                // return ResponseEntity.notFound().build();
                        }
                } catch (Exception e) {
                        return ResponseEntity.notFound().build();
                }
        }

        @GetMapping("")
        public ResponseEntity<ResponseObject> getProducts(
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int limit) throws JsonProcessingException {
                int totalPages = 0;
                // productRedisService.clear();
                PageRequest pageRequest = PageRequest.of(
                                page, limit,
                                // Sort.by("createdAt").descending()
                                Sort.by("id").ascending());
                logger.info(String.format("keyword = %s, category_id = %d, page = %d, limit = %d",
                                keyword, categoryId, page, limit));
                List<ProductResponse> productResponses = productRedisService
                                .getAllProducts(keyword, categoryId, pageRequest);
                if (productResponses != null && !productResponses.isEmpty()) {
                        totalPages = productResponses.get(0).getTotalPages();
                }
                if (productResponses == null) {
                        Page<ProductResponse> productPage = productService
                                        .getAllProducts(keyword, categoryId, pageRequest);                      
                        totalPages = productPage.getTotalPages();
                        productResponses = productPage.getContent();
                        for (ProductResponse product : productResponses) {
                                product.setTotalPages(totalPages);
                        }
                        productRedisService.saveAllProducts(
                                        productResponses,
                                        keyword,
                                        categoryId,
                                        pageRequest);
                }
                ProductListResponse productListResponse = ProductListResponse
                                .builder()
                                .products(productResponses)
                                .totalPages(totalPages)
                                .build();
                return ResponseEntity.ok().body(ResponseObject.builder()
                                .message("Get products successfully")
                                .status(HttpStatus.OK)
                                .data(productListResponse)
                                .build());
        }

        // http://localhost:8088/api/v1/products/6
        @GetMapping("/{id}")
        public ResponseEntity<ResponseObject> getProductById(
                        @PathVariable("id") Long productId) throws Exception {
                Product existingProduct = productService.getProductById(productId);
                return ResponseEntity.ok(ResponseObject.builder()
                                .data(ProductResponse.fromProduct(existingProduct))
                                .message("Get detail product successfully")
                                .status(HttpStatus.OK)
                                .build());

        }

        @GetMapping("/by-ids")
        public ResponseEntity<ResponseObject> getProductsByIds(@RequestParam("ids") String ids) {
                List<Long> productIds = Arrays.stream(ids.split(","))
                                .map(Long::parseLong)
                                .collect(Collectors.toList());
                List<Product> products = productService.findProductsByIds(productIds);
                return ResponseEntity.ok(ResponseObject.builder()
                                .data(products)
                                .message("Get products successfully")
                                .status(HttpStatus.OK)
                                .build());
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @Operation(security = { @SecurityRequirement(name = "bearer-key") })
        public ResponseEntity<ResponseObject> deleteProduct(@PathVariable long id) {
                productService.deleteProduct(id);
                return ResponseEntity.ok(ResponseObject.builder()
                                .data(null)
                                .message(String.format("Product with id = %d deleted successfully", id))
                                .status(HttpStatus.OK)
                                .build());
        }

        // @PostMapping("/generateFakeProducts")
        private ResponseEntity<ResponseObject> generateFakeProducts() throws Exception {
                Faker faker = new Faker();
                for (int i = 0; i < 1_000_000; i++) {
                        String productName = faker.commerce().productName();
                        if (productService.existsByName(productName)) {
                                continue;
                        }
                        ProductDTO productDTO = ProductDTO.builder()
                                        .name(productName)
                                        .price((float) faker.number().numberBetween(10, 90_000_000))
                                        .description(faker.lorem().sentence())
                                        .thumbnail("")
                                        .categoryId((long) faker.number().numberBetween(2, 5))
                                        .build();
                        productService.createProduct(productDTO);
                }
                return ResponseEntity.ok(ResponseObject.builder()
                                .message("Insert fake products succcessfully")
                                .data(null)
                                .status(HttpStatus.OK)
                                .build());
        }

        // update a product
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        // @SecurityRequirement(name="bearer-key")
        @Operation(security = { @SecurityRequirement(name = "bearer-key") })
        public ResponseEntity<ResponseObject> updateProduct(
                        @PathVariable long id,
                        @RequestBody ProductDTO productDTO) throws Exception {
                Product updatedProduct = productService.updateProduct(id, productDTO);
                return ResponseEntity.ok(ResponseObject.builder()
                                .data(updatedProduct)
                                .message("Update product successfully")
                                .status(HttpStatus.OK)
                                .build());
        }

        @PostMapping("/like/{productId}")
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
        public ResponseEntity<ResponseObject> likeProduct(@PathVariable Long productId) throws Exception {
                User loginUser = securityUtils.getLoggedInUser();
                Product likedProduct = productService.likeProduct(loginUser.getId(), productId);
                return ResponseEntity.ok(ResponseObject.builder()
                                .data(ProductResponse.fromProduct(likedProduct))
                                .message("Like product successfully")
                                .status(HttpStatus.OK)
                                .build());
        }

        @PostMapping("/unlike/{productId}")
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
        public ResponseEntity<ResponseObject> unlikeProduct(@PathVariable Long productId) throws Exception {
                User loginUser = securityUtils.getLoggedInUser();
                Product unlikedProduct = productService.unlikeProduct(loginUser.getId(), productId);
                return ResponseEntity.ok(ResponseObject.builder()
                                .data(ProductResponse.fromProduct(unlikedProduct))
                                .message("Unlike product successfully")
                                .status(HttpStatus.OK)
                                .build());
        }

        @PostMapping("/favorite-products")
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
        public ResponseEntity<ResponseObject> findFavoriteProductsByUserId() throws Exception {
                User loginUser = securityUtils.getLoggedInUser();
                List<ProductResponse> favoriteProducts = productService.findFavoriteProductsByUserId(loginUser.getId());
                return ResponseEntity.ok(ResponseObject.builder()
                                .data(favoriteProducts)
                                .message("Favorite products retrieved successfully")
                                .status(HttpStatus.OK)
                                .build());
        }
}
