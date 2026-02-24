package techPascoal.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import techPascoal.entity.ProductsEntity;
import techPascoal.entity.RawMaterialsEntity;
import techPascoal.entity.ProductRawMaterialEntity;
import techPascoal.repository.ProductsRepository;
import techPascoal.repository.RawMaterialsRepository;
import techPascoal.repository.ProductRawMaterialRepository;

@Path("/production")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductionController {

    @Inject
    ProductsRepository productsRepository;

    @Inject
    RawMaterialsRepository rawMaterialsRepository;

    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;

    @GET
    @Path("/suggested")
    //route GET http://localhost:8080/production/suggested
    public Response getSuggestedProduction() {

        List<ProductsEntity> products = productsRepository.list("order by price desc");


        Map<Long, BigDecimal> stockMap = new HashMap<>();
        for (RawMaterialsEntity mat : rawMaterialsRepository.listAll()) {
            stockMap.put(mat.getId(), mat.getStock_quantity());
        }


        List<Map<String, Object>> productionPlan = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (ProductsEntity product : products) {

            List<ProductRawMaterialEntity> requirements =
                    productRawMaterialRepository.list("product.id", product.getId());


            if (requirements.isEmpty()) continue;


            BigDecimal maxQty = null;
            for (ProductRawMaterialEntity req : requirements) {
                BigDecimal stockQty = stockMap.get(req.getRawMaterial().getId());
                BigDecimal possible =
                        stockQty.divide(req.getQuantityRequired(), 0, RoundingMode.DOWN);
                if (maxQty == null || possible.compareTo(maxQty) < 0) {
                    maxQty = possible;
                }
            }

            if (maxQty == null || maxQty.compareTo(BigDecimal.ZERO) <= 0) continue;

            for (ProductRawMaterialEntity req : requirements) {
                BigDecimal newStock = stockMap.get(req.getRawMaterial().getId())
                        .subtract(req.getQuantityRequired().multiply(maxQty));
                stockMap.put(req.getRawMaterial().getId(), newStock);
            }


            BigDecimal productTotal = product.getPrice().multiply(maxQty);
            totalValue = totalValue.add(productTotal);

            Map<String, Object> planEntry = new HashMap<>();
            planEntry.put("product", product.getName());
            planEntry.put("quantity", maxQty);
            planEntry.put("totalValue", productTotal);

            productionPlan.add(planEntry);
            productionPlan.sort((a, b) -> {
                BigDecimal v1 = (BigDecimal) a.get("totalValue");
                BigDecimal v2 = (BigDecimal) b.get("totalValue");
                return v2.compareTo(v1);
            });

        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalValue", totalValue);
        response.put("productionPlan", productionPlan);

        return Response.ok(response).build();
    }
}
