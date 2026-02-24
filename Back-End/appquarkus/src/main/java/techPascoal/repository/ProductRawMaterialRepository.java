package techPascoal.repository;

import techPascoal.entity.ProductRawMaterialEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRawMaterialRepository implements PanacheRepository<ProductRawMaterialEntity> {
}
