package preference.internal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import me.denley.wearpreferenceactivity.R;

public class ListChooserActivity extends TitledWearActivity {

    public static Intent createIntent(Context context, String key, String title, int icon,
                                      CharSequence[] entries, CharSequence[] entryValues,
                                      int[] entryIcons,
                                      int currentValue){

        final Intent launcherIntent = new Intent(context, ListChooserActivity.class);
        launcherIntent.putExtra(EXTRA_PREF_KEY, key);
        launcherIntent.putExtra(EXTRA_TITLE, title);
        launcherIntent.putExtra(EXTRA_ICON, icon);
        launcherIntent.putExtra(EXTRA_ENTRIES, entries);
        launcherIntent.putExtra(EXTRA_ENTRY_VALUES, entryValues);
        launcherIntent.putExtra(EXTRA_ENTRY_ICONS, entryIcons);
        launcherIntent.putExtra(EXTRA_CURRENT_VALUE, currentValue);
        return launcherIntent;
    }

    public static final String EXTRA_PREF_KEY = "key";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_ICON = "icon";
    public static final String EXTRA_ENTRIES = "entries";
    public static final String EXTRA_ENTRY_VALUES = "values";
    public static final String EXTRA_ENTRY_ICONS = "icons";
    public static final String EXTRA_CURRENT_VALUE = "current_value";



    String key;
    @DrawableRes private int icon;
    CharSequence[] entries;
    CharSequence[] values;
    int[] icons;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_list);

        loadIntentExtras();
        checkRequiredExtras();

        final WearableRecyclerView list = findViewById(android.R.id.list);
        list.setAdapter(new PreferenceEntriesAdapter());
        WearableRecyclerView.LayoutManager layoutManager = new WearableLinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        // list.scrollToPosition makes our automatic heading adjustment fail so use this instead
        ((WearableLinearLayoutManager)list.getLayoutManager()).scrollToPositionWithOffset(getIntent().getIntExtra(EXTRA_CURRENT_VALUE, 0), 0);
        /*Log.d("ListChooserActivity",
                "scrollToPositionWithOffset("+getIntent().getIntExtra(EXTRA_CURRENT_VALUE, 0)+")");*/
    }

    private void loadIntentExtras(){
        key = getIntent().getStringExtra(EXTRA_PREF_KEY);
        setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        icon = getIntent().getIntExtra(EXTRA_ICON, 0);
        entries = getIntent().getCharSequenceArrayExtra(EXTRA_ENTRIES);
        values = getIntent().getCharSequenceArrayExtra(EXTRA_ENTRY_VALUES);
        icons = getIntent().getIntArrayExtra(EXTRA_ENTRY_ICONS);
    }

    private void checkRequiredExtras(){
        if(key==null || key.isEmpty()){
            throw new IllegalArgumentException("Missing required extra EXTRA_PREF_KEY (preference key)");
        }
        if(entries==null){
            throw new IllegalArgumentException("Missing required extra EXTRA_ENTRIES (entry names)");
        }
        if(values==null){
            throw new IllegalArgumentException("Missing required extra EXTRA_ENTRY_VALUES (preference entry values)");
        }
    }


    private class PreferenceEntriesAdapter extends WearableRecyclerView.Adapter<PreferenceEntriesAdapter.RecyclerViewHolder> {

        @Override @NonNull
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final ListItemLayout itemView = new ListItemLayout(ListChooserActivity.this);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            final ListItemLayout itemView = (ListItemLayout)holder.itemView;
            itemView.bindView(icons != null ? icons[position] : icon, entries[position], null);
        }

        @Override public int getItemCount() {
            return entries.length;
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements
                View.OnClickListener {

            public RecyclerViewHolder(final View view) {
                super(view);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick (View view) {
                int position = getAdapterPosition();
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListChooserActivity.this);
                prefs.edit().putString(key, values[position].toString()).apply();
                finish();
            }
        }
    }

}
