package techPascoal.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import java.util.List;

import techPascoal.entity.ProductsEntity;
import techPascoal.repository.ProductsRepository;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductsController {

    @Inject
    ProductsRepository productsRepository;

    @GET
    //route: GET | http://localhost:8080/products
    public Response getAllProducts() {
        List<ProductsEntity> products = productsRepository.listAll();
        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    //route: GET | http://localhost:8080/products/{id}
    public Response getProductById(@PathParam("id") Long id) {
        ProductsEntity product = productsRepository.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    @POST
    @Transactional
    public Response createProduct(ProductsEntity product) {

        if (product == null)
            throw new BadRequestException("Body required");

        if (product.getName() == null || product.getName().isBlank())
            throw new BadRequestException("Name required");

        if (product.getPrice() == null ||
                product.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0)
            throw new BadRequestException("Price must be positive");

        try {
            productsRepository.persist(product);
            return Response.status(Response.Status.CREATED).entity(product).build();

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create product");
        }
    }


    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateProduct(@PathParam("id") Long id, ProductsEntity updatedProduct) {

        if (updatedProduct == null)
            throw new BadRequestException("Body required");

        ProductsEntity product = productsRepository.findById(id);
        if (product == null)
            throw new NotFoundException("Product not found");

        if (updatedProduct.getName() == null || updatedProduct.getName().isBlank())
            throw new BadRequestException("Name required");

        if (updatedProduct.getPrice() == null ||
                updatedProduct.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0)
            throw new BadRequestException("Price must be positive");

        try {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());

            return Response.ok(product).build();

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to update product");
        }
    }



    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteProduct(@PathParam("id") Long id) {

        ProductsEntity product = productsRepository.findById(id);
        if (product == null)
            throw new NotFoundException("Product not found");

        try {
            productsRepository.delete(product);
            return Response.noContent().build();

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete product");
        }
    }
    }

