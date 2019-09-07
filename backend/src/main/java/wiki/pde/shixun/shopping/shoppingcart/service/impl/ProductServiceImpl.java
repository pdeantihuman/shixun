package wiki.pde.shixun.shopping.shoppingcart.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.enums.ProductStatusEnum;
import wiki.pde.shixun.shopping.shoppingcart.enums.ResultEnum;
import wiki.pde.shixun.shopping.shoppingcart.exception.MyException;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInfo;
import wiki.pde.shixun.shopping.shoppingcart.repository.ProductInfoRepository;
import wiki.pde.shixun.shopping.shoppingcart.service.CategoryService;
import wiki.pde.shixun.shopping.shoppingcart.service.ProductService;

import javax.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final
    ProductInfoRepository productInfoRepository;

    private final
    CategoryService categoryService;

    public ProductServiceImpl(ProductInfoRepository productInfoRepository, CategoryService categoryService) {
        this.productInfoRepository = productInfoRepository;
        this.categoryService = categoryService;
    }

    @Override
    public ProductInfo findOne(String productId) {

        return productInfoRepository.findByProductId(productId);
    }

    @Override
    public Page<ProductInfo> findUpAll(Pageable pageable) {
        return productInfoRepository.findAllByProductStatusOrderByProductIdAsc(ProductStatusEnum.UP.getCode(),pageable);
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAllByOrderByProductId(pageable);
    }

    @Override
    public Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable) {
        return productInfoRepository.findAllByCategoryTypeOrderByProductIdAsc(categoryType, pageable);
    }

    @Override
    @Transactional
    public void increaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) {
            throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        int update = productInfo.getProductStock() + amount;
        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void decreaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) {
            throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        int update = productInfo.getProductStock() - amount;
        if(update < 0) {
            throw new MyException(ResultEnum.PRODUCT_NOT_ENOUGH );
        }

        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) {
            throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        if (productInfo.getProductStatus().equals(ProductStatusEnum.DOWN.getCode())) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) {
            throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatus().equals(ProductStatusEnum.UP.getCode())) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfo update(ProductInfo productInfo) {
        // if null throw exception
        categoryService.findByCategoryType(productInfo.getCategoryType());
        if(productInfo.getProductStatus() > 1) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        return productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return update(productInfo);
    }

    @Override
    public void delete(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) {
            throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        productInfoRepository.delete(productInfo);

    }
}
