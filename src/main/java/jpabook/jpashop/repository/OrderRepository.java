package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

  //TODO: 주문 검색
  public List<Order> findAll(OrderSearch orderSearch){
    String jpql = "select o from Order o join o.member m where o.status = :status and m.name like :name";
    return entityManager.createQuery(jpql,Order.class)
      .setParameter("status", orderSearch.getOrderStatus())
      .setParameter("name", orderSearch.getMemberName())
      .setMaxResults(1000)
      .getResultList();
  }
}
