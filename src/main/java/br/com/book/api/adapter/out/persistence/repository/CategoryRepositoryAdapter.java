package br.com.book.api.adapter.out.persistence.repository;

import br.com.book.api.adapter.out.persistence.mapper.CategoryMapper;
import br.com.book.api.application.port.out.CategoryRepositoryPort;
import br.com.book.api.adapter.out.persistence.entity.CategoryEntity;

import br.com.book.api.domain.Category;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryRepositoryAdapter implements CategoryRepositoryPort, PanacheRepository<CategoryEntity> {

    @Inject
    CategoryMapper categoryMapper;

    @Override
    public List<Category> findAllCategories() {
        return categoryMapper.toDomainList(listAll());
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return findByIdOptional(id).map(categoryMapper::toDomain);
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return find("name = :name", Parameters.with("name", name))
                .firstResultOptional()
                .map(categoryMapper::toDomain);
    }

    @Override
    public List<Category> findCategoriesByNameContaining(String name) {
        return categoryMapper.toDomainList(
                find("name LIKE :name", Parameters.with("name", "%" + name + "%")).list()
        );
    }

    @Override
    public List<Category> findCategoriesWithDescription() {
        return categoryMapper.toDomainList(
                find("description IS NOT NULL AND description != ''").list()
        );
    }

    @Override
    public Optional<Category> findCategoryByIdWithBooks(Long id) {
        return find("SELECT DISTINCT c FROM CategoryEntity c LEFT JOIN FETCH c.books WHERE c.id = :id",
                Parameters.with("id", id))
                .firstResultOptional()
                .map(categoryMapper::toDomain);
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = categoryMapper.toEntity(category);
        persist(entity);
        return categoryMapper.toDomain(entity);
    }

    @Override
    public void deleteCategoryById(Long id) {
        deleteById(id);
    }

    @Override
    public boolean existsCategoryById(Long id) {
        return findByIdOptional(id).isPresent();
    }

    @Override
    public boolean existsCategoryByName(String name) {
        return count("name = :name", Parameters.with("name", name)) > 0;
    }
}