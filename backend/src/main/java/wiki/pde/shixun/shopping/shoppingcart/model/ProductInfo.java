package wiki.pde.shixun.shopping.shoppingcart.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class ProductInfo {
    @Id
    @Column(length = 40)
    private String productId;
    @NotNull
    private String productName;
    @NotNull
    private BigDecimal productPrice;
    /**
     * 库存
     */
    @NotNull
    @Min(0)
    private Integer productStock;
    private String productDescription;
    /**
     * 0: on-sale
     * 1: off-sale
     */
    @ColumnDefault("0")
    private Integer productStatus;
    private String productIcon;
    @ColumnDefault("0")
    private Integer categoryType;
    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;

    public ProductInfo() {
    }
}
