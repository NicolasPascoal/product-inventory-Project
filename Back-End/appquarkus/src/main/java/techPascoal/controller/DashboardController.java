package techPascoal.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.enterprise.context.ApplicationScoped;

import techPascoal.entity.ProductRawMaterialEntity;
import techPascoal.entity.ProductsEntity;
import techPascoal.repository.ProductsRepository;
import techPascoal.repository.RawMaterialsRepository;
import techPascoal.repository.ProductRawMaterialRepository;

@ApplicationScoped
@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardController {

    @Inject
    ProductsRepository productsRepository;
    @Inject
    RawMaterialsRepository rawMaterialsRepository;
    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;

    @GET
    @Path("/total-products")
    public Response getTotalProducts() {

        long total = productsRepository.count();

        Map<String, Object> response = new HashMap<>();
        return Response.ok(Map.of("total", total)).build();
    }
    @GET
    @Path("/total-raw-materials")
    public Response getTotalRawMaterials() {

        long total = rawMaterialsRepository.count();

        return Response.ok(Map.of("total", total)).build();
    }
    @GET
    @Path("/low-stock-products")
    public Response getLowStockProducts() {

        BigDecimal MIN_STOCK_ALERT = new BigDecimal("10");

        List<ProductsEntity> products = productsRepository.listAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (ProductsEntity product : products) {

            List<ProductRawMaterialEntity> requirements =
                    productRawMaterialRepository.list("product.id", product.getId());

            boolean lowStock = false;

            for (ProductRawMaterialEntity req : requirements) {

                BigDecimal stock = req.getRawMaterial().getStock_quantity();

                if (stock.compareTo(MIN_STOCK_ALERT) < 0) {
                    lowStock = true;
                    break;
                }
            }

            if (lowStock) {
                result.add(Map.of(
                        "productId", product.getId(),
                        "name", product.getName()
                ));
            }
        }

        return Response.ok(result).build();
    }
    @GET
    @Path("/total-producible-products")
    public Response getTotalProducibleProducts() {

        List<ProductsEntity> products = productsRepository.listAll();

        long total = 0;

        for (ProductsEntity product : products) {

            List<ProductRawMaterialEntity> requirements =
                    productRawMaterialRepository.list("product.id", product.getId());

            if (requirements.isEmpty()) continue;

            boolean canProduce = true;

            for (ProductRawMaterialEntity req : requirements) {

                BigDecimal stock = req.getRawMaterial().getStock_quantity();

                if (stock.compareTo(req.getQuantityRequired()) < 0) {
                    canProduce = false;
                    break;
                }
            }

            if (canProduce) total++;
        }

        return Response.ok(Map.of("total", total)).build();
    }
}
