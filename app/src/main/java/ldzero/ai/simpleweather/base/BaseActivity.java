package ldzero.ai.simpleweather.base;

import android.support.annotation.MenuRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * base activity
 * Created on 2018/3/18.
 *
 * @author ldzero
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuId() != 0) {
            // if subclass returns a menu id greater then 0, think subclass has menu, will inflate it.
            getMenuInflater().inflate(getMenuId(), menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    /**
     * get menu id, if subclass has menu, need to override this method, return the menu id
     *
     * @return
     */
    @MenuRes
    protected int getMenuId() {
        // override it if subclass has menu.
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        // if subclass handle this event, return true, else return super method.
        return optionsItemSel(itemId) || super.onOptionsItemSelected(item);
    }

    /**
     * if subclass need to handle option item selected event, need to override this method,
     * if subclass has process this event, need to return true.
     *
     * @param itemId option item id
     * @return has subclass handle this event
     */
    protected boolean optionsItemSel(int itemId) {
        // override it if subclass need to handle menu item select event
        return false;
    }
}
