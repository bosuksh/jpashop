package jpabook.jpashop.controller;

import jpabook.jpashop.controller.dto.BookForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
  private final ItemService itemService;

  @GetMapping("/new")
  public String createForm(Model model){
   model.addAttribute("itemForm", new BookForm());
   return "items/createItemForm";
  }

  @PostMapping("/new")
  public String create(BookForm bookForm) {
    Book book = bookForm.createBook();
    itemService.saveItem(book);
    return "redirect:/";
  }

  @GetMapping
  public String list(Model model){
    List<Item> items = itemService.findItems();
    model.addAttribute("items", items);
    return "items/itemList";
  }
}