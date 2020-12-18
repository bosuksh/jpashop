package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
@Transactional
class OrderServiceTest {

  @Autowired
  private EntityManager entityManager;
  @Autowired
  private OrderService orderService;
  @Autowired
  private OrderRepository orderRepository;

  @Test
  public void 상품_주문() throws Exception {
    //given
    Member member = createMember("회원1", new Address("서울", "성동구", "123-123"));
    Item book = createBook("JPA책", 10000, 10);
    int orderCount = 2;

    //when
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    //then
    Order order = orderRepository.findOne(orderId);

    assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, order.getStatus());
    assertEquals("주문 상품 종류 수가 정확해야 한다.", 1, order.getOrderItems().size());
    assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, order.getTotalPrice());
    assertEquals("주문 수량 만큼 재고가 줄어야한다.",8, book.getStockQuantity());

  }


  @Test
  public void 상품주문_재고수량_초과() throws Exception {
    //given
    Member member = createMember("회원1", new Address("서울", "성동구", "123-123"));
    Item item = createBook("JPA책", 10000, 10);
    int orderCount = 11;
    //when
    NotEnoughStockException notEnoughStockException = assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), item.getId(), orderCount));
    //then
    assertEquals("에러가 발생한다.","재고가 충분하지 않습니다.",notEnoughStockException.getMessage());
  }

  @Test
  public void 주문_취소() throws Exception {
    //given
    Member member = createMember("회원1", new Address("서울", "성동구", "123-123"));
    Item book = createBook("JPA책", 10000, 10);
    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    //when
    orderService.cancelOrder(orderId);

    //then
    Order order = orderRepository.findOne(orderId);
    assertEquals("주문 취소시 상태는 cancel", OrderStatus.CANCEL, order.getStatus());
    assertEquals("주문 취소시 재고가 원복되어야 한다.", 10,book.getStockQuantity());
  }

  private Item createBook(String name, int price, int quantity) {
    Item book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(quantity);
    entityManager.persist(book);
    return book;
  }

  private Member createMember(String name, Address address) {
    Member member = new Member();
    member.setName(name);
    member.setAddress(address);
    entityManager.persist(member);
    return member;
  }
}