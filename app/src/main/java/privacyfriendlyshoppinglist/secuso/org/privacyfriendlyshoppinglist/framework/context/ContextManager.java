package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context;

import android.content.Context;
import dagger.ObjectGraph;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.dagger.context.config.AppContextModule;

/**
 * Description:
 * Author: Grebiel Jose Ifill Brito
 * Created: 18.05.16 creation date
 */
public class ContextManager<T extends ContextSetter>
{

    public T getInstance(Context context, Class<T> aClass)
    {
        T classInstance;
        ObjectGraph objectGraph = ObjectGraph.create(new AppContextModule());
        classInstance = objectGraph.get(aClass);
        classInstance.setContext(context);
        return classInstance;
    }

    public T getInstance(Class<T> aClass)
    {
        T classInstance;
        ObjectGraph objectGraph = ObjectGraph.create(new AppContextModule());
        classInstance = objectGraph.get(aClass);
        return classInstance;
    }
}
