package com.pixcat.warehouseproducts.product;

import com.pixcat.warehouseproducts.type.Kilograms;
import com.pixcat.warehouseproducts.type.Millimeters;
import com.pixcat.warehouseproducts.type.Temperature;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductRepository {

    private static final String SELECT_ALL_PRODUCTS_STATEMENT = "SELECT * FROM warehouse_products.details";
    private static final String SELECT_BARCODES_FOR_ID_STATEMENT = """
            SELECT barcode FROM warehouse_products.barcodes barcodes
            WHERE barcodes.product_id = ?;
            """;
    private static final String SELECT_PRODUCT_FOR_ID_STATEMENT = """
            SELECT * FROM warehouse_products.details details
            WHERE details.product_id = ?
            """;
    private static final String SELECT_PRODUCT_ID_FOR_BARCODE_STATEMENT = """
            SELECT product_id FROM warehouse_products.barcodes barcodes
            WHERE barcodes.barcode = ?
            """;
    private static final String SELECT_PRODUCT_ID_STATEMENT = """
            SELECT product_id FROM warehouse_products.details details
            WHERE details.product_id = ?
            """;
    private static final String DELETE_BARCODES_STATEMENT = """
            DELETE FROM warehouse_products.barcodes barcodes
            WHERE barcodes.product_id = ?
            """;
    private static final String DELETE_PRODUCTS_STATEMENT = """
            DELETE FROM warehouse_products.details details
            WHERE details.product_id = ?
            """;
    private static final String INSERT_PRODUCTS_STATEMENT = """
            INSERT INTO warehouse_products.details
            (product_id, description, storage_temp, height, width, length, weight)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String INSERT_BARCODES_STATEMENT = """
            INSERT INTO warehouse_products.barcodes
            (barcode, product_id)
            VALUES (?, ?)
            """;

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductDetails> getAllProducts() {
        final var rows = jdbcTemplate.queryForList(SELECT_ALL_PRODUCTS_STATEMENT);
        return rows.stream()
                .map(row -> ProductDetails.builder()
                        .id(ProductId.of((String) row.get("product_id")))
                        .description((String) row.get("description"))
                        .storageTemperature(Temperature.valueOf((String) row.get("storage_temp")))
                        .height(Millimeters.of((Integer) row.get("height")))
                        .width(Millimeters.of((Integer) row.get("width")))
                        .length(Millimeters.of((Integer) row.get("length")))
                        .weight(Kilograms.of((BigDecimal) row.get("weight")))
                        .barcodes(barcodesForId(ProductId.of((String) row.get("product_id"))))
                        .build()
                )
                .collect(toList());
    }

    private List<Barcode> barcodesForId(ProductId id) {
        return jdbcTemplate.queryForList(SELECT_BARCODES_FOR_ID_STATEMENT, Barcode.class, id.value);
    }

    public ProductDetails getProductById(ProductId id) {
        try {
            final var row = jdbcTemplate.queryForMap(SELECT_PRODUCT_FOR_ID_STATEMENT, id.value);
            return ProductDetails.builder()
                    .id(id)
                    .description((String) row.get("description"))
                    .storageTemperature(Temperature.valueOf((String) row.get("storage_temp")))
                    .height(Millimeters.of((Integer) row.get("height")))
                    .width(Millimeters.of((Integer) row.get("width")))
                    .length(Millimeters.of((Integer) row.get("length")))
                    .weight(Kilograms.of((BigDecimal) row.get("weight")))
                    .barcodes(barcodesForId(id))
                    .build();
        } catch (IncorrectResultSizeDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public ProductDetails getProductByBarcode(Barcode barcode) {
        try {
            final var id = jdbcTemplate
                    .queryForObject(SELECT_PRODUCT_ID_FOR_BARCODE_STATEMENT, ProductId.class, barcode.value);
            if (id == null) {
                return null;
            }

            return getProductById(id);
        } catch (IncorrectResultSizeDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public boolean containsProduct(ProductId id) {
        try {
            final var foundId = jdbcTemplate.queryForObject(SELECT_PRODUCT_ID_STATEMENT, String.class, id.value);

            return foundId != null;
        } catch (IncorrectResultSizeDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void createProduct(ProductId id, ProductDetails details) {
        jdbcTemplate.update(DELETE_BARCODES_STATEMENT, id.value);
        jdbcTemplate.update(DELETE_PRODUCTS_STATEMENT, id.value);

        jdbcTemplate.update(INSERT_PRODUCTS_STATEMENT,
                id.value,
                details.getDescription(),
                details.getStorageTemperature().toString(),
                details.getHeight().value,
                details.getWidth().value,
                details.getLength().value,
                details.getWeight().value
        );
        final var barcodes = details.getBarcodes();
        jdbcTemplate.batchUpdate(INSERT_BARCODES_STATEMENT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, barcodes.get(i).value);
                ps.setString(2, id.value);
            }

            @Override
            public int getBatchSize() {
                return barcodes.size();
            }
        });
    }
}
