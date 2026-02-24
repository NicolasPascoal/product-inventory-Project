package techPascoal.repository;

import techPascoal.entity.RawMaterialsEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RawMaterialsRepository implements PanacheRepository<RawMaterialsEntity>  {
}