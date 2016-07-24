package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.ContextSetter;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductDto;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductTemplateDto;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.TotalDto;

import java.util.List;

/**
 * Description:
 * Author: Grebiel Jose Ifill Brito
 * Created: 11.06.16 creation date
 */
public interface ProductService extends ContextSetter
{
    String SORT_BY_NAME = "sort..by.name";
    String SORT_BY_COUNT = "sort.by.count";
    String SORT_BY_PRICE = "sort.by.price";
    String SORT_BY_CATEGORY = "sort.by.category";
    String SORT_BY_STORE = "sort.by.description";

    void saveOrUpdate(ProductDto dto, String listId);

    ProductDto getById(String id);

    void deleteById(String id);

    void deleteSelected(List<ProductDto> productDtos);

    List<ProductDto> getAllProducts(String listId);

    void deleteAllFromList(String listId);

    List<ProductTemplateDto> getAllTemplateProducts();

    List<ProductDto> moveSelectedToEnd(List<ProductDto> productDtos);

    TotalDto computeTotals(List<ProductDto> productDtos);

    void sortProducts(List<ProductDto> products, String criteria, boolean ascending);
}