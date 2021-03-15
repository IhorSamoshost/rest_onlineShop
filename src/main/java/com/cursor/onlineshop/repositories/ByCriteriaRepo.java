package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.goods.Category;
import com.cursor.onlineshop.entities.goods.Item;
import com.cursor.onlineshop.exceptions.InvalidSortValueException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ByCriteriaRepo {

    final EntityManager em;

    public ByCriteriaRepo(EntityManager em) {
        this.em = em;
    }

    public List<Category> getCategories(
            int limit,
            int offset,
            String name,
            String description
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);

        Root<Category> category = cq.from(Category.class);
        List<Predicate> predicates = new LinkedList<>();
        if (name != null && !name.isBlank()) {
            Predicate categoryNamePredicate = cb.equal(category.get("name"), name);
            predicates.add(categoryNamePredicate);
        }
        if (description != null && !description.isBlank()) {
            Predicate categoryDescriptionPredicate =
                    cb.like(category.get("description"), "%" + description + "%");
            predicates.add(categoryDescriptionPredicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(category.get("name")));
        TypedQuery<Category> query = em.createQuery(cq).setMaxResults(limit).setFirstResult(offset);
        return query.getResultList();
    }

    public List<Item> getItems(
            int limit,
            int offset,
            String category,
            String name,
            String description,
            Item.Sort sort
    ) throws InvalidSortValueException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> item = cq.from(Item.class);

//        Metamodel m = em.getMetamodel();
//        EntityType<Item> Item_ = m.entity(Item.class);
//        CriteriaQuery<Category> categoryCriteriaQuery = cb.createQuery(Category.class);
//        Root<Item> itemCategoryQuery = categoryCriteriaQuery.from(Item_);


        List<Predicate> predicates = new LinkedList<>();
        if (category != null && !category.isBlank()) {
            Subquery<Category> subquery = cq.subquery(Category.class);
            Root<Category> categoryRoot = subquery.from(Category.class);
            subquery.select(categoryRoot).distinct(true)
                    .where(cb.like(categoryRoot.get("name"), "%" + category + "%"));
            Predicate itemCategoryPredicate = cb.in(item.get("category")).value(subquery);
            predicates.add(itemCategoryPredicate);
        }
        if (name != null && !name.isBlank()) {
            Predicate itemNamePredicate = cb.equal(item.get("name"), name);
            predicates.add(itemNamePredicate);
        }
        if (description != null && !description.isBlank()) {
            Predicate itemDescriptionPredicate =
                    cb.like(item.get("description"), "%" + description + "%");
            predicates.add(itemDescriptionPredicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        switch (sort) {
            case NAME:
                cq.orderBy(cb.asc(item.get("name")));
                TypedQuery<Item> query = em.createQuery(cq).setMaxResults(limit).setFirstResult(offset);
                return query.getResultList();
            case CATEGORY:
                cq.orderBy(cb.asc(item.get("category").get("name")));
                query = em.createQuery(cq).setMaxResults(limit).setFirstResult(offset);
                return query.getResultList();
            case PRICE_ASC:
                cq.orderBy(cb.asc(item.get("price")));
                query = em.createQuery(cq).setMaxResults(limit).setFirstResult(offset);
                return query.getResultList();
//                return movieDao.findAllByMovieNameAndDescriptionLikeOrderByRate_avgRateValueAsc(
//                        name, "%" + description + "%").stream().skip(offset).limit(limit).collect(Collectors.toList());
            case PRICE_DESC:
                cq.orderBy(cb.desc(item.get("price")));
                query = em.createQuery(cq).setMaxResults(limit).setFirstResult(offset);
                return query.getResultList();
//                return movieDao.findAllByMovieNameAndDescriptionLikeOrderByRate_avgRateValueDesc(
//                        name, "%" + description + "%").stream().skip(offset).limit(limit).collect(Collectors.toList());
            default:
                throw new InvalidSortValueException();
        }
    }
}
