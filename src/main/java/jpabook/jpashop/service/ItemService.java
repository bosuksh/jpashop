package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.service.dto.UpdateItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
  private final ItemRepository itemRepository;

  @Transactional
  public void saveItem(Item item){
    itemRepository.save(item);
  }

  @Transactional
  public void updateItem(Long itemId, UpdateItemDto dto) {
    Book findItem = (Book) itemRepository.findOne(itemId);
    findItem.setPrice(dto.getPrice());
    findItem.setName(dto.getName());
    findItem.setStockQuantity(dto.getStockQuantity());

  }

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public Item findOne(Long itemId){
    return itemRepository.findOne(itemId);
  }
}
