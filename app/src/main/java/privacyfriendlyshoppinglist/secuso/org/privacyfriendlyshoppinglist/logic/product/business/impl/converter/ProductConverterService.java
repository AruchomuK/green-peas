package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.impl.converter;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.ContextSetter;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.persistence.entity.ProductItemEntity;

public interface ProductConverterService extends ContextSetter
{
    void convertItemToEntity(ProductItem item, ProductItemEntity entity);

    void convertEntitiesToItem(ProductItemEntity entity, ProductItem item);

    String getDoubleAsString(Double price);

    Double getStringAsDouble(String priceString);
}
