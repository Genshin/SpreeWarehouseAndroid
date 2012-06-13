package org.genshin.warehouse.orders;

import java.util.ArrayList;

import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;
import org.genshin.warehouse.Warehouse.ResultCodes;
import org.genshin.warehouse.orders.Order;
//import org.genshin.warehouse.orders.OrderDetailsActivity;
//import org.genshin.warehouse.orders.OrderEditActivity;
import org.genshin.warehouse.orders.OrderListAdapter;
import org.genshin.warehouse.orders.OrderListItem;
import org.genshin.warehouse.orders.Orders;
import org.genshin.warehouse.orders.OrdersMenuActivity;
import org.genshin.warehouse.orders.OrdersMenuActivity.menuCodes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OrdersMenuActivity extends Activity {
	private static Orders orders;
	private static Order selectedOrder;
	private SpreeConnector spree;
	
	private OrderListAdapter ordersAdapter;
	
	private ListView orderList;
	private MultiAutoCompleteTextView searchBar;
	private Button searchButton;
	
	private Spinner orderSpinner;
	private ArrayAdapter<String> sadapter;
	
	private ImageButton backwardButton;
	private boolean updown = false;		// falseの時は▽、trueの時は△表示
	
	private void hookupInterface() {
		orderList = (ListView) findViewById(R.id.order_menu_list);
        
        searchBar = (MultiAutoCompleteTextView) findViewById(R.id.order_menu_searchbox);
		
		//Standard text search hookup
		searchButton = (Button) findViewById(R.id.order_menu_search_button);
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				orders.textSearch(searchBar.getText().toString());
				refreshOrderMenu();
				clearImage();
				orderSpinner.setSelection(0);
			}
		});
		
		//Order spinner
		orderSpinner = (Spinner) findViewById(R.id.order_spinner);
		sadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
	    sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // アイテムを追加します
	    sadapter.add("未選択");
	    sadapter.add("初期値に戻す");
	    sadapter.add("名前順");
	    sadapter.add("値段順");
	    sadapter.add("在庫数順");
	    orderSpinner.setPrompt("ソート");
	    orderSpinner.setAdapter(sadapter);
	    
	    orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                Spinner spinner = (Spinner) parent;
                //String item = (String) spinner.getSelectedItem();
                switch(position) {
                	case 0:		// 未選択
                		break;
                	case 1:		// 初期値に戻す
                		orders.textSearch(searchBar.getText().toString());
        				refreshOrderMenu();
                		clearImage();
                		break;
                	case 2:		// 名前順
						//sortName();
						clearImage();
                		break;
	                case 3:		// 値段順
	                	//sortPrice();
	                	clearImage();
	                	break;
	                case 4:		// 在庫数順
	                	//sortCountOnHand();
	                	clearImage();
	                	break;
	                default :
	                	break;             
                }
            }

			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
	    
	    // backwardButton 並びを逆に
	    backwardButton = (ImageButton)findViewById(R.id.order_menu_backward_button);
	    backwardButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				if (!updown) {
					backwardButton.setImageResource(android.R.drawable.arrow_up_float);
					updown = true;
				} else {
					backwardButton.setImageResource(android.R.drawable.arrow_down_float);
					updown = false;
				}
				switchOrder();
			}
		});
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        Warehouse.ChangeActivityContext(this);
        
        hookupInterface();
        
        spree = new SpreeConnector(this);
        if (orders == null) {
        	orders = new Orders(this, spree);
        	orders.getNewestOrders(10);
        }
        
        refreshOrderMenu();
        
	}

	public static enum menuCodes { registerOrder };

	/*
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		Resources res = getResources();
        // メニューアイテムを追加する
        menu.add(Menu.NONE, menuCodes.registerOrder.ordinal(), Menu.NONE, res.getString(R.string.register_order));
        return super.onCreateOptionsMenu(menu);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == menuCodes.registerOrder.ordinal()) {
			Intent intent = new Intent(this, OrderEditActivity.class);
			intent.putExtra("IS_NEW", true);
            startActivity(intent);
        	
			return true;
		}
        
        return false;
    }
    */
	
	private void refreshOrderMenu() {		
		OrderListItem[] orderListItems = new OrderListItem[orders.list.size()];
		
		for (int i = 0; i < orders.list.size(); i++) {
			Order p = orders.list.get(i);
			orderListItems[i] = new OrderListItem(p.date, p.number, p.state, p.paymentState, p.shipmentState, p.email, p.count, p.price);
		}
		
		//statusText.setText(orders.count + this.getString(R.string.orders_counter) );
		
		ordersAdapter = new OrderListAdapter(this, orderListItems);
		orderList.setAdapter(ordersAdapter);
        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	//orderListClickHandler(parent, view, position);
            }
        });
        
	}
	
	/*
	public static void showOrderDetails(Context ctx, Order order) {
		OrdersMenuActivity.setSelectedOrder(order);
		Intent orderDetailsIntent = new Intent(ctx, OrderDetailsActivity.class);
    	ctx.startActivity(orderDetailsIntent);
	}
	
	private void orderListClickHandler(AdapterView<?> parent, View view, int position) {
		OrdersMenuActivity.showOrderDetails(this, orders.list.get(position));				
	}
	*/

	/*
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == ResultCodes.SCAN.ordinal()) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //TODO limit this to bar code types?
                if (format != "QR_CODE") {
                	//Assume barcode, and barcodes correlate to orders
                	//Toast.makeText(this, "[" + format + "]: " + contents + "\nSearching!", Toast.LENGTH_LONG).show();
                	orders.findByBarcode(contents);
                	//if we have one hit that's the order we want, so go to it
                	refreshOrderMenu();
                	if (orders.list.size() == 1)
                		showOrderDetails(this, orders.list.get(0));
                    
                	//Toast.makeText(this, "Results:" + orders.count, Toast.LENGTH_LONG).show();
                }
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
    */

	public static Order getSelectedOrder() {
		return selectedOrder;
	}

	public static void setSelectedOrder(Order selectedOrder) {
		if (selectedOrder == null)
			selectedOrder = new Order();
		
		OrdersMenuActivity.selectedOrder = selectedOrder;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	//
	// 各種ソート
	//
	/////////////////////////////////////////////////////////////////////////////////////

	// ▽ボタンを初期に
	public void clearImage() {
		if (updown) {
			backwardButton.setImageResource(android.R.drawable.arrow_down_float);
			updown = false;
		}
	}
	
	// 並びを逆にする
	public void switchOrder() {
		ArrayList<Order> sortedList = new ArrayList<Order>();
		
		for (int i = orders.list.size() - 1; i >= 0; i--) {
			sortedList.add(orders.list.get(i));
		}
		
		orders.list = sortedList;
		refreshOrderMenu();
	}
	
	/*
	// 値段順
	public void sortPrice() {
		Order temp;

		for (int i = 0; i < orders.list.size() - 1; i++) {
			for (int j = i + 1; j < orders.list.size(); j++) {
				if (orders.list.get(i).price < orders.list.get(j).price) {
					temp = orders.list.get(i);
					orders.list.set(i, orders.list.get(j));
					orders.list.set(j, temp);
				} else if (orders.list.get(i).price == orders.list.get(j).price) {
					if (orders.list.get(i).id > orders.list.get(j).id) {
						temp = orders.list.get(i);
						orders.list.set(i, orders.list.get(j));
						orders.list.set(j, temp);
					}
				}
			}
		}

		refreshOrderMenu();
	}
	
	// 在庫数順
	public void sortCountOnHand() {
		Order temp;
		
		for (int i = 0; i < orders.list.size() - 1; i++) {
			for (int j = i + 1; j < orders.list.size(); j++) {
				if (orders.list.get(i).countOnHand < orders.list.get(j).countOnHand) {
					temp = orders.list.get(i);
					orders.list.set(i, orders.list.get(j));
					orders.list.set(j, temp);
				} else if (orders.list.get(i).countOnHand == orders.list.get(j).countOnHand) {
					if (orders.list.get(i).id > orders.list.get(j).id) {
						temp = orders.list.get(i);
						orders.list.set(i, orders.list.get(j));
						orders.list.set(j, temp);
					}
				}
			}
		}

		refreshOrderMenu();
	}
	
	// 名前順
	public void sortName(){
		Order temp;
		
		for (int i = 0; i < orders.list.size() - 1; i++) {
			for (int j = i + 1; j < orders.list.size(); j++) {
				if (orders.list.get(i).name.compareTo(orders.list.get(j).name) > 0) {
					temp = orders.list.get(i);
					orders.list.set(i, orders.list.get(j));
					orders.list.set(j, temp);
				} else if (orders.list.get(i).name.compareTo(orders.list.get(j).name) == 0) {
					if (orders.list.get(i).id > orders.list.get(j).id) {
						temp = orders.list.get(i);
						orders.list.set(i, orders.list.get(j));
						orders.list.set(j, temp);
					}
				}
			}
		}

		refreshOrderMenu();
	}
	*/
	
	/*
	// 名前順
	public void sortName(){
		ArrayList<Order> sortedList = new ArrayList<Order>();
		String[][] sort = new String[orders.list.size()][2];
		
		for (int i = 0; i < orders.list.size(); i++) {
				sort[i][0] = orders.list.get(i).name;
				sort[i][1] = String.valueOf(orders.list.get(i).id);
		}
		
		Arrays.sort(sort, new nameComparator());
		
		for (int i = 0; i <orders.list.size(); i++) {
			for (int j = 0; j < orders.list.size(); j++)
				if (sort[i][0].equals(orders.list.get(j).name) &&
					sort[i][1] == String.valueOf(orders.list.get(j).id)) {
					sortedList.add(orders.list.get(j));
				}
		}
		
		orders.list = sortedList;
		refreshOrderMenu();
	}		
	// 比較用
	public class nameComparator implements Comparator<Object> {
		private int index;

		public int compare(Object Obj1, Object Obj2) {
			index = 0;

			String[] str1 = (String[])Obj1;
			String[] str2 = (String[])Obj2;
			
			if ((str1[index].compareTo(str2[index])) == 0) {
				index = 1;
			}
			
			return (str1[index].compareTo(str2[index]));
		}
		
	}
	*/


}
