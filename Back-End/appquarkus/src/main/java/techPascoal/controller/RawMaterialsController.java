package techPascoal.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import java.util.List;

import techPascoal.entity.RawMaterialsEntity;
import techPascoal.repository.RawMaterialsRepository;

@Path("/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialsController {

    @Inject
    RawMaterialsRepository rawMaterialsRepository;


    @GET
    // route GET http://localhost:8080/raw-materials
    public Response getAllRawMaterials() {
        List<RawMaterialsEntity> materials = rawMaterialsRepository.listAll();
        return Response.ok(materials).build();
    }


    @GET
    @Path("/{id}")
    // route GET http://localhost:8080/raw-materials/{id}
    public Response getRawMaterialById(@PathParam("id") Long id) {
        RawMaterialsEntity material = rawMaterialsRepository.findById(id);
        if (material == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(material).build();
    }

    @POST
    @Transactional
    public Response createRawMaterial(RawMaterialsEntity material) {

        if (material == null)
            throw new BadRequestException("Body required");

        if (material.getName() == null || material.getName().isBlank())
            throw new BadRequestException("Name required");

        if (material.getStock_quantity() == null ||
                material.getStock_quantity().compareTo(java.math.BigDecimal.ZERO) < 0)
            throw new BadRequestException("Stock must be zero or positive");

        try {
            rawMaterialsRepository.persist(material);
            return Response.status(Response.Status.CREATED).entity(material).build();

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create raw material");
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateRawMaterial(@PathParam("id") Long id, RawMaterialsEntity updatedMaterial) {

        if (updatedMaterial == null)
            throw new BadRequestException("Body required");

        RawMaterialsEntity material = rawMaterialsRepository.findById(id);
        if (material == null)
            throw new NotFoundException("Raw material not found");

        if (updatedMaterial.getName() == null || updatedMaterial.getName().isBlank())
            throw new BadRequestException("Name required");

        if (updatedMaterial.getStock_quantity() == null ||
                updatedMaterial.getStock_quantity().compareTo(java.math.BigDecimal.ZERO) < 0)
            throw new BadRequestException("Stock must be zero or positive");

        try {
            material.setName(updatedMaterial.getName());
            material.setStock_quantity(updatedMaterial.getStock_quantity());

            return Response.ok(material).build();

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to update raw material");
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteRawMaterial(@PathParam("id") Long id) {

        RawMaterialsEntity material = rawMaterialsRepository.findById(id);
        if (material == null)
            throw new NotFoundException("Raw material not found");

        try {
            rawMaterialsRepository.delete(material);
            return Response.noContent().build();

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete raw material");
        }
    }
}
