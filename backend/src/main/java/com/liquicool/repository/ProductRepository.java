package com.liquicool.repository;

import com.liquicool.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByStatusAndNameContainingIgnoreCase(Integer status, String keyword, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Product> findByStatusAndCategoryAndNameContainingIgnoreCase(
            Integer status, String category, String keyword, Pageable pageable);

    Page<Product> findByStatusAndCategory(Integer status, String category, Pageable pageable);

    @Query("select p.category, count(p) from Product p group by p.category")
    List<Object[]> countGroupByCategory();
}
