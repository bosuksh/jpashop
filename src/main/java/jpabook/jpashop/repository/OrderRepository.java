package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final EntityManager entityManager;

  public void save(Order order){
    entityManager.persist(order);
  }
  public Order findOne(Long id) {
    return entityManager.find(Order.class, id);
  }

  // 주문 검색
  public List<Order> findAll(OrderSearch orderSearch){
    String jpql = "select o from Order o join o.member m where o.status = :status and m.name like :name";
    return entityManager.createQuery(jpql,Order.class)
      .setParameter("status", orderSearch.getOrderStatus())
      .setParameter("name", orderSearch.getMemberName())
      .setMaxResults(1000)
      .getResultList();
  }

  public List<Order> findAllByCriteria(OrderSearch orderSearch){
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
    Root<Order> orderRoot = criteriaQuery.from(Order.class);
    Join<Order, Member> orderMemberJoin = orderRoot.join("member", JoinType.INNER);

    List<Predicate> criteria = new ArrayList<>();
    if(orderSearch.getOrderStatus() != null) {
      Predicate status = criteriaBuilder.equal(orderRoot.get("status"), orderSearch.getOrderStatus());
      criteria.add(status);
    }
    if(StringUtils.hasText(orderSearch.getMemberName())) {
      Predicate name = criteriaBuilder.like(orderMemberJoin.get("name"), "%"+orderSearch.getMemberName()+"%");
      criteria.add(name);
    }
    criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[criteria.size()])));
    TypedQuery<Order> query = entityManager.createQuery(criteriaQuery).setMaxResults(1000);
    return query.getResultList();
  }
}
