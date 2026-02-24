package techPascoal.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "raw_materials")
public class RawMaterialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal stock_quantity;
    public RawMaterialsEntity() {}

    public RawMaterialsEntity(String name, BigDecimal stock_quantity) {
        this.name = name;
        this.stock_quantity = stock_quantity;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getStock_quantity() { return stock_quantity; }
    public void setStock_quantity(BigDecimal stock_quantity) { this.stock_quantity = stock_quantity; }
}
