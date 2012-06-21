package org.genshin.warehouse.orders;

import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse.ResultCodes;
import org.genshin.warehouse.products.Product;
import org.genshin.warehouse.products.ProductDetailsActivity;
import org.genshin.warehouse.products.ProductListAdapter;
import org.genshin.warehouse.products.ProductListItem;
import org.genshin.warehouse.products.Products;
import org.genshin.warehouse.products.ProductsMenuActivity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetailsActivity extends TabActivity {
	SpreeConnector spree;
	Bundle extras;

	private TextView number;
	private TextView statement;
	private TextView mainTotal;
	private TextView shipmentState;
	private TextView paymentState;
	
	private TextView itemTotal;
	private TextView cost;
	private TextView lastTotal;
	private Button pickingButton;	
	private Button canselButton;
	private Button editButton;
	
	private TextView paymentAddress;
	private TextView shipmentAddress;
	private TextView email;
	private Button accountEditButton;
	
	Order order;
	
	private static OrderDetails orderDetails;
	private ListView listView;
	private OrderDetailsAdapter adapter;

	
	private void initViewElements() {
		number = (TextView) findViewById(R.id.order_details_number);
		statement = (TextView) findViewById(R.id.order_details_main_statement);
		mainTotal = (TextView) findViewById(R.id.order_details_main_total);
		shipmentState = (TextView) findViewById(R.id.order_details_main_shipment);
		paymentState = (TextView) findViewById(R.id.order_details_main_payment);
		
		listView = (ListView) findViewById
				(R.id.order_details_main).findViewById(R.id.order_details_menu_list);

		itemTotal = (TextView) findViewById
				(R.id.order_details_main).findViewById(R.id.order_details_item_total);
		cost = (TextView) findViewById
				(R.id.order_details_main).findViewById(R.id.order_details_cost);
		lastTotal =(TextView) findViewById
				(R.id.order_details_main).findViewById(R.id.order_details_last_total);
		
		paymentAddress = (TextView) findViewById
				(R.id.order_details_account).findViewById(R.id.order_details_payment_address);
		shipmentAddress = (TextView) findViewById
				(R.id.order_details_account).findViewById(R.id.order_details_shipment_address);
		email = (TextView) findViewById
				(R.id.order_details_account).findViewById(R.id.order_details_email);
	}
	
	private void getOrderInfo() {
		order = OrdersMenuActivity.getSelectedOrder();
	}
	
	private void setViewFields() {
		number.setText(order.number);
		orderDetails = new OrderDetails(this, spree);
		orderDetails.processOLIContainer(order.number);
		orderDetails.putOrderDetails(order.number);
		
		statement.setText(orderDetails.statement);
		mainTotal.setText("" + orderDetails.mainTotal);
		paymentAddress.setText(orderDetails.paymentAddress);
		shipmentAddress.setText(orderDetails.shipmentAddress);
		email.setText(orderDetails.email);

		OrderLineItem[] orderLineItem = new OrderLineItem[orderDetails.list.size()];
		
		for (int i = 0; i < orderDetails.list.size(); i++) {
			OrderLineItem p = orderDetails.list.get(i);
			
			orderLineItem[i] = new OrderLineItem(p.name, p.price, p.quantity, p.total);
		}

		adapter = new OrderDetailsAdapter(this, orderLineItem);
		listView.setAdapter(adapter);
		setListViewHeightBasedOnChildren(listView);
		
		orderDetails.totalCalc(orderDetails.list.size(), orderDetails);
		itemTotal.setText("" + orderDetails.itemTotal);
		cost.setText("" + orderDetails.cost);
		lastTotal.setText("" + orderDetails.lastTotal);
	}
	
	private void hookupInterface() {
		pickingButton = (Button) findViewById
				(R.id.order_details_main).findViewById(R.id.order_details_picking_button);
		canselButton = (Button) findViewById
				(R.id.order_details_main).findViewById(R.id.order_details_cansel_button);
		editButton = (Button) findViewById
				(R.id.order_details_main).findViewById(R.id.order_details_edit_button);
		accountEditButton = (Button) findViewById
				(R.id.order_details_account).findViewById(R.id.order_details_accountedit_button);
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        
        TabHost tabHost = getTabHost();
        
        TabSpec tab1 = tabHost.newTabSpec("tab1");
        tab1.setIndicator("注文詳細");
        tab1.setContent(R.id.order_details_main);
        tabHost.addTab(tab1);
        
        TabSpec tab2 = tabHost.newTabSpec("tab2");
        tab2.setIndicator("お客様情報");
        tab2.setContent(R.id.order_details_account);
        tabHost.addTab(tab2);
        
        TabSpec tab3 = tabHost.newTabSpec("tab3");
        tab3.setIndicator("調整(値引き・追加料金)");
        tab3.setContent(R.id.order_details_adjustment);
        tabHost.addTab(tab3);
        
        TabSpec tab4 = tabHost.newTabSpec("tab4");
        tab4.setIndicator("支払い方法");
        tab4.setContent(R.id.order_details_payment);
        tabHost.addTab(tab4);
        
        TabSpec tab5 = tabHost.newTabSpec("tab5");
        tab5.setIndicator("配送");
        tab5.setContent(R.id.order_details_shipment);
        tabHost.addTab(tab5);
        
        TabSpec tab6 = tabHost.newTabSpec("tab6");
        tab6.setIndicator("返品承認");
        tab6.setContent(R.id.order_details_return);
        tabHost.addTab(tab6);
        
        tabHost.setCurrentTab(0);
        
        spree = new SpreeConnector(this);

		getOrderInfo();
		initViewElements();
		setViewFields();
		hookupInterface();
	}
	
	//public static enum menuCodes { stock, destock, registerVisualCode, addOrderImage, editOrderDetails };
	
	/*
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		Resources res = getResources();
        // メニューアイテムを追加します
        menu.add(Menu.NONE, menuCodes.stock.ordinal(), Menu.NONE, res.getString(R.string.stock_in));
        menu.add(Menu.NONE, menuCodes.destock.ordinal(), Menu.NONE, res.getString(R.string.destock));
        menu.add(Menu.NONE, menuCodes.registerVisualCode.ordinal(), Menu.NONE, res.getString(R.string.register_barcode));
        menu.add(Menu.NONE, menuCodes.addOrderImage.ordinal(), Menu.NONE, res.getString(R.string.add_order_image));
        menu.add(Menu.NONE, menuCodes.editOrderDetails.ordinal(), Menu.NONE, res.getString(R.string.edit_order_details));
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	*/	  
		//Java can't do this!? WTF!
        /*switch (item.getItemId()) {
        	default:
        		return super.onOptionsItemSelected(item);
        	case registerVisualCode:
            
        		return true;
        }*//*
		int id = item.getItemId();

		if (id == menuCodes.registerVisualCode.ordinal()) {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            //intent.putExtra("SCAN_MODE", "BARCODE_MODE");
            startActivityForResult(intent, ResultCodes.SCAN.ordinal());
        	
			return true;
		} else if (id == menuCodes.editOrderDetails.ordinal()) {
			Intent intent = new Intent(this, OrderEditActivity.class);
			startActivity(intent);
		}
        
        return false;
    }
	*/
	/*
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == ResultCodes.SCAN.ordinal()) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //TODO limit this to bar code types?
                
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
    */
	
	// ListViewの高さを動的に取得
	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 設定するListViewからアダプタを取得する
		OrderDetailsAdapter adapter = (OrderDetailsAdapter) listView.getAdapter();

		int height = 0;
		int width = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);

		// アダプタのデータ分ループして、高さなどを設定
		for (int i = 0; i < adapter.getCount(); i++) {
			View view = adapter.getView(i, null, listView);
			view.measure(width, MeasureSpec.UNSPECIFIED);
			height += view.getMeasuredHeight();
		}

		// 実際のListViewに反映する
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
}