package preference;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.XmlRes;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.denley.wearpreferenceactivity.R;
import preference.internal.ListItemLayout;
import preference.internal.TitledWearActivity;
import preference.internal.WearPreferenceItem;
import preference.internal.WearPreferenceScreen;

/**
 * An Activity that will show preferences items in a list.
 *
 * Users should subclass this class and call {@link #addPreferencesFromResource(int)} to populate the list of preference items.
 */
public abstract class WearPreferenceActivity extends TitledWearActivity {

    private WearableRecyclerView list;
    private List<WearPreferenceItem> preferences = new ArrayList<>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preference_list);
        list = findViewById(android.R.id.list);
        WearableRecyclerView.LayoutManager layoutManager = new WearableLinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
    }

    /**
     * Inflates the preferences from the given resource and displays them on this page.
     * @param prefsResId    The resource ID of the preferences xml file.
     */
    protected void  addPreferencesFromResource(@XmlRes int prefsResId) {
        addPreferencesFromResource(prefsResId, new XmlPreferenceParser());
    }

    /**
     * Inflates the preferences from the given resource and displays them on this page.
     * @param prefsResId    The resource ID of the preferences xml file.
     * @param parser        A parser used to parse custom preference types
     */
    protected void addPreferencesFromResource(@XmlRes int prefsResId, @NonNull XmlPreferenceParser parser) {
        final WearPreferenceScreen prefsRoot = parser.parse(this, prefsResId);
        addPreferencesFromPreferenceScreen(prefsRoot);
    }

    /** DO NOT USE - For internal use only */
    protected void addPreferencesFromPreferenceScreen(WearPreferenceScreen preferenceScreen){
        addPreferences(preferenceScreen.getChildren());
    }

    private void addPreferences(List<WearPreferenceItem> newPreferences){
        preferences = newPreferences;
        SettingsAdapter adapter = new SettingsAdapter(preferences);
        list.setAdapter(adapter);
    }

    private class SettingsAdapter extends WearableRecyclerView.Adapter<SettingsAdapter.PreferenceRecyclerViewHolder> {
        private final List<WearPreferenceItem> preferences;
        public SettingsAdapter(List<WearPreferenceItem> newPreferences) {
            preferences = newPreferences;
        }
        @NonNull
        @Override
        public PreferenceRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final ListItemLayout itemView = new ListItemLayout(WearPreferenceActivity.this);
            return new PreferenceRecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PreferenceRecyclerViewHolder holder, int position) {
            final WearPreferenceItem preference = preferences.get(position);
            final ListItemLayout itemView = (ListItemLayout)holder.itemView;
            itemView.bindPreference(preference);
            // todo: fixa itemView.onNonCenterPosition(false);
        }

        @Override
        public int getItemCount() {
            return preferences.size();
        }

        public class PreferenceRecyclerViewHolder extends WearableRecyclerView.ViewHolder implements
                View.OnClickListener {

            public PreferenceRecyclerViewHolder(final View view) {
                super(view);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick (View view) {
                int position = getAdapterPosition();
                final WearPreferenceItem clickedItem = preferences.get(position);
                clickedItem.onPreferenceClick(WearPreferenceActivity.this);
            }
        }

        @Override
        public void onViewRecycled(@NonNull PreferenceRecyclerViewHolder holder) {
            super.onViewRecycled(holder);
            final ListItemLayout itemView = (ListItemLayout)holder.itemView;
            itemView.releaseBinding();
        }
    }


    }
/*private class SettingsAdapterOld extends WearableListView.Adapter {
        @Override public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ListItemLayout itemView = new ListItemLayout(WearPreferenceActivity.this);
            return new WearableListView.ViewHolder(itemView);
        }

        @Override public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            final WearPreferenceItem preference = preferences.get(position);
            final ListItemLayout itemView = (ListItemLayout)holder.itemView;
            itemView.bindPreference(preference);
            itemView.onNonCenterPosition(false);
        }

        @Override public int getItemCount() {
            return preferences.size();
        }

        @Override public void onViewRecycled(WearableListView.ViewHolder holder) {
            final ListItemLayout itemView = (ListItemLayout)holder.itemView;
            itemView.releaseBinding();
        }
    }

}*/
