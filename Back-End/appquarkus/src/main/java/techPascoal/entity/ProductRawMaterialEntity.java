package techPascoal.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_raw_materials")
public class ProductRawMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK para Produto
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductsEntity product;

    // FK para Matéria-prima
    @ManyToOne
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterialsEntity rawMaterial;

    // Quantidade necessária por produto
    @Column(name = "quantity_needed", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityRequired;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProductsEntity getProduct() { return product; }
    public void setProduct(ProductsEntity product) { this.product = product; }

    public RawMaterialsEntity getRawMaterial() { return rawMaterial; }
    public void setRawMaterial(RawMaterialsEntity rawMaterial) { this.rawMaterial = rawMaterial; }

    public BigDecimal getQuantityRequired() { return quantityRequired; }
    public void setQuantityRequired(BigDecimal quantityRequired) { this.quantityRequired = quantityRequired; }
}
