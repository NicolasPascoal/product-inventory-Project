package techPascoal.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;
import techPascoal.entity.ProductRawMaterialEntity;
import techPascoal.repository.ProductRawMaterialRepository;

@Path("/product-raw-material")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRawMaterialController {

    @Inject
    ProductRawMaterialRepository repository;

    @GET
    //route: GET | http://localhost:8080/product-raw-material
    public Response getAll() {
        List<ProductRawMaterialEntity> list = repository.listAll();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    //route: GET | http://localhost:8080/product-raw-material/{id}
    public Response getById(@PathParam("id") Long id) {
        ProductRawMaterialEntity prm = repository.findById(id);
        if (prm == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(prm).build();
    }

    @POST
    @Transactional
    public Response create(ProductRawMaterialEntity prm) {

        if (prm == null)
            throw new BadRequestException("Body is required");

        if (prm.getProduct() == null || prm.getProduct().getId() == null)
            throw new BadRequestException("Product id required");

        if (prm.getRawMaterial() == null || prm.getRawMaterial().getId() == null)
            throw new BadRequestException("Raw material id required");

        if (prm.getQuantityRequired() == null ||
                prm.getQuantityRequired().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }

        try {
            repository.persist(prm);
            return Response.status(Response.Status.CREATED).entity(prm).build();

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create product raw material");
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, ProductRawMaterialEntity updated) {

        if (updated == null)
            throw new BadRequestException("Body is required");

        ProductRawMaterialEntity prm = repository.findById(id);
        if (prm == null)
            throw new NotFoundException("Link not found");

        try {
            prm.setProduct(updated.getProduct());
            prm.setRawMaterial(updated.getRawMaterial());
            prm.setQuantityRequired(updated.getQuantityRequired());

            return Response.ok(prm).build();

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to update link");
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {

        try {
            boolean deleted = repository.deleteById(id);

            if (!deleted)
                throw new NotFoundException("Link not found");

            return Response.noContent().build();

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete link");
        }
    }
}
