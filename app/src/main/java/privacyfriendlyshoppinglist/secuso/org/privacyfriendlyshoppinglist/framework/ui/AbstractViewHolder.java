package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.business.AbstractItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.business.PFACache;

public abstract class AbstractViewHolder<Item extends AbstractItem, Cache extends PFACache> extends RecyclerView.ViewHolder
{
    protected Cache cache;

    public AbstractViewHolder(final View parent, Cache cache)
    {
        super(parent);
        this.cache = cache;
    }

    public abstract void processItem(Item item);
}
