package jpabook.jpashop.controller.dto;

import jpabook.jpashop.domain.item.Book;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
  private Long id;

  private String name;
  private int price;
  private int stockQuantity;

  private String author;
  private String isbn;

  public Book createBook() {
    Book book = new Book();
    book.setName(name);
    book.setAuthor(author);
    book.setIsbn(isbn);
    book.setStockQuantity(stockQuantity);
    book.setPrice(price);
    return book;
  }
}
