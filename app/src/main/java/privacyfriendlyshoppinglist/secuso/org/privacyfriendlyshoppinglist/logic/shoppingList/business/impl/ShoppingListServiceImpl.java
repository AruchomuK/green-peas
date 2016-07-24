package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.impl;

import android.content.Context;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.comparators.PFAComparators;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.persistence.DB;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.StringUtils;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.ProductService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.ShoppingListService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListDto;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.impl.comparators.ListsComparators;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.impl.converter.ShoppingListConverter;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.impl.validator.ShoppingListValidator;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.persistence.ShoppingListDao;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.persistence.entity.ShoppingListEntity;
import rx.Observable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Author: Grebiel Jose Ifill Brito
 * Created: 11.06.16 creation date
 */
public class ShoppingListServiceImpl implements ShoppingListService
{

    private ShoppingListDao shoppingListDao;
    private ShoppingListConverter shoppingListConverter;
    private ShoppingListValidator shoppingListValidator;
    private ProductService productService;
    private Context context;

    @Inject
    public ShoppingListServiceImpl(
            ShoppingListDao shoppingListDao,
            ShoppingListConverter shoppingListConverter,
            ShoppingListValidator shoppingListValidator
    )
    {
        this.shoppingListDao = shoppingListDao;
        this.shoppingListConverter = shoppingListConverter;
        this.shoppingListValidator = shoppingListValidator;
    }

    @Override
    public void setContext(Context context, DB db)
    {
        shoppingListDao.setContext(context, db);
        shoppingListConverter.setContext(context, db);
        this.context = context;
    }

    @Override
    public void saveOrUpdate(ListDto dto)
    {
        shoppingListValidator.validate(dto);
        if ( !dto.hasErrors() )
        {
            if ( StringUtils.isEmpty(dto.getListName()) )
            {
                dto.setListName(context.getResources().getString(R.string.default_list_name));
            }
            ShoppingListEntity entity = new ShoppingListEntity();
            shoppingListConverter.convertDtoToEntity(dto, entity);
            Long id = shoppingListDao.save(entity);
            dto.setId(id.toString());
        }
    }

    @Override
    public ListDto getById(String id)
    {
        ListDto dto = new ListDto();
        ShoppingListEntity entity = shoppingListDao.getById(Long.valueOf(id));
        shoppingListConverter.convertEntityToDto(entity, dto);
        return dto;
    }

    @Override
    public ShoppingListEntity getEntityById(String id)
    {
        return shoppingListDao.getById(Long.valueOf(id));
    }

    @Override
    public void deleteById(String id)
    {
        shoppingListDao.deleteById(Long.valueOf(id));
    }

    @Override
    public List<ListDto> getAllListDtos()
    {
        Observable<ListDto> dtos = Observable
                .from(shoppingListDao.getAllEntities())
                .map(this::getDto);
        return dtos.toList().toBlocking().single();
    }

    @Override
    public List<String> deleteSelected(List<ListDto> shoppingListDtos)
    {
        List<String> deletedIds = new ArrayList<>();
        Observable
                .from(shoppingListDtos)
                .filter(dto -> dto.isSelected())
                .subscribe(
                        dto ->
                        {
                            String id = dto.getId();
                            deleteById(id);
                            deletedIds.add(id);
                        }
                );
        return deletedIds;
    }

    @Override
    public List<ListDto> moveSelectedToEnd(List<ListDto> shoppingListDtos)
    {
        List<ListDto> nonSelectedDtos = Observable
                .from(shoppingListDtos)
                .filter(dto -> !dto.isSelected())
                .toList().toBlocking().single();

        List<ListDto> selectedDtos = Observable
                .from(shoppingListDtos)
                .filter(dto -> dto.isSelected())
                .toList().toBlocking().single();
        nonSelectedDtos.addAll(selectedDtos);
        shoppingListDtos = nonSelectedDtos;
        return shoppingListDtos;
    }

    @Override
    public void sortList(List<ListDto> lists, String criteria, boolean ascending)
    {
        if ( PFAComparators.SORT_BY_NAME.equals(criteria) )
        {
            Collections.sort(lists, ListsComparators.getNameComparator(ascending));
        }
        else if ( PFAComparators.SORT_BY_PRIORITY.equals(criteria) )
        {
            Collections.sort(lists, ListsComparators.getPriorityComparator(ascending));
        }

    }

    private ListDto getDto(ShoppingListEntity entity)
    {
        ListDto dto = new ListDto();
        shoppingListConverter.convertEntityToDto(entity, dto);
        return dto;
    }
}