package org.genshin.warehouse.racks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

public class RacksMenuActivity extends Activity {
	private ExpandableListView racksRootList;

    private ExpandableListAdapter adapter;
	private void hookupInterface() {
		
		racksRootList = (ExpandableListView) findViewById(R.id.racks_root_list);

        WarehouseDivisions warehouses = Warehouse.Warehouses();
		
        ArrayList<HashMap<String, String>> warehouseRoots = new ArrayList<HashMap<String,String>>();
        ArrayList<ArrayList<HashMap<String, String>>> containerTaxonomyNodes = new ArrayList<ArrayList<HashMap<String,String>>>();
 
		for (int i = 0; i < warehouses.count; i++) {
			HashMap<String, String> warehouseDivisionMap = new HashMap<String, String>();
			warehouseDivisionMap.put("warehouse", warehouses.divisions.get(i).name);

			ArrayList<HashMap<String, String>> taxonomyNodeList = new ArrayList<HashMap<String,String>>();
			for (int j = 0; j < warehouses.divisions.get(i).containers.taxonomies.size(); j++) {
				HashMap<String, String> taxonomyNode = new HashMap<String, String>();
				taxonomyNode.put("warehouse", warehouses.divisions.get(i).name);
				taxonomyNode.put("taxonomyName", warehouses.divisions.get(i).containers.taxonomies.get(j).name);
				taxonomyNodeList.add(taxonomyNode);
				containerTaxonomyNodes.add(taxonomyNodeList);
			}
			warehouseRoots.add(warehouseDivisionMap);
		}
 
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this,
                warehouseRoots,
                android.R.layout.simple_expandable_list_item_1,
                new String[] {"warehouse"},
                new int[] { android.R.id.text1 },
                containerTaxonomyNodes,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {"taxonomyName", "warehouse"},
                new int[] { android.R.id.text1, android.R.id.text2 });
 
        racksRootList.setAdapter(adapter);  
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.racks);
		
        Warehouse.setContext(this);
        Warehouse.Warehouses();
        
		hookupInterface();
	}

}
